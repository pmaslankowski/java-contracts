package pl.coco.compiler.instrumentation.invocation.internal.precondition;

import java.util.Arrays;

import javax.inject.Inject;
import javax.inject.Singleton;

import com.sun.source.tree.ExpressionTree;
import com.sun.tools.javac.code.Symbol;
import com.sun.tools.javac.code.Type;
import com.sun.tools.javac.tree.JCTree.JCExpression;
import com.sun.tools.javac.tree.JCTree.JCLambda;
import com.sun.tools.javac.tree.JCTree.JCLiteral;
import com.sun.tools.javac.tree.JCTree.JCMethodDecl;
import com.sun.tools.javac.tree.JCTree.JCMethodInvocation;
import com.sun.tools.javac.tree.JCTree.JCStatement;
import com.sun.tools.javac.tree.TreeMaker;
import com.sun.tools.javac.util.List;

import pl.coco.compiler.instrumentation.ContractMethod;
import pl.coco.compiler.instrumentation.invocation.internal.ConditionSupplierProvider;
import pl.coco.compiler.model.ContractInvocation;
import pl.compiler.commons.invocation.MethodInvocationBuilder;
import pl.compiler.commons.invocation.MethodInvocationDescription;
import pl.compiler.commons.util.TypeRegistry;

@Singleton
public class RequiresAndAssertsInvocationBuilder {

    private static final String STRING_TYPE_NAME = "java.lang.String";
    private static final String CONDITION_SUPPLIER_TYPE = "pl.coco.internal.ConditionSupplier";

    private final TreeMaker treeMaker;
    private final TypeRegistry typeRegistry;
    private final MethodInvocationBuilder methodInvocationBuilder;
    private final ConditionSupplierProvider conditionSupplierProvider;

    @Inject
    public RequiresAndAssertsInvocationBuilder(TreeMaker treeMaker, TypeRegistry typeRegistry,
            MethodInvocationBuilder methodInvocationBuilder,
            ConditionSupplierProvider conditionSupplierProvider) {
        this.treeMaker = treeMaker;
        this.typeRegistry = typeRegistry;
        this.methodInvocationBuilder = methodInvocationBuilder;
        this.conditionSupplierProvider = conditionSupplierProvider;
    }

    public JCStatement build(ContractInvocation invocation, JCMethodDecl currentMethod) {

        MethodInvocationDescription desc = new MethodInvocationDescription.Builder()
                .withClassName(invocation.getContractMethod().getInternalClassName())
                .withArguments(getArgumentsForContractCall(invocation.getArguments()))
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
        return typeRegistry.getMethodSymbol(contractMethod.getInternalClassName(),
                contractMethod.getMethodName(), currentMethod, arguments);
    }

    private List<JCExpression> getArgumentsForContractCall(
            java.util.List<? extends ExpressionTree> arguments) {

        JCExpression precondition = (JCExpression) arguments.get(0);
        JCLiteral preconditionAsStringLiteral = treeMaker.Literal(precondition.toString());

        JCLambda conditionSupplier = conditionSupplierProvider.getSupplier(precondition);

        return List.from(Arrays.asList(conditionSupplier, preconditionAsStringLiteral));
    }
}
