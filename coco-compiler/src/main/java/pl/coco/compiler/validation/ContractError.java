package pl.coco.compiler.validation;

public enum ContractError {

    CONTRACT_CAN_OCCUR_IN_BLOCK_AT_THE_BEGINNING_OF_THE_METHOD(
            "This contract can occur in the contracts block at the beginning of the method only."),

    CONTRACT_BLOCK_CAN_CONTAIN_ONLY_CONTRACTS(
            "Contract block at the beginning of the method can contain only contracts " +
                    "and cannot be interspersed with any other statements."),

    RESULT_CAN_BE_PLACED_INSIDE_ENSURES_IN_NON_VOID_METHODS_ONLY(
            "Contract.result() can occur only inside postcondition specification" +
                    " (Contract.ensures(...)) in non-void methods");

    private final String message;

    ContractError(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
