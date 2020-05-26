package pl.coco.compiler.annotation;

import javax.inject.Inject;
import javax.inject.Singleton;

import com.sun.source.tree.CompilationUnitTree;
import com.sun.source.tree.MethodTree;
import com.sun.source.util.TreeScanner;

@Singleton
public class ContractAnnotationVisitor extends TreeScanner<Void, Void> {

    private final ContractAnnotationProcessor annotationProcessor;

    private CompilationUnitTree compilationUnit;

    @Inject
    public ContractAnnotationVisitor(ContractAnnotationProcessor annotationProcessor) {
        this.annotationProcessor = annotationProcessor;
    }

    @Override
    public Void visitCompilationUnit(CompilationUnitTree compilationUnit, Void aVoid) {
        this.compilationUnit = compilationUnit;
        return super.visitCompilationUnit(compilationUnit, aVoid);
    }

    @Override
    public Void visitMethod(MethodTree method, Void aVoid) {
        annotationProcessor.processMethod(new AnnotationProcessorInput(compilationUnit, method));
        return super.visitMethod(method, aVoid);
    }
}
