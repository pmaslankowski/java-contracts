package pl.coco.compiler.util;

import com.sun.source.util.JavacTask;
import com.sun.tools.javac.api.JavacTaskImpl;
import com.sun.tools.javac.code.Symbol;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.tree.JCTree.JCExpression;
import com.sun.tools.javac.tree.TreeMaker;
import com.sun.tools.javac.util.Context;
import com.sun.tools.javac.util.List;

public class MethodInvocationBuilder {

    private final JavacTaskImpl task;
    private final TreeMaker treeMaker;

    private String className;
    private Symbol methodSymbol;
    private List<JCExpression> arguments;
    private int position;

    public MethodInvocationBuilder(JavacTask task) {
        this.task = (JavacTaskImpl) task;
        Context context = this.task.getContext();
        this.treeMaker = TreeMaker.instance(context);
    }

    public MethodInvocationBuilder withClassName(String fullyQualifiedClassName) {
        this.className = fullyQualifiedClassName;
        return this;
    }

    public MethodInvocationBuilder withMethodSymbol(Symbol methodSymbol) {
        this.methodSymbol = methodSymbol;
        return this;
    }

    public MethodInvocationBuilder withArguments(List<JCExpression> args) {
        this.arguments = args;
        return this;
    }

    public MethodInvocationBuilder withPosition(int position) {
        this.position = position;
        return this;
    }

    public JCTree.JCMethodInvocation build() {
        List<JCExpression> typeParameters = List.nil();
        JCExpression methodAccess = newMethodAccess(className);
        return treeMaker.at(position)
                .Apply(typeParameters, methodAccess, arguments)
                .setType(methodSymbol.asType().getReturnType());
    }

    private JCExpression newMethodAccess(String className) {
        if (className != null) {
            return getStaticMethodAccess(className);
        } else {
            return getInstanceMethodAccess(methodSymbol);
        }
    }

    private JCExpression getStaticMethodAccess(String className) {
        JCExpression classAccess = newClassAccess(className);
        return treeMaker.at(position).Select(classAccess, methodSymbol);
    }

    private JCExpression newClassAccess(String fullyQualifiedClassName) {
        ClassAccessBuilder classAccessBuilder = new ClassAccessBuilder(task, position);
        return classAccessBuilder.build(fullyQualifiedClassName);
    }

    private JCTree.JCIdent getInstanceMethodAccess(Symbol methodSymbol) {
        return treeMaker.at(position).Ident(methodSymbol);
    }
}
