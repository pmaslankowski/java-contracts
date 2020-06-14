package pl.coas.compiler.instrumentation.model.pointcut;

public enum MethodModifier {

    PUBLIC, PRIVATE, PROTECTED, PACKAGE_PRIVATE, ALL;

    public static MethodModifier of(String value) {
        switch (value) {
            case "public":
                return PUBLIC;
            case "private":
                return PRIVATE;
            case "protected":
                return PROTECTED;
            case "package-private":
                return PACKAGE_PRIVATE;
            case "":
                return ALL;
            default:
                throw new IllegalArgumentException(
                        "Value " + value + " is not a valid method modifier");
        }
    }
}
