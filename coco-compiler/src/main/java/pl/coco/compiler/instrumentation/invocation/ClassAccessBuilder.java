package pl.coco.compiler.instrumentation.invocation;

import javax.inject.Inject;

import pl.coco.compiler.util.TypeRegistry;

import com.sun.tools.javac.code.Symbol;
import com.sun.tools.javac.code.Type;
import com.sun.tools.javac.tree.JCTree.JCExpression;
import com.sun.tools.javac.tree.TreeMaker;

public class ClassAccessBuilder {

    private final TreeMaker treeMaker;
    private final TypeRegistry typeRegistry;

    @Inject
    public ClassAccessBuilder(TreeMaker treeMaker, TypeRegistry typeRegistry) {
        this.treeMaker = treeMaker;
        this.typeRegistry = typeRegistry;
    }

    public JCExpression build(String fullyQualifiedClassName, int position) {
        String[] classAccessPath = fullyQualifiedClassName.split("\\.");

        String topPackageName = classAccessPath[0];
        JCExpression classAccess = getTopPackageAccess(topPackageName, position);

        String currentPackageName = topPackageName;
        for (int i = 1; i < classAccessPath.length - 1; i++) {
            currentPackageName = currentPackageName + "." + classAccessPath[i];
            classAccess = getInnerPackageAccess(classAccess, currentPackageName, position);
        }

        return getClassAccess(classAccess, fullyQualifiedClassName, position);
    }

    private JCExpression getTopPackageAccess(String topPackageName, int position) {
        Symbol packageSymbol = typeRegistry.getPackageSymbol(topPackageName);
        Type packageType = typeRegistry.getPackageType(topPackageName);
        JCExpression classAccess = treeMaker.at(position)
                .Ident(packageSymbol);
        return classAccess.setType(packageType);
    }

    private JCExpression getInnerPackageAccess(JCExpression previousClassAccess,
            String currentPackageName, int position) {

        Symbol packageSymbol = typeRegistry.getPackageSymbol(currentPackageName);
        Type packageType = typeRegistry.getPackageType(currentPackageName);
        JCExpression result = treeMaker.at(position)
                .Select(previousClassAccess, packageSymbol);
        return result.setType(packageType);
    }

    private JCExpression getClassAccess(JCExpression previousClassAccess,
            String fullyQualifiedClassName, int position) {

        Symbol classSymbol = typeRegistry.getClassSymbol(fullyQualifiedClassName);
        Type classType = typeRegistry.getClassType(fullyQualifiedClassName);
        JCExpression result = treeMaker.at(position)
                .Select(previousClassAccess, classSymbol);
        return result.setType(classType);
    }
}
