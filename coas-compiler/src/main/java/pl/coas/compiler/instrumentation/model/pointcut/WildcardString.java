package pl.coas.compiler.instrumentation.model.pointcut;

import java.util.Objects;

public class WildcardString {

    private final String value;

    public WildcardString(String value) {
        this.value = value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        WildcardString that = (WildcardString) o;
        return Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return "WildcardString{" +
                "value='" + value + '\'' +
                '}';
    }
}
