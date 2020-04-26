package pl.coco.compiler.instrumentation.invocation;

import java.util.List;
import java.util.Optional;

import com.sun.source.tree.ExpressionTree;
import com.sun.source.tree.MethodInvocationTree;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.util.Name;

import pl.coco.compiler.util.TreePasser;

public class SimpleMethodInvocation {

    protected final Name methodName;
    protected final Name fullyQualifiedClassName;
    protected final List<? extends ExpressionTree> arguments;
    protected final MethodInvocationTree expression;

    protected SimpleMethodInvocation(Name methodName, Name fullyQualifiedClassName,
            List<? extends ExpressionTree> arguments,
            MethodInvocationTree expression) {

        this.methodName = methodName;
        this.fullyQualifiedClassName = fullyQualifiedClassName;
        this.arguments = arguments;
        this.expression = expression;
    }

    public static Optional<SimpleMethodInvocation> of(MethodInvocationTree expression) {
        Optional<Name> methodName = getMethodName(expression);
        Optional<Name> fullyQualifiedClassName = getFullyQualifiedClassName(expression);
        List<? extends ExpressionTree> arguments = getArguments(expression);

        if (methodName.isPresent() && fullyQualifiedClassName.isPresent()) {
            return Optional.of(new SimpleMethodInvocation(methodName.get(),
                    fullyQualifiedClassName.get(), arguments, expression));
        } else {
            return Optional.empty();
        }
    }

    private static Optional<Name> getMethodName(MethodInvocationTree methodInvocation) {
        return TreePasser.of(methodInvocation)
                .map(MethodInvocationTree::getMethodSelect)
                .as(JCTree.JCFieldAccess.class)
                .mapAndGet(access -> access.sym.name);
    }

    private static Optional<Name> getFullyQualifiedClassName(
            MethodInvocationTree methodInvocation) {

        return TreePasser.of(methodInvocation)
                .map(MethodInvocationTree::getMethodSelect)
                .as(JCTree.JCFieldAccess.class)
                .mapAndGet(access -> access.sym.owner.getQualifiedName());
    }

    private static List<? extends ExpressionTree> getArguments(
            MethodInvocationTree methodInvocation) {
        return methodInvocation.getArguments();
    }

    public boolean isMethodInvocationOf(String fullyQualifiedClassName, String methodName) {
        return this.fullyQualifiedClassName.contentEquals(fullyQualifiedClassName)
                && this.methodName.contentEquals(methodName);
    }

    public Name getMethodName() {
        return methodName;
    }

    public Name getFullyQualifiedClassName() {
        return fullyQualifiedClassName;
    }

    public List<? extends ExpressionTree> getArguments() {
        return arguments;
    }

    public MethodInvocationTree getExpression() {
        return expression;
    }
}
