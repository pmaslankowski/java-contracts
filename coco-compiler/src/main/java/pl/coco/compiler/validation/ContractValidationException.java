package pl.coco.compiler.validation;

public class ContractValidationException extends RuntimeException {

    private final ContractError error;

    public ContractValidationException(ContractError error) {
        super("An error during contract compilation occured: " + error.getMessage());
        this.error = error;
    }
}
