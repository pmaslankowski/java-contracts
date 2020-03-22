package pl.coco.compiler.util;

import com.sun.source.util.JavacTask;
import com.sun.tools.javac.api.BasicJavacTask;
import com.sun.tools.javac.code.Type;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.tree.JCTree.JCExpression;
import com.sun.tools.javac.tree.JCTree.JCFieldAccess;
import com.sun.tools.javac.tree.JCTree.JCMethodInvocation;
import com.sun.tools.javac.tree.TreeMaker;
import com.sun.tools.javac.util.Context;
import com.sun.tools.javac.util.List;
import com.sun.tools.javac.util.Name;
import com.sun.tools.javac.util.Names;

public class MethodInvocationBuilder {

    private final TreeMaker treeMaker;
    private final Names names;

    private String className;
    private String methodName;
    private List<JCExpression> arguments;
    private int position;

    public MethodInvocationBuilder(JavacTask task) {
        Context context = ((BasicJavacTask) task).getContext();
        this.treeMaker = TreeMaker.instance(context);
        this.names = Names.instance(context);
    }

    public MethodInvocationBuilder withClassName(String fullyQualifiedClassName) {
        this.className = fullyQualifiedClassName;
        return this;
    }

    public MethodInvocationBuilder withMethodName(String methodName) {
        this.methodName = methodName;
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

    public JCTree.JCStatement build() {
        List<JCExpression> typeParameters = List.nil();
        JCFieldAccess methodAccess = newMethodAccess(className, methodName);
        JCMethodInvocation methodInvocation = treeMaker.at(position)
                .Apply(typeParameters, methodAccess, arguments)
                .setType(new Type.JCVoidType());
        return treeMaker.at(position)
                .Call(methodInvocation);
    }

    private JCFieldAccess newMethodAccess(String className, String methodName) {
        JCExpression classAccess = newClassAccess(className);
        return treeMaker.at(position)
                .Select(classAccess, name(methodName));
    }

    private JCExpression newClassAccess(String className) {
        String[] classAccessPath = className.split("\\.");
        String topPackageName = classAccessPath[0];
        JCExpression classAccess = treeMaker.at(position)
                .Ident(name(topPackageName));

        for (int i = 1; i < classAccessPath.length; i++) {
            String accessPathPart = classAccessPath[i];
            classAccess = treeMaker.at(position)
                    .Select(classAccess, name(accessPathPart));
        }

        return classAccess;
    }

    private Name name(String nameAsString) {
        return names.fromString(nameAsString);
    }
}
