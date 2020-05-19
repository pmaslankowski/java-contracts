package pl.coco.compiler.instrumentation;

import com.sun.source.tree.ClassTree;
import com.sun.source.tree.CompilationUnitTree;
import com.sun.source.tree.MethodTree;
import com.sun.tools.javac.tree.JCTree.JCClassDecl;
import com.sun.tools.javac.tree.JCTree.JCMethodDecl;

public class MethodInput {

    private final CompilationUnitTree compilationUnit;
    private final JCClassDecl clazz;
    private final JCMethodDecl method;

    public MethodInput(CompilationUnitTree compilationUnit, ClassTree clazz, MethodTree method) {
        this.compilationUnit = compilationUnit;
        this.clazz = (JCClassDecl) clazz;
        this.method = (JCMethodDecl) method;
    }

    public CompilationUnitTree getCompilationUnit() {
        return compilationUnit;
    }

    public JCClassDecl getClazz() {
        return clazz;
    }

    public JCMethodDecl getMethod() {
        return method;
    }

}
