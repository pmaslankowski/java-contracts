package pl.coco.internal;

import java.text.MessageFormat;

import pl.coco.api.ContractFailedException;

public class ContractEngine {

    //TODO: move MessageFormat.format to exception constructor
    public static void requires(boolean precondition, String preconditionAsString) {
        if (!precondition) {
            throw new ContractFailedException(MessageFormat.format(
                    "Precondition \"{0}\" is not satisfied.", preconditionAsString));
        }
    }

    public static void ensures(boolean postcondition, String postconditionAsString) {
        if (!postcondition) {
            throw new ContractFailedException(MessageFormat.format(
                    "Postcondition \"{0}\" is not satisfied.", postconditionAsString));
        }
    }
}
