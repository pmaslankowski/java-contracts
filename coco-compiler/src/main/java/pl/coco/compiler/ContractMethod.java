package pl.coco.compiler;

import java.util.Optional;

import pl.coco.compiler.instrumentation.SimpleMethodInvocation;

public enum ContractMethod {

    REQUIRES("requires", ContractMethod.API_CLASS_NAME, ContractMethod.INTERNAL_CLASS_NAME),
    ENSURES("ensures", ContractMethod.API_CLASS_NAME, ContractMethod.INTERNAL_CLASS_NAME),
    RESULT("result", ContractMethod.API_CLASS_NAME, ContractMethod.INTERNAL_CLASS_NAME);

    private static final String API_CLASS_NAME = "pl.coco.api.Contract";
    private static final String INTERNAL_CLASS_NAME = "pl.coco.internal.ContractEngine";

    private String methodName;
    private String apiClassName;
    private String internalClassName;

    ContractMethod(String methodName, String apiClassName, String internalClassName) {
        this.methodName = methodName;
        this.apiClassName = apiClassName;
        this.internalClassName = internalClassName;
    }

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
        return Optional.empty();
    }

    private static boolean isContractMethodInvocation(SimpleMethodInvocation invocation,
            ContractMethod method) {

        return invocation.isMethodInvocationOf(method.getApiClassName(), method.getMethodName());
    }

    public String getMethodName() {
        return methodName;
    }

    public String getApiClassName() {
        return apiClassName;
    }

    public String getInternalClassName() {
        return internalClassName;
    }
}
