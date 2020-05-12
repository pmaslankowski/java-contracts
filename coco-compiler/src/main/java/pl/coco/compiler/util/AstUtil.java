package pl.coco.compiler.util;

import javax.lang.model.type.TypeKind;

import com.sun.source.tree.ClassTree;
import com.sun.tools.javac.tree.JCTree.JCClassDecl;
import com.sun.tools.javac.tree.JCTree.JCMethodDecl;

public class AstUtil {

    public static final String CONSTRUCTOR_NAME = "<init>";

    public static void addMethodToClass(JCMethodDecl methodDeclaration, ClassTree clazz) {
        JCClassDecl classDeclaration = (JCClassDecl) clazz;
        classDeclaration.defs = classDeclaration.getMembers().append(methodDeclaration);
        classDeclaration.sym.members().enter(methodDeclaration.sym);
    }

    public static boolean isConstructor(JCMethodDecl method) {
        return method.getName().toString().equals(CONSTRUCTOR_NAME);
    }

    public static boolean isVoid(JCMethodDecl method) {
        if (isConstructor(method)) {
            return true;
        }

        return method.getReturnType().type.getKind().equals(TypeKind.VOID);
    }
}
