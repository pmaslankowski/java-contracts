package pl.coco.compiler.util;

import java.util.Arrays;

import com.sun.source.tree.StatementTree;
import com.sun.source.util.JavacTask;
import com.sun.tools.javac.api.JavacTaskImpl;
import com.sun.tools.javac.code.Symbol;
import com.sun.tools.javac.code.Type;
import com.sun.tools.javac.comp.AttrContext;
import com.sun.tools.javac.comp.Env;
import com.sun.tools.javac.comp.Resolve;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.tree.TreeMaker;
import com.sun.tools.javac.util.List;
import com.sun.tools.javac.util.Name;
import com.sun.tools.javac.util.Names;

import pl.coco.compiler.ContractMethod;
import pl.coco.compiler.instrumentation.ContractInvocation;

public class InternalInvocationBuilder {

    private static final String STRING_TYPE_NAME = "java.lang.String";
    private static final String BOOLEAN_TYPE_NAME = "java.lang.Boolean";

    private final JavacTaskImpl task;
    private final Resolve resolver;
    private final Names names;
    private final TreeMaker treeMaker;

    private ContractInvocation contractInvocation;
    private JCTree.JCExpressionStatement statement;
    private Symbol resultSymbol;

    public InternalInvocationBuilder(JavacTask task) {
        this.task = (JavacTaskImpl) task;
        this.resolver = Resolve.instance(this.task.getContext());
        this.names = Names.instance(this.task.getContext());
        this.treeMaker = TreeMaker.instance(this.task.getContext());
    }

    public InternalInvocationBuilder withContractInvocation(ContractInvocation contractInvocation) {
        this.contractInvocation = contractInvocation;
        return this;
    }

    public InternalInvocationBuilder withStatement(StatementTree statement) {
        this.statement = (JCTree.JCExpressionStatement) statement;
        return this;
    }

    public InternalInvocationBuilder withResultSymbol(Symbol resultSymbol) {
        this.resultSymbol = resultSymbol;
        return this;
    }

    public JCTree.JCStatement build() {
        JCTree.JCMethodInvocation methodInvocation = new MethodInvocationBuilder(task)
                .withClassName(contractInvocation.getContractMethod().getInternalClassName())
                .withArguments(getArgumentsForContractCall(contractInvocation))
                .withPosition(statement.pos)
                .withMethodSymbol(getInternalContractMethodSymbol(contractInvocation))
                .build();

        return treeMaker.at(statement.pos)
                .Call(methodInvocation);
    }

    private Symbol getInternalContractMethodSymbol(ContractInvocation contractInvocation) {
        ContractMethod contractMethod = contractInvocation.getContractMethod();
        Type string = getType(STRING_TYPE_NAME);
        Type bool = getType(BOOLEAN_TYPE_NAME);
        List<Type> arguments = List.from(Arrays.asList(bool, string));
        return getMethodSymbol(contractMethod.getInternalClassName(),
                contractMethod.getMethodName(),
                arguments);
    }

    private Type getType(String name) {
        return task.getElements().getTypeElement(name).asType();
    }

    private Symbol getMethodSymbol(String fullyQualifiedClassName, String methodName,
            List<Type> arguments) {

        Env<AttrContext> env = new Env<>(statement, new AttrContext());
        Type subject = getType(fullyQualifiedClassName);
        Name methodIdentifier = names.fromString(methodName);
        return resolver.resolveInternalMethod(statement.pos(), env, subject, methodIdentifier,
                arguments, List.nil());
    }

    private List<JCTree.JCExpression> getArgumentsForContractCall(
            ContractInvocation invocation) {

        ArgumentsProcessorFactory argumentsProcessorFactory =
                new ArgumentsProcessorFactory(task, resultSymbol);
        ArgumentsProcessor argumentsProcessor =
                argumentsProcessorFactory.newArgumentsProcessor(invocation.getContractMethod());
        return argumentsProcessor.processArguments(invocation.getArguments());
    }
}
