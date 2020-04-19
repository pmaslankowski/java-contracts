package pl.coco.api;

import java.text.MessageFormat;

public class ContractFailedException extends RuntimeException {

    // TODO: add mapping between variables and actual values
    public ContractFailedException(String formatString, Object... args) {
        super(MessageFormat.format(formatString, args));
    }
}
