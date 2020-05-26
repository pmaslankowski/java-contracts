package pl.coco.compiler.annotation;

import com.sun.source.tree.ClassTree;
import com.sun.source.tree.CompilationUnitTree;
import com.sun.tools.javac.tree.JCTree.JCClassDecl;
import com.sun.tools.javac.tree.JCTree.JCCompilationUnit;

public class AnnotationProcessorClassInput {

    private final JCCompilationUnit compilationUnit;
    private final JCClassDecl clazz;

    public AnnotationProcessorClassInput(CompilationUnitTree compilationUnit, ClassTree clazz) {
        this.compilationUnit = (JCCompilationUnit) compilationUnit;
        this.clazz = (JCClassDecl) clazz;
    }

    public JCCompilationUnit getCompilationUnit() {
        return compilationUnit;
    }

    public JCClassDecl getClazz() {
        return clazz;
    }
}
