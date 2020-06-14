package pl.coas.compiler.instrumentation.model.pointcut;

public enum MethodKind {

    STATIC, NON_STATIC, ALL;

    public static MethodKind of(String value) {
        switch (value) {
            case "static":
                return STATIC;
            case "non-static":
                return NON_STATIC;
            case "":
                return ALL;
            default:
                throw new IllegalArgumentException(
                        "Value: " + value + " is not a valid method kind");
        }
    }
}
