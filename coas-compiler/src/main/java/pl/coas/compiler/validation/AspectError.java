package pl.coas.compiler.validation;

public enum AspectError {

    ASPECT_CAN_OCCUR_IN_BLOCK_AT_THE_BEGINNING_OF_THE_METHOD(
            "This statement can occur in the aspect block at the beginning of the method only."),
    ASPECT_BLOCK_CAN_CONTAIN_ASPECTS_ONLY("Aspect block at the beginning of the method can contain only aspects " +
            "and cannot be interspersed with any other statements."),
    INVALID_ADVICE_SINGATURE("Advice method must return Object and take JoinPoint.");

    private final String message;

    AspectError(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
