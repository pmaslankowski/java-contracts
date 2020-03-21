package pl.coco.api;

public class ContractFailedException extends RuntimeException {

    //TODO: add mapping between variables and actual values
    public ContractFailedException(String condition) {
        super(condition);
    }
}
