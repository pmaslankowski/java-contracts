package pl.coco.compiler.util;

import javax.lang.model.element.Element;

import com.sun.tools.javac.api.BasicJavacTask;
import com.sun.tools.javac.code.Symbol;
import com.sun.tools.javac.code.Type;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.tree.TreeMaker;

public class ClassAccessBuilder {

    private final BasicJavacTask task;
    private final TreeMaker treeMaker;
    private final int position;

    public ClassAccessBuilder(BasicJavacTask task, int position) {
        this.task = task;
        this.treeMaker = TreeMaker.instance(task.getContext());
        this.position = position;
    }

    public JCTree.JCExpression build(String fullyQualifiedClassName) {
        String[] classAccessPath = fullyQualifiedClassName.split("\\.");

        String topPackageName = classAccessPath[0];
        JCTree.JCExpression classAccess = getTopPackageAccess(topPackageName);

        String currentPackageName = topPackageName;
        for (int i = 1; i < classAccessPath.length - 1; i++) {
            currentPackageName = currentPackageName + "." + classAccessPath[i];
            classAccess = getInnerPackageAccess(classAccess, currentPackageName);
        }

        return getClassAccess(classAccess, fullyQualifiedClassName);
    }

    private JCTree.JCExpression getTopPackageAccess(String topPackageName) {
        JCTree.JCExpression classAccess = treeMaker.at(position)
                .Ident(getPackageSymbol(topPackageName));
        return classAccess.setType(getPackageType(topPackageName));
    }

    private Symbol getPackageSymbol(String packageName) {
        return (Symbol) getPackageElement(packageName);
    }

    private Element getPackageElement(String packageName) {
        return task.getElements().getPackageElement(packageName);
    }

    private Type getPackageType(String packageName) {
        return (Type) getPackageElement(packageName).asType();
    }

    private JCTree.JCExpression getInnerPackageAccess(JCTree.JCExpression previousClassAccess,
            String currentPackageName) {

        JCTree.JCExpression result = treeMaker.at(position)
                .Select(previousClassAccess, getPackageSymbol(currentPackageName));
        result.setType(getPackageType(currentPackageName));
        return result;
    }

    private JCTree.JCExpression getClassAccess(JCTree.JCExpression previousClassAccess,
            String fullyQualifiedClassName) {

        JCTree.JCExpression result = treeMaker.at(position)
                .Select(previousClassAccess, getClassSymbol(fullyQualifiedClassName));
        result.setType(getClassType(fullyQualifiedClassName));
        return result;
    }

    private Symbol getClassSymbol(String className) {
        return (Symbol) getClassElement(className);
    }

    private Type getClassType(String className) {
        return (Type) getClassElement(className).asType();
    }

    private Element getClassElement(String className) {
        return task.getElements().getTypeElement(className);
    }
}
