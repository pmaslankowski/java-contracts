package pl.coco.internal;

import pl.coco.api.ContractFailedException;

public class ContractEngine {

    public static void requires(boolean precondition, String preconditionAsString) {
        if (!precondition) {
            throw new ContractFailedException("Precondition \"{0}\" is not satisfied.",
                    preconditionAsString);
        }
    }

    public static void ensures(boolean postcondition, String postconditionAsString) {
        if (!postcondition) {
            throw new ContractFailedException("Postcondition \"{0}\" is not satisfied.",
                    postconditionAsString);
        }
    }
}
