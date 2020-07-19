package pl.coco.compiler.instrumentation;

import java.util.Optional;

import pl.compiler.commons.model.SimpleMethodInvocation;

public enum ContractMethod {

    REQUIRES("requires", true, false, false, true),
    ENSURES("ensures", true, true, false, true),
    ENSURES_SELF("ensuresSelf", true, true, false, true),
    CLASS_INVARIANT("classInvariant", true, false, false, true),
    INVARIANT("invariant", true, false, false, false),
    RESULT("result", false, false, true, false),
    OLD("old", false, false, true, false),
    FOR_ALL("forAll", false, false, true, false),
    EXISTS("exists", false, false, true, false);

    private static final String API_CLASS_NAME = "pl.coco.api.code.Contract";
    private static final String INTERNAL_CLASS_NAME = "pl.coco.internal.ContractEngine";

    private final String methodName;
    private final boolean isContractSpecification;
    private final boolean isPostcondition;
    private final boolean canOccurInsideSpecificationOnly;
    private final boolean canOccurAtTheBeginningOfTheMethodOnly;

    ContractMethod(String methodName, boolean isContractSpecification,
                   boolean isPostcondition, boolean canOccurInsideSpecificationOnly,
                   boolean canOccurAtTheBeginningOfTheMethodOnly) {
        this.methodName = methodName;
        this.isContractSpecification = isContractSpecification;
        this.isPostcondition = isPostcondition;
        this.canOccurInsideSpecificationOnly = canOccurInsideSpecificationOnly;
        this.canOccurAtTheBeginningOfTheMethodOnly = canOccurAtTheBeginningOfTheMethodOnly;
    }

    public static Optional<ContractMethod> of(SimpleMethodInvocation invocation) {
        for (ContractMethod method : ContractMethod.values()) {
            if (isContractMethodInvocation(invocation, method)) {
                return Optional.of(method);
            }
        }
        return Optional.empty();
    }

    private static boolean isContractMethodInvocation(SimpleMethodInvocation invocation,
            ContractMethod method) {

        return invocation.isMethodInvocationOf(API_CLASS_NAME, method.getMethodName());
    }

    public String getMethodName() {
        return methodName;
    }

    public String getInternalClassName() {
        return INTERNAL_CLASS_NAME;
    }

    public boolean isPostcondition() { return isPostcondition; }

    public boolean isContractSpecification() {
        return isContractSpecification;
    }

    public boolean canOccurAtTheBeginningOfTheMethodOnly() {
        return canOccurAtTheBeginningOfTheMethodOnly;
    }

    public boolean canOccurInsideSpecificationOnly() {
        return canOccurInsideSpecificationOnly;
    }
}
