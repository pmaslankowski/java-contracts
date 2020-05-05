package pl.coco.compiler.util;

import javax.lang.model.type.TypeKind;

import com.sun.source.tree.ClassTree;
import com.sun.tools.javac.tree.JCTree.JCClassDecl;
import com.sun.tools.javac.tree.JCTree.JCMethodDecl;

public class AstUtil {

    public static void addMethodToClass(JCMethodDecl methodDeclaration, ClassTree clazz) {
        JCClassDecl classDeclaration = (JCClassDecl) clazz;
        classDeclaration.defs = classDeclaration.getMembers().append(methodDeclaration);
        classDeclaration.sym.members().enter(methodDeclaration.sym);
    }

    public static boolean isVoid(JCMethodDecl method) {
        return method.getReturnType().type.getKind().equals(TypeKind.VOID);
    }
}
