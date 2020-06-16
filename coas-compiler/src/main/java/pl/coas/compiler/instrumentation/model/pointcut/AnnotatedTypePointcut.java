package pl.coas.compiler.instrumentation.model.pointcut;

import java.util.Objects;

import com.sun.tools.javac.tree.JCTree.JCAnnotation;
import com.sun.tools.javac.util.List;

import pl.coas.compiler.instrumentation.model.JoinPoint;

public class AnnotatedTypePointcut implements Pointcut {

    private final WildcardString annotation;

    public AnnotatedTypePointcut(String annotation) {
        this.annotation = new WildcardString(annotation);
    }

    @Override
    public boolean matches(JoinPoint joinPoint) {
        List<JCAnnotation> jpAnnotations = joinPoint.getClazz().getModifiers().getAnnotations();

        return jpAnnotations.stream()
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
        AnnotatedTypePointcut that = (AnnotatedTypePointcut) o;
        return Objects.equals(annotation, that.annotation);
    }

    @Override
    public int hashCode() {
        return Objects.hash(annotation);
    }

    @Override
    public String toString() {
        return "AnnotatedTypePointcut{" +
                "annotation=" + annotation +
                '}';
    }
}
