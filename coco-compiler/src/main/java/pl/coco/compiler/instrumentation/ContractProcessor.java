package pl.coco.compiler.instrumentation;

import static com.sun.tools.javac.tree.JCTree.JCIdent;
import static com.sun.tools.javac.tree.JCTree.JCMethodInvocation;
import static com.sun.tools.javac.tree.JCTree.JCReturn;
import static com.sun.tools.javac.tree.JCTree.JCVariableDecl;
import static java.util.stream.Collectors.toList;

import java.util.ArrayList;

import javax.inject.Inject;
import javax.lang.model.type.TypeKind;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
import pl.coco.compiler.instrumentation.contract.PostconditionMethodGenerator;
import pl.coco.compiler.instrumentation.contract.PreconditionMethodGenerator;
import pl.coco.compiler.instrumentation.invocation.MethodInvocationBuilder;
import pl.coco.compiler.instrumentation.invocation.MethodInvocationDescription;

public class ContractProcessor {

    private static final String RESULT_VARIABLE_NAME = "result";

    private static final Logger log = LoggerFactory.getLogger(ContractProcessor.class);

    private final TreeMaker treeMaker;
    private final Names names;
    private final ContractAnalyzer contractAnalyzer;
    private final BridgeMethodBuilder bridgeMethodBuilder;
    private final PreconditionMethodGenerator preconditionMethodGenerator;
    private final PostconditionMethodGenerator postconditionMethodGenerator;
    private final MethodInvocationBuilder methodInvocationBuilder;

    @Inject
    public ContractProcessor(
            TreeMaker treeMaker,
            Names names,
            ContractAnalyzer contractAnalyzer,
            BridgeMethodBuilder bridgeMethodBuilder,
            PreconditionMethodGenerator preconditionMethodGenerator,
            PostconditionMethodGenerator postconditionMethodGenerator,
            MethodInvocationBuilder methodInvocationBuilder) {

        this.treeMaker = treeMaker;
        this.names = names;
        this.contractAnalyzer = contractAnalyzer;
        this.bridgeMethodBuilder = bridgeMethodBuilder;
        this.preconditionMethodGenerator = preconditionMethodGenerator;
        this.postconditionMethodGenerator = postconditionMethodGenerator;
        this.methodInvocationBuilder = methodInvocationBuilder;
    }

    // TODO: add contract inheritance
    // TODO: change naming - Bridge method to target method
    // TODO: add unit test with contract in constructors
    // TODO: check that Contract.result() calls occur only inside Contract.ensures() in non void
    // method
    // TODO: add Contract.ForAll and Contract.Exists methods for array and Collections
    // TODO: type checking for Contract.result() calls
    // TODO: check that Contract.result() calls occur only inside Contract.ensures()
    public void process(MethodInput input) {
        JCClassDecl clazz = (JCClassDecl) input.getClazz();
        JCMethodDecl method = (JCMethodDecl) input.getMethod();
        JCBlock body = method.getBody();

        if (contractAnalyzer.hasContracts(clazz, method)) {
            log.debug("Detected contracts in method {}.{}", clazz.sym.getQualifiedName(),
                    method.getName());

            JCMethodDecl bridge = createBridgeMethod(method);
            addMethodToClass(bridge, clazz);

            JCMethodDecl preconditionMethod = preconditionMethodGenerator.generate(clazz, method);
            addMethodToClass(preconditionMethod, clazz);

            JCMethodDecl postconditionMethod = postconditionMethodGenerator.generate(clazz, method);
            addMethodToClass(postconditionMethod, clazz);

            java.util.List<JCStatement> processedStatements =
                    processStatements(bridge, preconditionMethod, postconditionMethod);

            body.stats = List.from(processedStatements);

            log.debug("Instrumented class code: {}", clazz);
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

    private java.util.List<JCStatement> processStatements(JCMethodDecl bridgeMethod,
            JCMethodDecl preconditionMethod, JCMethodDecl postconditionMethod) {

        java.util.List<JCStatement> processedStatements = new ArrayList<>();

        VarSymbol resultSymbol = getResultSymbol(bridgeMethod);
        JCStatement bridgeMethodStmt = generateBridgeInvocationStatement(bridgeMethod,
                resultSymbol);
        JCStatement preconditionMethodStmt = generateInvocationStatement(preconditionMethod);
        JCStatement postconditionMethodStmt = generateInvocationStatement(postconditionMethod);

        processedStatements.add(preconditionMethodStmt);
        processedStatements.add(bridgeMethodStmt);
        processedStatements.add(postconditionMethodStmt);

        if (!isVoid(bridgeMethod)) {
            JCReturn returnStatement = treeMaker
                    .Return(treeMaker.Ident(resultSymbol));
            processedStatements.add(returnStatement);
        }

        return processedStatements;
    }

    private JCStatement generateInvocationStatement(JCMethodDecl method) {
        return treeMaker.Call(generateMethodInvocation(method));
    }

    private JCStatement generateBridgeInvocationStatement(JCMethodDecl bridgeMethod,
            VarSymbol resultSymbol) {

        JCMethodInvocation bridgeMethodInvocation = generateMethodInvocation(bridgeMethod);

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

    private JCMethodInvocation generateMethodInvocation(JCMethodDecl method) {

        java.util.List<JCIdent> parameters = getParameters(method);
        MethodInvocationDescription desc = new MethodInvocationDescription.Builder()
                .withMethodSymbol(method.sym)
                .withArguments(List.from(parameters))
                .build();
        return methodInvocationBuilder.build(desc);
    }

    private java.util.List<JCIdent> getParameters(MethodTree method) {
        return method.getParameters().stream()
                .map(this::toIdentifier)
                .collect(toList());
    }

    private JCIdent toIdentifier(VariableTree param) {
        JCVariableDecl variableDec = (JCVariableDecl) param;
        return treeMaker.Ident(variableDec.sym);
    }
}
