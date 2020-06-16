package pl.coas.compiler.instrumentation.model.pointcut;

import java.util.List;
import java.util.Objects;

public class RegularMethodArguments implements MethodArguments {

    private final List<WildcardString> arguments;

    public RegularMethodArguments(List<WildcardString> arguments) {
        this.arguments = arguments;
    }

    @Override
    public boolean match(List<String> jpArguments) {
        if (jpArguments.size() != arguments.size()) {
            return false;
        }

        for (int i = 0; i < arguments.size(); i++) {
            WildcardString specifiedArg = arguments.get(i);
            String jpArg = jpArguments.get(i);
            if (!specifiedArg.matches(jpArg)) {
                return false;
            }
        }

        return true;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        RegularMethodArguments that = (RegularMethodArguments) o;
        return Objects.equals(arguments, that.arguments);
    }

    @Override
    public int hashCode() {
        return Objects.hash(arguments);
    }

    @Override
    public String toString() {
        return "RegularMethodArguments{" +
                "arguments=" + arguments +
                '}';
    }
}
