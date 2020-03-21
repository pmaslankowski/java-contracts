package pl.coco.compiler.instrumentation;

import java.util.List;
import java.util.Optional;

import javax.lang.model.element.Name;

import com.sun.source.tree.ExpressionTree;
import com.sun.source.tree.IdentifierTree;
import com.sun.source.tree.MemberSelectTree;
import com.sun.source.tree.MethodInvocationTree;
import com.sun.tools.javac.tree.JCTree;

import pl.coco.compiler.util.TreePasser;

public class SimpleMethodInvocation {

    protected final Name methodName;
    protected final Name className;
    protected final List<? extends ExpressionTree> arguments;
    protected final int position;

    protected SimpleMethodInvocation(Name methodName, Name className,
            List<? extends ExpressionTree> arguments, int position) {

        this.methodName = methodName;
        this.className = className;
        this.arguments = arguments;
        this.position = position;
    }

    public static Optional<SimpleMethodInvocation> of(MethodInvocationTree methodInvocation) {

        Optional<Name> methodName = getMethodName(methodInvocation);
        Optional<Name> className = getClassName(methodInvocation);
        List<? extends ExpressionTree> arguments = getArguments(methodInvocation);
        int pos = ((JCTree.JCMethodInvocation) methodInvocation).getStartPosition();

        if (methodName.isPresent() && className.isPresent()) {
            return Optional.of(
                    new SimpleMethodInvocation(methodName.get(), className.get(), arguments, pos));
        } else {
            return Optional.empty();
        }
    }

    private static Optional<Name> getMethodName(MethodInvocationTree methodInvocation) {
        return TreePasser.of(methodInvocation)
                .map(MethodInvocationTree::getMethodSelect)
                .as(MemberSelectTree.class)
                .mapAndGet(MemberSelectTree::getIdentifier);
    }

    private static Optional<Name> getClassName(MethodInvocationTree methodInvocation) {
        return TreePasser.of(methodInvocation)
                .map(MethodInvocationTree::getMethodSelect)
                .as(MemberSelectTree.class)
                .map(MemberSelectTree::getExpression)
                .as(IdentifierTree.class)
                .mapAndGet(IdentifierTree::getName);
    }

    private static List<? extends ExpressionTree> getArguments(
            MethodInvocationTree methodInvocation) {
        return methodInvocation.getArguments();
    }

    public Name getMethodName() {
        return methodName;
    }

    public Name getClassName() {
        return className;
    }

    public List<? extends ExpressionTree> getArguments() {
        return arguments;
    }

    public int getPosition() {
        return position;
    }
}
