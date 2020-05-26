package pl.coco.compiler.annotation;

import javax.inject.Inject;
import javax.inject.Singleton;

import com.sun.source.tree.MethodTree;
import com.sun.source.util.TreeScanner;
import com.sun.tools.javac.tree.JCTree.JCMethodDecl;

@Singleton
public class ContractAnnotationVisitor extends TreeScanner<Void, Void> {

    private final ContractAnnotationProcessor annotationProcessor;

    @Inject
    public ContractAnnotationVisitor(ContractAnnotationProcessor annotationProcessor) {
        this.annotationProcessor = annotationProcessor;
    }

    @Override
    public Void visitMethod(MethodTree method, Void aVoid) {
        annotationProcessor.processMethod((JCMethodDecl) method);
        return super.visitMethod(method, aVoid);
    }
}
