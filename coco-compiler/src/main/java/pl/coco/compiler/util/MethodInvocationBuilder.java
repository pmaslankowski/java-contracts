package pl.coco.compiler.util;

import javax.lang.model.element.Element;

import com.sun.source.util.JavacTask;
import com.sun.tools.javac.api.BasicJavacTask;
import com.sun.tools.javac.code.Symbol;
import com.sun.tools.javac.code.Type;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.tree.JCTree.JCExpression;
import com.sun.tools.javac.tree.JCTree.JCMethodInvocation;
import com.sun.tools.javac.tree.TreeMaker;
import com.sun.tools.javac.util.Context;
import com.sun.tools.javac.util.List;
import com.sun.tools.javac.util.Name;
import com.sun.tools.javac.util.Names;

//TODO: add type information handling to this class
public class MethodInvocationBuilder {

    private final BasicJavacTask task;
    private final TreeMaker treeMaker;
    private final Names names;

    private String className;
    private String methodName;
    private Symbol methodSymbol;
    private List<JCExpression> arguments;
    private int position;

    public MethodInvocationBuilder(JavacTask task) {
        this.task = (BasicJavacTask) task;
        Context context = this.task.getContext();
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

    public JCTree.JCStatement build() {
        List<JCExpression> typeParameters = List.nil();
        JCExpression methodAccess = newMethodAccess(className, methodName);
        JCMethodInvocation methodInvocation = treeMaker.at(position)
                .Apply(typeParameters, methodAccess, arguments)
                .setType(methodSymbol.asType().getReturnType());
        return treeMaker.at(position)
                .Call(methodInvocation);
    }

    //TODO: refactor
    public JCTree.JCMethodInvocation buildAsMethodInvocation() {
        List<JCExpression> typeParameters = List.nil();
        JCExpression methodAccess = newMethodAccess(className, methodName);
        return treeMaker.at(position)
                .Apply(typeParameters, methodAccess, arguments)
                .setType(methodSymbol.asType().getReturnType());
    }

    //TODO: refactor
    private JCExpression newMethodAccess(String className, String methodName) {
        if (className != null) {
            JCExpression classAccess = newClassAccess(className);
            return treeMaker.at(position)
                    .Select(classAccess, methodSymbol);
        } else {
            return treeMaker.at(position).Ident(methodSymbol);
        }
    }

    //TODO: add type information
    private JCExpression newClassAccess(String fullyQualifiedClassName) {
        // top package:
        String[] classAccessPath = fullyQualifiedClassName.split("\\.");
        String topPackageName = classAccessPath[0];
        JCExpression classAccess = treeMaker.at(position)
                .Ident(getPackageSymbol(topPackageName));
        classAccess.setType(getPackageType(topPackageName));

        // inner packages:
        String currentPackageName = topPackageName;
        for (int i = 1; i < classAccessPath.length - 1; i++) {
            String accessPathPart = classAccessPath[i];
            currentPackageName = currentPackageName + "." + accessPathPart;
            classAccess = treeMaker.at(position)
                    .Select(classAccess, getPackageSymbol(currentPackageName));
            classAccess.setType(getPackageType(currentPackageName));
        }

        // class name:
        String className = classAccessPath[classAccessPath.length - 1];
        classAccess = treeMaker.at(position)
                .Select(classAccess, getClassSymbol(fullyQualifiedClassName));
        classAccess.setType(getClassType(fullyQualifiedClassName));

        return classAccess;
    }

    private Name name(String nameAsString) {
        return names.fromString(nameAsString);
    }


    private Type getPackageType(String packageName) {
        return (Type) getPackageElement(packageName).asType();
    }

    private Symbol getPackageSymbol(String packageName) {
        return (Symbol) getPackageElement(packageName);
    }

    private Element getPackageElement(String packageName) {
        return task.getElements().getPackageElement(packageName);
    }

    private Type getClassType(String className) {
        return (Type) getClassElement(className).asType();
    }

    private Symbol getClassSymbol(String className) {
        return (Symbol) getClassElement(className);
    }

    private Element getClassElement(String className) {
        return task.getElements().getTypeElement(className);
    }
}
