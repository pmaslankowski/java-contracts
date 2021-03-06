package pl.compiler.commons.util;

import javax.lang.model.type.TypeKind;

import com.sun.source.tree.ClassTree;
import com.sun.tools.javac.code.Flags;
import com.sun.tools.javac.tree.JCTree.JCClassDecl;
import com.sun.tools.javac.tree.JCTree.JCMethodDecl;
import com.sun.tools.javac.tree.JCTree.JCVariableDecl;

public class AstUtil {

    public static final String CONSTRUCTOR_NAME = "<init>";

    public static void addMethodToClass(JCMethodDecl methodDeclaration, ClassTree clazz) {
        JCClassDecl classDeclaration = (JCClassDecl) clazz;
        classDeclaration.defs = classDeclaration.getMembers().append(methodDeclaration);
        classDeclaration.sym.members().enter(methodDeclaration.sym);
    }

    public static void addFieldToClass(JCVariableDecl field, JCClassDecl clazz) {
        clazz.defs = clazz.getMembers().append(field);
        clazz.sym.members().enter(field.sym);
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

    public static boolean isStatic(JCMethodDecl method) {
        return Flags.isStatic(method.sym);
    }
}
