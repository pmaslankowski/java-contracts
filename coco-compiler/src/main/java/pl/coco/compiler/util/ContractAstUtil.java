package pl.coco.compiler.util;

import java.util.Optional;

import com.sun.source.tree.ExpressionStatementTree;
import com.sun.source.tree.ExpressionTree;
import com.sun.source.tree.MethodInvocationTree;
import com.sun.source.tree.StatementTree;
import com.sun.tools.javac.code.Flags;
import com.sun.tools.javac.tree.JCTree.JCMethodDecl;

import pl.coco.compiler.model.ContractInvocation;
import pl.coco.compiler.model.SimpleMethodInvocation;

public class ContractAstUtil {

    public static boolean isContractInvocation(StatementTree statement) {
        return getMethodInvocation(statement)
                .flatMap(SimpleMethodInvocation::of)
                .flatMap(ContractInvocation::of)
                .isPresent();
    }

    public static boolean isContractInvocation(ExpressionTree expression) {
        return getMethodInvocationFromExpr(expression)
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

    public static ContractInvocation getContractInvocation(ExpressionTree expression) {
        return getMethodInvocationFromExpr(expression)
                .flatMap(SimpleMethodInvocation::of)
                .flatMap(ContractInvocation::of)
                .orElseThrow(() -> new IllegalArgumentException("Given expression: " + expression
                        + " is not a contract method invocation."));
    }

    private static Optional<MethodInvocationTree> getMethodInvocation(StatementTree statement) {
        return TreePasser.of(statement)
                .as(ExpressionStatementTree.class)
                .map(ExpressionStatementTree::getExpression)
                .flatMapAndGet(ContractAstUtil::getMethodInvocationFromExpr);
    }

    private static Optional<MethodInvocationTree> getMethodInvocationFromExpr(
            ExpressionTree expression) {

        return TreePasser.of(expression)
                .as(MethodInvocationTree.class)
                .get();
    }

    public static boolean isSynthetic(JCMethodDecl methodDecl) {
        long flags = methodDecl.getModifiers().flags;
        return (flags & Flags.SYNTHETIC) != 0;
    }
}
