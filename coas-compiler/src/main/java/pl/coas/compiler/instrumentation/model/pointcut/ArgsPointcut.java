package pl.coas.compiler.instrumentation.model.pointcut;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class ArgsPointcut implements Pointcut {

    private final List<WildcardString> argumentTypes;

    public ArgsPointcut(List<String> argumentTypes) {
        this.argumentTypes = argumentTypes.stream()
                .map(WildcardString::new)
                .collect(Collectors.toList());
    }

    public List<WildcardString> getArgumentTypes() {
        return argumentTypes;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        ArgsPointcut that = (ArgsPointcut) o;
        return Objects.equals(argumentTypes, that.argumentTypes);
    }

    @Override
    public int hashCode() {
        return Objects.hash(argumentTypes);
    }

    @Override
    public String toString() {
        return "ArgsPointcut{" +
                "argumentTypes=" + argumentTypes +
                '}';
    }
}
