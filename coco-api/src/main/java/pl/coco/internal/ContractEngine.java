package pl.coco.internal;

import java.text.MessageFormat;

import pl.coco.api.ContractFailedException;

public class ContractEngine {

    public static void requires(boolean precondition, String preconditionAsString) {
        if (!precondition) {
            throw new ContractFailedException(MessageFormat.format(
                    "Precondition {0} is not satisfied.", preconditionAsString));
        }
    }
}
