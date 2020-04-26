package pl.coco.compiler.util;

import java.util.Optional;

import pl.coco.compiler.instrumentation.invocation.ContractInvocation;
import pl.coco.compiler.instrumentation.invocation.SimpleMethodInvocation;

import com.sun.source.tree.ExpressionStatementTree;
import com.sun.source.tree.MethodInvocationTree;
import com.sun.source.tree.StatementTree;

public class ContractAstUtil {

    public static boolean isContractInvocation(StatementTree statement) {
        return getMethodInvocation(statement)
                .flatMap(SimpleMethodInvocation::of)
                .flatMap(ContractInvocation::of)
                .isPresent();
    }

    public static ContractInvocation getContractInvocation(StatementTree statement) {
        return getMethodInvocation(statement)
                .flatMap(SimpleMethodInvocation::of)
                .flatMap(ContractInvocation::of)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Given statement: " + statement + " is not a contract method invocation."));
    }

    private static Optional<MethodInvocationTree> getMethodInvocation(StatementTree statement) {
        return TreePasser.of(statement)
                .as(ExpressionStatementTree.class)
                .map(ExpressionStatementTree::getExpression)
                .as(MethodInvocationTree.class)
                .get();
    }
}
