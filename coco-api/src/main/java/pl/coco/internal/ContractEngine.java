package pl.coco.internal;

import com.rits.cloning.Cloner;

import pl.coco.api.ContractFailedException;

public class ContractEngine {

    private static Cloner cloner = new Cloner();
    private static ThreadLocal<Boolean> isContractUnderEvaluation = new ThreadLocal<>();
    static {
        isContractUnderEvaluation.set(false);
    }

    public static void requires(ConditionSupplier precondition, String preconditionAsString) {
        evaluate(precondition, "Precondition \"{0}\" is not satisfied.", preconditionAsString);
    }

    public static void ensures(ConditionSupplier postcondition, String postconditionAsString) {
        evaluate(postcondition, "Postcondition \"{0}\" is not satisfied.", postconditionAsString);
    }

    public static void ensuresSelf(ConditionSupplier postcondition, String postconditionAsString) {
        evaluate(postcondition, "Postcondition \"{0}\" is not satisfied.", postconditionAsString);
    }

    public static void classInvariant(ConditionSupplier condition, String conditionAsString,
            boolean isBefore) {

        String when = isBefore ? "before" : "after";
        evaluate(condition, "Invariant \"{0}\" is not satisfied {1} the method call",
                conditionAsString, when);
    }

    public static void invariant(ConditionSupplier condition, String conditionAsString) {
        evaluate(condition, "Assertion \"{0}\" is not satisfied.", conditionAsString);
    }

    private static void evaluate(ConditionSupplier condition, String contractMessage,
            String... args) {

        if (isContractUnderEvaluation.get()) {
            return;
        }

        boolean result = evaluateCondition(condition);

        if (!result) {
            throw new ContractFailedException(contractMessage, (Object[]) args);
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

    public static <T> T deepClone(T obj) {
        return cloner.deepClone(obj);
    }
}
