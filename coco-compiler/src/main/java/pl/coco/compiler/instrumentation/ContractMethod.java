package pl.coco.compiler.instrumentation;

import java.util.Optional;

import pl.coco.compiler.instrumentation.invocation.SimpleMethodInvocation;

public enum ContractMethod {

    REQUIRES("requires"), ENSURES("ensures"), RESULT("result"), INVARIANT("invariant"), FOR_ALL(
            "forAll"), EXISTS("exists");

    private static final String API_CLASS_NAME = "pl.coco.api.Contract";
    private static final String INTERNAL_CLASS_NAME = "pl.coco.internal.ContractEngine";

    private String methodName;

    ContractMethod(String methodName) {
        this.methodName = methodName;
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
}
