package pl.coco.compiler.util;

import static com.sun.tools.javac.tree.JCTree.JCExpression;

import java.util.Arrays;

import javax.inject.Inject;

import com.sun.tools.javac.code.Symbol;
import com.sun.tools.javac.code.Type;
import com.sun.tools.javac.comp.AttrContext;
import com.sun.tools.javac.comp.Env;
import com.sun.tools.javac.comp.Resolve;
import com.sun.tools.javac.tree.JCTree.JCExpressionStatement;
import com.sun.tools.javac.tree.JCTree.JCMethodInvocation;
import com.sun.tools.javac.tree.JCTree.JCStatement;
import com.sun.tools.javac.tree.TreeMaker;
import com.sun.tools.javac.util.List;
import com.sun.tools.javac.util.Name;
import com.sun.tools.javac.util.Names;

import pl.coco.compiler.ContractMethod;
import pl.coco.compiler.instrumentation.ContractInvocation;

public class InternalInvocationBuilder {

    private static final String STRING_TYPE_NAME = "java.lang.String";
    private static final String CONDITION_SUPPLIER_TYPE = "pl.coco.internal.ConditionSupplier";

    private final Resolve resolver;
    private final Names names;
    private final TreeMaker treeMaker;
    private final TypeRegistry typeRegistry;
    private final MethodInvocationBuilder methodInvocationBuilder;
    private final ArgumentsProcessorFactory argumentsProcessorFactory;

    @Inject
    public InternalInvocationBuilder(Resolve resolver, Names names, TreeMaker treeMaker,
            TypeRegistry typeRegistry, MethodInvocationBuilder methodInvocationBuilder,
            ArgumentsProcessorFactory argumentsProcessorFactory) {

        this.resolver = resolver;
        this.names = names;
        this.treeMaker = treeMaker;
        this.typeRegistry = typeRegistry;
        this.methodInvocationBuilder = methodInvocationBuilder;
        this.argumentsProcessorFactory = argumentsProcessorFactory;
    }

    public JCStatement build(ContractInvocation invocation, JCExpressionStatement statement,
            Symbol resultSymbol) {

        MethodInvocationDescription desc = new MethodInvocationDescription.Builder()
                .withClassName(invocation.getContractMethod().getInternalClassName())
                .withArguments(getArgumentsForContractCall(invocation, resultSymbol))
                .withPosition(statement.pos)
                .withMethodSymbol(getInternalContractMethodSymbol(invocation, statement))
                .build();
        JCMethodInvocation methodInvocation = methodInvocationBuilder.build(desc);

        return treeMaker.at(statement.pos)
                .Call(methodInvocation);
    }

    private Symbol getInternalContractMethodSymbol(ContractInvocation contractInvocation,
            JCExpressionStatement statement) {

        ContractMethod contractMethod = contractInvocation.getContractMethod();
        Type string = typeRegistry.getType(STRING_TYPE_NAME);
        Type conditionSupplier = typeRegistry.getType(CONDITION_SUPPLIER_TYPE);
        List<Type> arguments = List.from(Arrays.asList(conditionSupplier, string));
        return getMethodSymbol(contractMethod.getInternalClassName(),
                contractMethod.getMethodName(), statement, arguments);
    }

    private Symbol getMethodSymbol(String fullyQualifiedClassName, String methodName,
            JCExpressionStatement statement, List<Type> arguments) {

        Env<AttrContext> env = new Env<>(statement, new AttrContext());
        Type subject = typeRegistry.getType(fullyQualifiedClassName);
        Name methodIdentifier = names.fromString(methodName);
        return resolver.resolveInternalMethod(statement.pos(), env, subject, methodIdentifier,
                arguments, List.nil());
    }

    private List<JCExpression> getArgumentsForContractCall(ContractInvocation invocation,
            Symbol resultSymbol) {

        ArgumentsProcessor argumentsProcessor =
                argumentsProcessorFactory.newArgumentsProcessor(invocation.getContractMethod());
        return argumentsProcessor.processArguments(invocation.getArguments(), resultSymbol);
    }
}
