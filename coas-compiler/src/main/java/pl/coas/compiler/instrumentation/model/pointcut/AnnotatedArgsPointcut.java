package pl.coas.compiler.instrumentation.model.pointcut;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class AnnotatedArgsPointcut implements Pointcut {

    private final List<WildcardString> annotations;

    public AnnotatedArgsPointcut(List<String> annotations) {
        this.annotations = annotations.stream()
                .map(WildcardString::new)
                .collect(Collectors.toList());
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
