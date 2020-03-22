package pl.coco.compiler.util;

import java.util.Optional;

import com.sun.source.tree.ExpressionStatementTree;
import com.sun.source.tree.MethodInvocationTree;
import com.sun.source.tree.StatementTree;

import pl.coco.compiler.instrumentation.ContractMethodInvocation;
import pl.coco.compiler.instrumentation.SimpleMethodInvocation;

public class ContractAstUtil {

    public static Optional<ContractMethodInvocation> getContractInvocation(
            StatementTree statement) {

        return getMethodInvocation(statement)
                .flatMap(SimpleMethodInvocation::of)
                .flatMap(ContractMethodInvocation::of);
    }

    private static Optional<MethodInvocationTree> getMethodInvocation(StatementTree statement) {
        return TreePasser.of(statement)
                .as(ExpressionStatementTree.class)
                .map(ExpressionStatementTree::getExpression)
                .as(MethodInvocationTree.class)
                .get();
    }
}
