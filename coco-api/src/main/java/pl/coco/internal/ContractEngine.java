package pl.coco.internal;

import pl.coco.api.ContractFailedException;

public class ContractEngine {

    private static ThreadLocal<Boolean> isContractUnderEvaluation = new ThreadLocal<>();
    static {
        isContractUnderEvaluation.set(false);
    }

    // TODO: test isContractUnderEvalutaion flag
    public static void requires(ConditionSupplier precondition, String preconditionAsString) {
        evaluate(precondition, "Precondition \"{0}\" is not satisfied.", preconditionAsString);
    }

    public static void ensures(ConditionSupplier postcondition, String postconditionAsString) {
        evaluate(postcondition, "Postcondition \"{0}\" is not satisfied.", postconditionAsString);
    }

    private static void evaluate(ConditionSupplier condition, String contractMessage,
            String conditionAsString) {

        if (isContractUnderEvaluation.get()) {
            return;
        }

        boolean result = evaluateCondition(condition);

        if (!result) {
            throw new ContractFailedException(contractMessage, conditionAsString);
        }
    }

    private static boolean evaluateCondition(ConditionSupplier condition) {
        isContractUnderEvaluation.set(true);
        try {
            return condition.get();
        } catch (Exception e) {
            throw new ContractFailedException(
                    "An exception has been thrown during contract evaluation:", e);
        } finally {
            isContractUnderEvaluation.set(false);
        }
    }
}
