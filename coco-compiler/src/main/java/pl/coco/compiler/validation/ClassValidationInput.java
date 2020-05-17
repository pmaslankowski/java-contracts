package pl.coco.compiler.validation;

import com.sun.source.tree.ClassTree;
import com.sun.source.tree.CompilationUnitTree;
import com.sun.tools.javac.tree.JCTree.JCClassDecl;

public class ClassValidationInput {

    private final CompilationUnitTree compilationUnit;
    private final JCClassDecl clazz;

    public ClassValidationInput(CompilationUnitTree compilationUnit, ClassTree clazz) {
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
