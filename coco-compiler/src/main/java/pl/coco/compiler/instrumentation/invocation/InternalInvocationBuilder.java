package pl.coco.compiler.instrumentation.invocation;

import static com.sun.tools.javac.tree.JCTree.JCExpression;

import java.util.Arrays;

import javax.inject.Inject;

import com.sun.tools.javac.code.Symbol;
import com.sun.tools.javac.code.Type;
import com.sun.tools.javac.comp.AttrContext;
import com.sun.tools.javac.comp.Env;
import com.sun.tools.javac.comp.Resolve;
import com.sun.tools.javac.tree.JCTree.JCMethodDecl;
import com.sun.tools.javac.tree.JCTree.JCMethodInvocation;
import com.sun.tools.javac.tree.JCTree.JCStatement;
import com.sun.tools.javac.tree.TreeMaker;
import com.sun.tools.javac.util.List;
import com.sun.tools.javac.util.Name;
import com.sun.tools.javac.util.Names;

import pl.coco.compiler.instrumentation.ContractMethod;
import pl.coco.compiler.instrumentation.invocation.arguments.ArgumentsProcessor;
import pl.coco.compiler.instrumentation.invocation.arguments.ArgumentsProcessorFactory;
import pl.coco.compiler.util.TypeRegistry;

// TODO: podzielić na klasę bazową i 2 klasy: RequiresInvocationBuilder i EnsuresInvocationBuilder
// oraz użyć klasy bazowej w InvariantInvocationBuilder
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

    public JCStatement build(ContractInvocation invocation, JCMethodDecl currentMethod,
            Symbol resultSymbol) {

        MethodInvocationDescription desc = new MethodInvocationDescription.Builder()
                .withClassName(invocation.getContractMethod().getInternalClassName())
                .withArguments(getArgumentsForContractCall(invocation, resultSymbol))
                .withPosition(currentMethod.pos)
                .withMethodSymbol(getInternalContractMethodSymbol(invocation, currentMethod))
                .build();
        JCMethodInvocation methodInvocation = methodInvocationBuilder.build(desc);

        return treeMaker.at(currentMethod.pos)
                .Call(methodInvocation);
    }

    private Symbol getInternalContractMethodSymbol(ContractInvocation contractInvocation,
            JCMethodDecl currentMethod) {

        ContractMethod contractMethod = contractInvocation.getContractMethod();
        Type string = typeRegistry.getType(STRING_TYPE_NAME);
        Type conditionSupplier = typeRegistry.getType(CONDITION_SUPPLIER_TYPE);
        List<Type> arguments = List.from(Arrays.asList(conditionSupplier, string));
        return getMethodSymbol(contractMethod.getInternalClassName(),
                contractMethod.getMethodName(), currentMethod, arguments);
    }

    private Symbol getMethodSymbol(String fullyQualifiedClassName, String methodName,
            JCMethodDecl currentMethod, List<Type> arguments) {

        Env<AttrContext> env = new Env<>(currentMethod, new AttrContext());
        Type subject = typeRegistry.getType(fullyQualifiedClassName);
        Name methodIdentifier = names.fromString(methodName);
        return resolver.resolveInternalMethod(currentMethod.pos(), env, subject, methodIdentifier,
                arguments, List.nil());
    }

    private List<JCExpression> getArgumentsForContractCall(ContractInvocation invocation,
            Symbol resultSymbol) {

        ArgumentsProcessor argumentsProcessor =
                argumentsProcessorFactory.newArgumentsProcessor(invocation.getContractMethod());
        return argumentsProcessor.processArguments(invocation.getArguments(), resultSymbol);
    }
}
