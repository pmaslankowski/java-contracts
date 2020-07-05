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
        if (value.startsWith("r/")) {
            return getRegexPattern(value.substring(2));
        } else {
            return getWildcardPattern(value);
        }
    }

    private Pattern getRegexPattern(String value) {
        return Pattern.compile(value);
    }

    private Pattern getWildcardPattern(String value) {
        String quotedValue = Pattern.quote(value);
        String regex = quotedValue.replace("*", "\\E.*\\Q");
        return Pattern.compile(regex);
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
