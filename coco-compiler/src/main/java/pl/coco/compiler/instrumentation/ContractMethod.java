package pl.coco.compiler.instrumentation;

import java.util.Optional;

import pl.coco.compiler.instrumentation.invocation.SimpleMethodInvocation;

public enum ContractMethod {

    REQUIRES("requires", true, false, true),
    ENSURES("ensures", true, false, true),
    INVARIANT("invariant", true, false, true),
    RESULT("result", false, true, false),
    FOR_ALL("forAll", false, true, false),
    EXISTS("exists", false, true, false);

    private static final String API_CLASS_NAME = "pl.coco.api.code.Contract";
    private static final String INTERNAL_CLASS_NAME = "pl.coco.internal.ContractEngine";

    private final String methodName;
    private final boolean isContractSpecification;
    private final boolean canOccurInsideSpecificationOnly;
    private final boolean canOccurAtTheBeginningOfTheMethodOnly;

    ContractMethod(String methodName, boolean isContractSpecification,
            boolean canOccurInsideSpecificationOnly,
            boolean canOccurAtTheBeginningOfTheMethodOnly) {
        this.methodName = methodName;
        this.isContractSpecification = isContractSpecification;
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
