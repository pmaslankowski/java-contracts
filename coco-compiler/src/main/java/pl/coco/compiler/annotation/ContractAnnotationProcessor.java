package pl.coco.compiler.annotation;

import java.util.List;
import java.util.stream.Collectors;

import javax.inject.Inject;
import javax.inject.Singleton;

import com.sun.tools.javac.tree.JCTree.JCAnnotation;
import com.sun.tools.javac.tree.JCTree.JCClassDecl;
import com.sun.tools.javac.tree.JCTree.JCMethodDecl;
import com.sun.tools.javac.tree.JCTree.JCStatement;

@Singleton
public class ContractAnnotationProcessor {

    private final ContractAnnotationFactory annotationFactory;
    private final AnnotationTranslator annotationTranslator;
    private final InvariantTranslator invariantTranslator;
    private final ContractImportAdder contractImportAdder;

    @Inject
    public ContractAnnotationProcessor(ContractAnnotationFactory annotationFactory,
            AnnotationTranslator annotationTranslator, InvariantTranslator invariantTranslator,
            ContractImportAdder contractImportAdder) {
        this.annotationFactory = annotationFactory;
        this.annotationTranslator = annotationTranslator;
        this.invariantTranslator = invariantTranslator;
        this.contractImportAdder = contractImportAdder;
    }

    public void processClass(AnnotationProcessorClassInput input) {
        JCClassDecl clazz = input.getClazz();

        List<JCAnnotation> annotations = clazz.getModifiers().getAnnotations();
        List<ContractAnnotation> invariantAnnotations = getContractAnnotations(annotations);

        if (!invariantAnnotations.isEmpty()) {
            contractImportAdder.addImportToCompilationUnit(input.getCompilationUnit());
            JCMethodDecl invariantMethod = invariantTranslator.translateToInvariantMethod(
                    invariantAnnotations);
            clazz.defs = clazz.defs.append(invariantMethod);
        }
    }

    private List<ContractAnnotation> getContractAnnotations(List<JCAnnotation> annotations) {
        return annotations.stream()
                .filter(annotationFactory::isContractAnnotation)
                .map(annotationFactory::getContractAnnotation)
                .collect(Collectors.toList());
    }

    public void processMethod(AnnotationProcessorMethodInput input) {
        JCMethodDecl method = input.getMethod();

        List<JCAnnotation> annotations = method.getModifiers().getAnnotations();
        List<ContractAnnotation> contractAnnotations = getContractAnnotations(annotations);
        List<JCStatement> contractStatements = getTranslatedContractStatements(contractAnnotations);

        if (!contractStatements.isEmpty()) {
            contractImportAdder.addImportToCompilationUnit(input.getCompilationUnit());
            method.getBody().stats = method.getBody().getStatements()
                    .prependList(com.sun.tools.javac.util.List.from(contractStatements));
        }
    }

    private List<JCStatement> getTranslatedContractStatements(
            List<ContractAnnotation> contractAnnotations) {

        return contractAnnotations.stream()
                .map(annotationTranslator::translate)
                .collect(Collectors.toList());
    }
}
