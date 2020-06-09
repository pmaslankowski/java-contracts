package pl.coas.compiler.instrumentation.model.pointcut;

import java.util.Objects;

public class AnnotatedMethodPointcut implements Pointcut {

    private final WildcardString annotation;

    public AnnotatedMethodPointcut(String annotation) {
        this.annotation = new WildcardString(annotation);
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
