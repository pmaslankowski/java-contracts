package pl.coco.compiler.util;

import com.sun.tools.javac.api.JavacTaskImpl;
import com.sun.tools.javac.code.Symbol;
import com.sun.tools.javac.code.Type;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.tree.TreeMaker;

public class ClassAccessBuilder {

    private final TreeMaker treeMaker;
    private final TypeRegistry typeRegistry;
    private final int position;

    public ClassAccessBuilder(JavacTaskImpl task, int position) {
        this.treeMaker = TreeMaker.instance(task.getContext());
        this.typeRegistry = new TypeRegistry(task);
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
        Symbol packageSymbol = typeRegistry.getPackageSymbol(topPackageName);
        Type packageType = typeRegistry.getPackageType(topPackageName);
        JCTree.JCExpression classAccess = treeMaker.at(position)
                .Ident(packageSymbol);
        return classAccess.setType(packageType);
    }

    private JCTree.JCExpression getInnerPackageAccess(JCTree.JCExpression previousClassAccess,
            String currentPackageName) {

        Symbol packageSymbol = typeRegistry.getPackageSymbol(currentPackageName);
        Type packageType = typeRegistry.getPackageType(currentPackageName);
        JCTree.JCExpression result = treeMaker.at(position)
                .Select(previousClassAccess, packageSymbol);
        return result.setType(packageType);
    }

    private JCTree.JCExpression getClassAccess(JCTree.JCExpression previousClassAccess,
            String fullyQualifiedClassName) {

        Symbol classSymbol = typeRegistry.getClassSymbol(fullyQualifiedClassName);
        Type classType = typeRegistry.getClassType(fullyQualifiedClassName);
        JCTree.JCExpression result = treeMaker.at(position)
                .Select(previousClassAccess, classSymbol);
        return result.setType(classType);
    }
}
