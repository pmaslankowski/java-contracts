package pl.coco.compiler.util;

import java.util.List;
import java.util.Optional;

import com.sun.source.tree.AnnotationTree;
import com.sun.source.tree.ExpressionStatementTree;
import com.sun.source.tree.ExpressionTree;
import com.sun.source.tree.MethodInvocationTree;
import com.sun.source.tree.MethodTree;
import com.sun.source.tree.StatementTree;
import com.sun.tools.javac.code.Type;
import com.sun.tools.javac.tree.JCTree.JCAnnotation;

import pl.coco.compiler.instrumentation.invocation.ContractInvocation;
import pl.coco.compiler.instrumentation.invocation.SimpleMethodInvocation;

public class ContractAstUtil {

    public static final String INVARIANT_ANNOTATION_TYPE = "pl.coco.api.Invariant";

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

    // TODO: może jest lepsze miejsce na tą metodę
    public static boolean isInvariantMethod(MethodTree method) {
        List<? extends AnnotationTree> annotations = method.getModifiers().getAnnotations();
        return annotations.stream().anyMatch(ContractAstUtil::isInvariantAnnotation);
    }

    private static boolean isInvariantAnnotation(AnnotationTree annotation) {
        Type annotationType = ((JCAnnotation) annotation).type;
        return annotationType.tsym.flatName().contentEquals(INVARIANT_ANNOTATION_TYPE);
    }
}
