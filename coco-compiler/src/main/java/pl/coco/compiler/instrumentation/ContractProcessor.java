package pl.coco.compiler.instrumentation;

import static com.sun.tools.javac.tree.JCTree.JCIdent;
import static com.sun.tools.javac.tree.JCTree.JCMethodInvocation;
import static com.sun.tools.javac.tree.JCTree.JCReturn;
import static com.sun.tools.javac.tree.JCTree.JCVariableDecl;
import static java.util.stream.Collectors.toList;

import java.util.ArrayList;

import javax.lang.model.type.TypeKind;

import com.sun.source.tree.BlockTree;
import com.sun.source.tree.ClassTree;
import com.sun.source.tree.MethodTree;
import com.sun.source.tree.StatementTree;
import com.sun.source.tree.VariableTree;
import com.sun.source.util.JavacTask;
import com.sun.tools.javac.api.JavacTaskImpl;
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

public class ContractProcessor {

    private static final String RESULT_VARIABLE_NAME = "result";

    private final JavacTaskImpl task;
    private final ClassTree clazz;
    private final MethodTree method;
    private final TreeMaker treeMaker;
    private final Names names;

    public ContractProcessor(JavacTask task,
            ClassTree clazz, MethodTree method) {
        this.task = (JavacTaskImpl) task;
        this.clazz = clazz;
        this.method = method;
        this.treeMaker = TreeMaker.instance(this.task.getContext());
        this.names = Names.instance(this.task.getContext());
    }

    // TODO: refactor util package - divide into separate packages
    // TODO: dependency injection
    // TODO: add contract inheritance
    // TODO: type checking for Contract.result() calls
    // TODO: check that Contract.result() calls occur only inside Contract.ensures()
    // TODO: add positions to all treeMaker calls
    public void process() {
        BlockTree methodBody = method.getBody();
        java.util.List<? extends StatementTree> contractStatements =
                getContractStatements(methodBody);

        if (contractStatements.size() > 0) {
            JCMethodDecl bridgeMethod = createBridgeMethod();
            addMethodToThisClass(bridgeMethod);
            java.util.List<JCStatement> processedStatements = processStatements(contractStatements,
                    bridgeMethod);
            ((JCBlock) methodBody).stats = List.from(processedStatements);
        }
    }

    private java.util.List<? extends StatementTree> getContractStatements(BlockTree methodBody) {
        return methodBody.getStatements().stream()
                .filter(ContractAstUtil::isContractInvocation)
                .collect(toList());
    }

    private JCMethodDecl createBridgeMethod() {
        BridgeMethodBuilder bridgeMethodBuilder = new BridgeMethodBuilder(task);
        return bridgeMethodBuilder.buildBridge((JCMethodDecl) method);
    }

    private void addMethodToThisClass(JCMethodDecl methodDeclaration) {
        JCClassDecl thisClassDeclaration = (JCClassDecl) this.clazz;
        thisClassDeclaration.defs = thisClassDeclaration.getMembers().append(methodDeclaration);
        thisClassDeclaration.sym.members().enter(methodDeclaration.sym);
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
            JCStatement internalContractInvocation = new InternalInvocationBuilder(task)
                    .withContractInvocation(invocation)
                    .withResultSymbol(resultSymbol)
                    .withStatement(statement)
                    .build();
            processed.add(invocation.getContractMethod(), internalContractInvocation);
        }
        return processed;
    }

    private JCStatement generateBridgeInvocationStatement(JCMethodDecl bridgeMethod,
            VarSymbol resultSymbol) {

        JCMethodInvocation bridgeMethodInvocation = generateBridgeMethodInvocation(
                bridgeMethod);

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

        java.util.List<JCIdent> parameters = getParametersForBridgeMethod(method);
        return new MethodInvocationBuilder(task)
                .withMethodSymbol(bridgeMethod.sym)
                .withArguments(List.from(parameters))
                .build();
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
