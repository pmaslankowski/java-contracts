package pl.coco.compiler.validation;

public enum ContractError {

    CONTRACT_CAN_OCCUR_IN_BLOCK_AT_THE_BEGINNING_OF_THE_METHOD(
            "This contract can occur in the contracts block at the beginning of the method only."),

    CONTRACT_BLOCK_CAN_CONTAIN_ONLY_CONTRACTS(
            "Contract block at the beginning of the method can contain only contracts " +
                    "and cannot be interspersed with any other statements."),

    RESULT_CAN_BE_PLACED_INSIDE_ENSURES_IN_NON_VOID_METHODS_ONLY(
            "Contract.result() can occur only inside postcondition specification" +
                    " (Contract.ensures(...)) in non-void methods"),

    RESULT_TYPE_MUST_MATCH_METHOD_TYPE(
            "Contract.result(...) type must match containing method return type"),

    INVARIANT_CAN_OCCUR_IN_INVARIANT_METHODS_ONLY(
            "Contract.invariant(...) can occur in invariant methods only"),

    MULTIPLE_INVARIANT_METHODS_IN_THE_SAME_CLASS(
            "There can be at most one invariant method in a class"),

    BAD_INVARIANT_METHOD_TYPE(
            "Invariant type must have the following signature: void()"),

    CONTRACT_STATEMENT_OUTSIDE_OF_CONTRACTS(
            "This statement can be used inside other contract statements only");

    private final String message;

    ContractError(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
