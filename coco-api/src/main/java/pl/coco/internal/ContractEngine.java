package pl.coco.internal;

import pl.coco.api.ContractFailedException;

public class ContractEngine {

    private static ThreadLocal<Boolean> isContractUnderEvaluation = new ThreadLocal<>();
    static {
        isContractUnderEvaluation.set(false);
    }

    // TODO: add exception handling from contract evaluation
    // TODO: test isContractUnderEvalutaion flag
    public static void requires(boolean precondition, String preconditionAsString) {
        evaluate(precondition, "Precondition \"{0}\" is not satisfied.", preconditionAsString);
    }

    public static void ensures(boolean postcondition, String postconditionAsString) {
        evaluate(postcondition, "Postcondition \"{0}\" is not satisfied.", postconditionAsString);
    }

    private static void evaluate(boolean condition, String contractMessage,
            String conditionAsString) {

        if (isContractUnderEvaluation.get()) {
            return;
        }

        isContractUnderEvaluation.set(true);
        if (!condition) {
            isContractUnderEvaluation.set(false);
            throw new ContractFailedException(contractMessage, conditionAsString);
        }
        isContractUnderEvaluation.set(false);
    }
}
