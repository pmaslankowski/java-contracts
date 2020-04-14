package pl.coco.compiler.instrumentation;

import java.util.List;
import java.util.Optional;

import javax.lang.model.element.Name;

import pl.coco.compiler.util.TreePasser;

import com.sun.source.tree.ExpressionTree;
import com.sun.source.tree.MethodInvocationTree;
import com.sun.tools.javac.tree.JCTree;

public class SimpleMethodInvocation {

    protected final Name methodName;
    protected final Name fullyQualifiedClassName;
    protected final List<? extends ExpressionTree> arguments;
    protected final int pos;

    protected SimpleMethodInvocation(Name methodName, Name fullyQualifiedClassName,
                                     List<? extends ExpressionTree> arguments, int pos) {

        this.methodName = methodName;
        this.fullyQualifiedClassName = fullyQualifiedClassName;
        this.arguments = arguments;
        this.pos = pos;
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
                .as(JCTree.JCFieldAccess.class)
                .mapAndGet(access -> access.sym.name);
    }

    //TODO: type instead of name
    private static Optional<Name> getClassName(MethodInvocationTree methodInvocation) {
        return TreePasser.of(methodInvocation)
                .map(MethodInvocationTree::getMethodSelect)
                .as(JCTree.JCFieldAccess.class)
                .mapAndGet(access -> access.sym.owner.getQualifiedName());
    }

    private static List<? extends ExpressionTree> getArguments(
            MethodInvocationTree methodInvocation) {
        return methodInvocation.getArguments();
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

    public int getPos() {
        return pos;
    }
}
