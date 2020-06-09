package pl.coas.compiler.instrumentation.model.pointcut;

import java.util.Objects;

public class AnnotatedTypePointcut implements Pointcut {

    private final WildcardString annotation;

    public AnnotatedTypePointcut(String annotation) {
        this.annotation = new WildcardString(annotation);
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
