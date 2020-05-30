package pl.coco.compiler.annotation;

import java.util.Optional;

import javax.inject.Singleton;

import com.sun.tools.javac.tree.JCTree.JCAnnotation;
import com.sun.tools.javac.tree.JCTree.JCLiteral;

@Singleton
public class ContractAnnotationFactory {

    public boolean isContractAnnotation(JCAnnotation annotation) {
        return doGetContractAnnotation(annotation).isPresent();
    }

    public ContractAnnotation getContractAnnotation(JCAnnotation annotation) {
        return doGetContractAnnotation(annotation).orElseThrow(
                () -> new IllegalArgumentException(
                        "Annotation: " + annotation + " is not a contract annotation."));
    }

    private Optional<ContractAnnotation> doGetContractAnnotation(JCAnnotation annotation) {

        for (ContractAnnotationType targetType : ContractAnnotationType.values()) {
            if (targetType.getName().equals(annotation.getAnnotationType().toString())) {
                ContractAnnotation contractAnnotation = new ContractAnnotation(
                        getExpression(annotation), getExprPosition(annotation), targetType);
                return Optional.of(contractAnnotation);
            }
        }

        return Optional.empty();
    }

    private String getExpression(JCAnnotation annotation) {
        JCLiteral expression = (JCLiteral) annotation.getArguments().head;
        return (String) expression.getValue();
    }

    private int getExprPosition(JCAnnotation annotation) {
        JCLiteral expression = (JCLiteral) annotation.getArguments().head;
        return expression.pos;
    }
}
