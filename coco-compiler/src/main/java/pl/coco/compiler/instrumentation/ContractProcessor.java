package pl.coco.compiler.instrumentation;

import static com.sun.tools.javac.tree.JCTree.JCIdent;
import static com.sun.tools.javac.tree.JCTree.JCMethodInvocation;
import static com.sun.tools.javac.tree.JCTree.JCReturn;
import static com.sun.tools.javac.tree.JCTree.JCVariableDecl;
import static java.util.stream.Collectors.toList;

import java.util.ArrayList;

import javax.inject.Inject;
import javax.lang.model.type.TypeKind;

import com.sun.source.tree.ClassTree;
import com.sun.source.tree.MethodTree;
import com.sun.source.tree.VariableTree;
import com.sun.tools.javac.code.Symbol.VarSymbol;
import com.sun.tools.javac.tree.JCTree.JCBlock;
import com.sun.tools.javac.tree.JCTree.JCClassDecl;
import com.sun.tools.javac.tree.JCTree.JCMethodDecl;
import com.sun.tools.javac.tree.JCTree.JCStatement;
import com.sun.tools.javac.tree.TreeMaker;
import com.sun.tools.javac.util.List;
import com.sun.tools.javac.util.Names;

import pl.coco.compiler.instrumentation.bridge.BridgeMethodBuilder;
import pl.coco.compiler.instrumentation.contract.InternalInvocationBuilder;
import pl.coco.compiler.instrumentation.invocation.ContractInvocation;
import pl.coco.compiler.instrumentation.invocation.MethodInvocationBuilder;
import pl.coco.compiler.instrumentation.invocation.MethodInvocationDescription;

public class ContractProcessor {

    private static final String RESULT_VARIABLE_NAME = "result";

    private final TreeMaker treeMaker;
    private final Names names;
    private final ContractProvider contractProvider;
    private final BridgeMethodBuilder bridgeMethodBuilder;
    private final InternalInvocationBuilder internalInvocationBuilder;
    private final MethodInvocationBuilder methodInvocationBuilder;

    @Inject
    public ContractProcessor(
            TreeMaker treeMaker,
            Names names,
            ContractProvider contractProvider,
            BridgeMethodBuilder bridgeMethodBuilder,
            InternalInvocationBuilder internalInvocationBuilder,
            MethodInvocationBuilder methodInvocationBuilder) {

        this.treeMaker = treeMaker;
        this.names = names;
        this.contractProvider = contractProvider;
        this.bridgeMethodBuilder = bridgeMethodBuilder;
        this.internalInvocationBuilder = internalInvocationBuilder;
        this.methodInvocationBuilder = methodInvocationBuilder;
    }

    // TODO: add contract inheritance
    // TODO: add unit test with contract in constructors
    // TODO: add Contract.ForAll and Contract.Exists methods for array and Collections
    // TODO: add synthetic marker to bridge methods
    // TODO: rename bridge method to javac synthetic method naming convention with $
    // TODO: type checking for Contract.result() calls
    // TODO: check that Contract.result() calls occur only inside Contract.ensures()
    public void process(MethodInput input) {
        JCClassDecl clazz = (JCClassDecl) input.getClazz();
        JCMethodDecl method = (JCMethodDecl) input.getMethod();
        JCBlock body = method.getBody();

        java.util.List<ContractInvocation> contracts = contractProvider.getContracts(clazz, method);

        if (!contracts.isEmpty()) {
            JCMethodDecl bridge = createBridgeMethod(method);
            addMethodToClass(bridge, input.getClazz());
            java.util.List<JCStatement> processedStatements = processStatements(contracts, method,
                    bridge);
            body.stats = List.from(processedStatements);
        }
    }

    private JCMethodDecl createBridgeMethod(MethodTree method) {
        return bridgeMethodBuilder.buildBridge((JCMethodDecl) method);
    }

    private void addMethodToClass(JCMethodDecl methodDeclaration, ClassTree clazz) {
        JCClassDecl classDeclaration = (JCClassDecl) clazz;
        classDeclaration.defs = classDeclaration.getMembers().append(methodDeclaration);
        classDeclaration.sym.members().enter(methodDeclaration.sym);
    }

    private java.util.List<JCStatement> processStatements(
            java.util.List<ContractInvocation> contracts,
            JCMethodDecl currentMethod, JCMethodDecl bridgeMethod) {

        VarSymbol resultSymbol = getResultSymbol(bridgeMethod);
        JCStatement bridgeInvocationStatement = generateBridgeInvocationStatement(bridgeMethod,
                resultSymbol);

        ProcessedContracts processedContracts = processContracts(contracts, currentMethod,
                resultSymbol);

        java.util.List<JCStatement> processedStatements = new ArrayList<>(
                processedContracts.getPreconditions());
        processedStatements.add(bridgeInvocationStatement);
        processedStatements.addAll(processedContracts.getPostconditions());

        if (!isVoid(bridgeMethod)) {
            JCReturn returnStatement = treeMaker
                    .Return(treeMaker.Ident(resultSymbol));
            processedStatements.add(returnStatement);
        }

        return processedStatements;
    }

    private ProcessedContracts processContracts(java.util.List<ContractInvocation> contracts,
            JCMethodDecl currentMethod, VarSymbol resultSymbol) {

        ProcessedContracts processed = new ProcessedContracts();
        for (ContractInvocation contract : contracts) {
            JCStatement internalContractInvocation = internalInvocationBuilder
                    .build(contract, currentMethod, resultSymbol);
            processed.add(contract.getContractMethod(), internalContractInvocation);
        }
        return processed;
    }

    private JCStatement generateBridgeInvocationStatement(JCMethodDecl bridgeMethod,
            VarSymbol resultSymbol) {

        JCMethodInvocation bridgeMethodInvocation = generateBridgeMethodInvocation(bridgeMethod);

        if (isVoid(bridgeMethod)) {
            return treeMaker.Call(bridgeMethodInvocation);
        }

        return treeMaker.VarDef(resultSymbol, bridgeMethodInvocation);
    }

    private boolean isVoid(JCMethodDecl bridgeMethod) {
        return bridgeMethod.getReturnType().type.getKind().equals(TypeKind.VOID);
    }

    private VarSymbol getResultSymbol(JCMethodDecl bridgeMethod) {
        return new VarSymbol(0,
                names.fromString(RESULT_VARIABLE_NAME),
                bridgeMethod.sym.getReturnType(),
                bridgeMethod.sym);
    }

    private JCMethodInvocation generateBridgeMethodInvocation(JCMethodDecl bridgeMethod) {

        java.util.List<JCIdent> parameters = getParametersForBridgeMethod(bridgeMethod);
        MethodInvocationDescription desc = new MethodInvocationDescription.Builder()
                .withMethodSymbol(bridgeMethod.sym)
                .withArguments(List.from(parameters))
                .build();
        return methodInvocationBuilder.build(desc);
    }

    private java.util.List<JCIdent> getParametersForBridgeMethod(MethodTree method) {
        return method.getParameters().stream()
                .map(this::toIdentifier)
                .collect(toList());
    }

    private JCIdent toIdentifier(VariableTree param) {
        JCVariableDecl variableDec = (JCVariableDecl) param;
        return treeMaker.Ident(variableDec.sym);
    }
}
