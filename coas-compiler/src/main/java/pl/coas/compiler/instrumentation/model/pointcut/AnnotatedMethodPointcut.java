package pl.coas.compiler.instrumentation.model.pointcut;

import java.util.Objects;

import com.sun.tools.javac.tree.JCTree.JCAnnotation;
import com.sun.tools.javac.util.List;

import pl.coas.compiler.instrumentation.model.JoinPoint;

public class AnnotatedMethodPointcut implements Pointcut {

    private final WildcardString annotation;

    public AnnotatedMethodPointcut(String annotation) {
        this.annotation = new WildcardString(annotation);
    }

    @Override
    public boolean matches(JoinPoint joinPoint) {
        List<JCAnnotation> jpAnnotations = joinPoint.getMethod().getModifiers().getAnnotations();

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
        AnnotatedMethodPointcut that = (AnnotatedMethodPointcut) o;
        return Objects.equals(annotation, that.annotation);
    }

    @Override
    public int hashCode() {
        return Objects.hash(annotation);
    }

    @Override
    public String toString() {
        return "AnnotatedMethodPointcut{" +
                "annotation=" + annotation +
                '}';
    }
}
