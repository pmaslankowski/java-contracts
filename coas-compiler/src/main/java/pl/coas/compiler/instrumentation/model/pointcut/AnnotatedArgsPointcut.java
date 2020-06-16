package pl.coas.compiler.instrumentation.model.pointcut;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import com.sun.tools.javac.tree.JCTree.JCAnnotation;
import com.sun.tools.javac.tree.JCTree.JCVariableDecl;

import pl.coas.compiler.instrumentation.model.JoinPoint;

public class AnnotatedArgsPointcut implements Pointcut {

    private final List<WildcardString> annotations;

    public AnnotatedArgsPointcut(List<String> annotations) {
        this.annotations = annotations.stream()
                .map(WildcardString::new)
                .collect(Collectors.toList());
    }

    @Override
    public boolean matches(JoinPoint joinPoint) {
        List<JCVariableDecl> parameters = joinPoint.getMethod().getParameters();

        if (annotations.size() != parameters.size()) {
            return false;
        }

        for (int i = 0; i < annotations.size(); i++) {
            JCVariableDecl parameter = parameters.get(i);
            WildcardString expectedAnnotation = annotations.get(i);
            if (!hasParameterAnnotation(parameter, expectedAnnotation)) {
                return false;
            }
        }

        return true;
    }

    private boolean hasParameterAnnotation(JCVariableDecl parameter, WildcardString annotation) {
        List<JCAnnotation> parameterAnnotations = parameter.getModifiers().getAnnotations();

        return parameterAnnotations.stream()
                .anyMatch(jpAnn -> annotation.matches(getAnnotationTypeName(jpAnn)));
    }

    private String getAnnotationTypeName(JCAnnotation annotation) {
        return annotation.type.tsym.getQualifiedName().toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        AnnotatedArgsPointcut that = (AnnotatedArgsPointcut) o;
        return Objects.equals(annotations, that.annotations);
    }

    @Override
    public int hashCode() {
        return Objects.hash(annotations);
    }

    @Override
    public String toString() {
        return "AnnotatedArgsPointcut{" +
                "annotations=" + annotations +
                '}';
    }
}
