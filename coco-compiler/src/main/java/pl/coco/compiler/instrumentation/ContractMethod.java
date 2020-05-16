package pl.coco.compiler.instrumentation;

import java.util.Optional;

import pl.coco.compiler.instrumentation.invocation.SimpleMethodInvocation;

public enum ContractMethod {

    REQUIRES("requires"), ENSURES("ensures"), RESULT("result"), INVARIANT("invariant"), FORALL(
            "forAll"), EXISTS("exists");

    private static final String API_CLASS_NAME = "pl.coco.api.Contract";
    private static final String INTERNAL_CLASS_NAME = "pl.coco.internal.ContractEngine";

    private String methodName;

    ContractMethod(String methodName) {
        this.methodName = methodName;
    }

    // TODO: przerobić, żeby dodawanie kolejnych wartości nie wymuszało zmiany tej metody
    public static Optional<ContractMethod> of(SimpleMethodInvocation invocation) {
        if (isContractMethodInvocation(invocation, REQUIRES)) {
            return Optional.of(REQUIRES);
        }
        if (isContractMethodInvocation(invocation, ENSURES)) {
            return Optional.of(ENSURES);
        }
        if (isContractMethodInvocation(invocation, RESULT)) {
            return Optional.of(RESULT);
        }
        if (isContractMethodInvocation(invocation, INVARIANT)) {
            return Optional.of(INVARIANT);
        }
        if (isContractMethodInvocation(invocation, FORALL)) {
            return Optional.of(FORALL);
        }
        if (isContractMethodInvocation(invocation, EXISTS)) {
            return Optional.of(EXISTS);
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
