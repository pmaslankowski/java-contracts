package pl.coas.compiler.instrumentation.model.pointcut;

import java.util.Objects;
import java.util.regex.Pattern;

public class WildcardString {

    private final String value;
    private final Pattern pattern;

    public WildcardString(String value) {
        this.value = value;
        this.pattern = makeRegex(value);
    }

    private Pattern makeRegex(String value) {
        String pattern = value.replace("*", ".*");
        return Pattern.compile(pattern);
    }

    public boolean matches(String str) {
        return pattern.matcher(str).matches();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        WildcardString that = (WildcardString) o;
        return Objects.equals(value, that.value) &&
                Objects.equals(pattern, that.pattern);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value, pattern);
    }

    @Override
    public String toString() {
        return "WildcardString{" +
                "value='" + value + '\'' +
                '}';
    }
}
