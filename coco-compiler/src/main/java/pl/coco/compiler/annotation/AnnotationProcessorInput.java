package pl.coco.compiler.annotation;

import static com.sun.tools.javac.tree.JCTree.JCMethodDecl;

import com.sun.source.tree.CompilationUnitTree;
import com.sun.source.tree.MethodTree;
import com.sun.tools.javac.tree.JCTree.JCCompilationUnit;

public class AnnotationProcessorInput {

    private final JCCompilationUnit compilationUnit;
    private final JCMethodDecl method;

    public AnnotationProcessorInput(CompilationUnitTree compilationUnit, MethodTree method) {
        this.compilationUnit = (JCCompilationUnit) compilationUnit;
        this.method = (JCMethodDecl) method;
    }

    public JCCompilationUnit getCompilationUnit() {
        return compilationUnit;
    }

    public JCMethodDecl getMethod() {
        return method;
    }

}
