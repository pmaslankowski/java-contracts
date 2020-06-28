package pl.coas.compiler.instrumentation.util;

import javax.inject.Inject;
import javax.inject.Singleton;

import com.sun.tools.javac.tree.JCTree.JCExpression;
import com.sun.tools.javac.tree.TreeMaker;
import com.sun.tools.javac.util.Names;

@Singleton
public class AccessBuilder {

    private final TreeMaker treeMaker;
    private final Names names;

    @Inject
    public AccessBuilder(TreeMaker treeMaker, Names names) {
        this.treeMaker = treeMaker;
        this.names = names;
    }

    public JCExpression build(String fullyQualifiedClassName) {
        String[] classAccessPath = fullyQualifiedClassName.split("\\.");
        if (classAccessPath.length == 1) {
            return treeMaker.Ident(names.fromString(fullyQualifiedClassName));
        }

        String topPackageName = classAccessPath[0];
        JCExpression classAccess = getTopPackageAccess(topPackageName);
        for (int i = 1; i < classAccessPath.length; i++) {
            classAccess = getInnerPackageAccess(classAccess, classAccessPath[i]);
        }

        return classAccess;
    }

    private JCExpression getTopPackageAccess(String topPackageName) {
        return treeMaker.Ident(names.fromString(topPackageName));
    }

    private JCExpression getInnerPackageAccess(JCExpression classAccess, String currentPackage) {
        return treeMaker.Select(classAccess, names.fromString(currentPackage));
    }
}
