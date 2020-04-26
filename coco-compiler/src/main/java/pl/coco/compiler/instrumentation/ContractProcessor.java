package pl.coco.compiler.instrumentation;

import static com.sun.tools.javac.tree.JCTree.JCExpressionStatement;
import static com.sun.tools.javac.tree.JCTree.JCIdent;
import static com.sun.tools.javac.tree.JCTree.JCMethodInvocation;
import static com.sun.tools.javac.tree.JCTree.JCReturn;
import static com.sun.tools.javac.tree.JCTree.JCVariableDecl;
import static java.util.stream.Collectors.toList;

import java.util.ArrayList;

import javax.inject.Inject;
import javax.lang.model.type.TypeKind;

import com.sun.source.tree.BlockTree;
import com.sun.source.tree.ClassTree;
import com.sun.source.tree.MethodTree;
import com.sun.source.tree.StatementTree;
import com.sun.source.tree.VariableTree;
import com.sun.tools.javac.code.Symbol.VarSymbol;
import com.sun.tools.javac.tree.JCTree.JCBlock;
import com.sun.tools.javac.tree.JCTree.JCClassDecl;
import com.sun.tools.javac.tree.JCTree.JCMethodDecl;
import com.sun.tools.javac.tree.JCTree.JCStatement;
import com.sun.tools.javac.tree.TreeMaker;
import com.sun.tools.javac.util.List;
import com.sun.tools.javac.util.Names;

import pl.coco.compiler.util.BridgeMethodBuilder;
import pl.coco.compiler.util.ContractAstUtil;
import pl.coco.compiler.util.InternalInvocationBuilder;
import pl.coco.compiler.util.MethodInvocationBuilder;
import pl.coco.compiler.util.MethodInvocationDescription;

public class ContractProcessor {

    private static final String RESULT_VARIABLE_NAME = "result";

    private final TreeMaker treeMaker;
    private final Names names;
    private final BridgeMethodBuilder bridgeMethodBuilder;
    private final InternalInvocationBuilder internalInvocationBuilder;
    private final MethodInvocationBuilder methodInvocationBuilder;

    @Inject
    public ContractProcessor(TreeMaker treeMaker, Names names,
            BridgeMethodBuilder bridgeMethodBuilder,
            InternalInvocationBuilder internalInvocationBuilder,
            MethodInvocationBuilder methodInvocationBuilder) {

        this.treeMaker = treeMaker;
        this.names = names;
        this.bridgeMethodBuilder = bridgeMethodBuilder;
        this.internalInvocationBuilder = internalInvocationBuilder;
        this.methodInvocationBuilder = methodInvocationBuilder;
    }

    // TODO: refactor util package - divide into separate packages
    // TODO: dependency injection
    // TODO: add contract inheritance
    // TODO: type checking for Contract.result() calls
    // TODO: check that Contract.result() calls occur only inside Contract.ensures()
    // TODO: add positions to all treeMaker calls
    public void process(ContractProcessorInput input) {
        MethodTree method = input.getMethod();
        BlockTree body = method.getBody();
        java.util.List<? extends StatementTree> contractStatements = getContractStatements(body);

        if (contractStatements.size() > 0) {
            JCMethodDecl bridgeMethod = createBridgeMethod(method);
            addMethodToClass(bridgeMethod, input.getClazz());
            java.util.List<JCStatement> processedStatements = processStatements(contractStatements,
                    bridgeMethod);
            ((JCBlock) body).stats = List.from(processedStatements);
        }
    }

    private java.util.List<? extends StatementTree> getContractStatements(BlockTree methodBody) {
        return methodBody.getStatements().stream()
                .filter(ContractAstUtil::isContractInvocation)
                .collect(toList());
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
            java.util.List<? extends StatementTree> contractStatements,
            JCMethodDecl bridgeMethod) {

        VarSymbol resultSymbol = getResultSymbol(bridgeMethod);
        JCStatement bridgeInvocationStatement =
                generateBridgeInvocationStatement(bridgeMethod,
                        resultSymbol);

        ProcessedContracts processedContracts =
                processStatements(contractStatements, resultSymbol);

        java.util.List<JCStatement> processedStatements =
                new ArrayList<>(processedContracts.getPreconditions());
        processedStatements.add(bridgeInvocationStatement);
        processedStatements.addAll(processedContracts.getPostconditions());

        if (!isVoid(bridgeMethod)) {
            JCReturn returnStatement = treeMaker
                    .Return(treeMaker.Ident(resultSymbol));
            processedStatements.add(returnStatement);
        }

        return processedStatements;
    }

    private ProcessedContracts processStatements(
            java.util.List<? extends StatementTree> statements,
            VarSymbol resultSymbol) {

        ProcessedContracts processed = new ProcessedContracts();
        for (StatementTree statement : statements) {
            ContractInvocation invocation = ContractAstUtil.getContractInvocation(statement);
            JCStatement internalContractInvocation = internalInvocationBuilder
                    .build(invocation, (JCExpressionStatement) statement, resultSymbol);
            processed.add(invocation.getContractMethod(), internalContractInvocation);
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
