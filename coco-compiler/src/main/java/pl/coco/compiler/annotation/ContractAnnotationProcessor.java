package pl.coco.compiler.annotation;

import java.util.List;
import java.util.stream.Collectors;

import javax.inject.Inject;
import javax.inject.Singleton;

import com.sun.tools.javac.tree.JCTree.JCAnnotation;
import com.sun.tools.javac.tree.JCTree.JCMethodDecl;
import com.sun.tools.javac.tree.JCTree.JCStatement;

@Singleton
public class ContractAnnotationProcessor {

    private final ContractAnnotationFactory annotationFactory;
    private final AnnotationTranslator annotationTranslator;
    private final ContractImportAdder contractImportAdder;

    @Inject
    public ContractAnnotationProcessor(ContractAnnotationFactory annotationFactory,
            AnnotationTranslator annotationTranslator, ContractImportAdder contractImportAdder) {
        this.annotationFactory = annotationFactory;
        this.annotationTranslator = annotationTranslator;
        this.contractImportAdder = contractImportAdder;
    }

    public void processMethod(AnnotationProcessorInput input) {
        JCMethodDecl method = input.getMethod();

        List<JCAnnotation> annotations = method.getModifiers().getAnnotations();

        List<ContractAnnotation> contractAnnotations = annotations.stream()
                .filter(annotationFactory::isContractAnnotation)
                .map(annotationFactory::getContractAnnotation)
                .collect(Collectors.toList());

        List<JCStatement> contractStatements = contractAnnotations.stream()
                .map(annotationTranslator::translate)
                .collect(Collectors.toList());

        if (!contractStatements.isEmpty()) {
            contractImportAdder.addImportToCompilationUnit(input.getCompilationUnit());
        }

        method.getBody().stats = method.getBody().getStatements()
                .prependList(com.sun.tools.javac.util.List.from(contractStatements));
    }
}
