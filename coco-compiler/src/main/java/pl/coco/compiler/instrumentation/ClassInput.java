package pl.coco.compiler.instrumentation;

import com.sun.source.tree.ClassTree;
import com.sun.source.tree.CompilationUnitTree;
import com.sun.tools.javac.tree.JCTree.JCClassDecl;

public class ClassInput {

    private final CompilationUnitTree compilationUnit;
    private final JCClassDecl clazz;

    public ClassInput(CompilationUnitTree compilationUnit, ClassTree clazz) {
        this.compilationUnit = compilationUnit;
        this.clazz = (JCClassDecl) clazz;
    }

    public CompilationUnitTree getCompilationUnit() {
        return compilationUnit;
    }

    public JCClassDecl getClazz() {
        return clazz;
    }
}
