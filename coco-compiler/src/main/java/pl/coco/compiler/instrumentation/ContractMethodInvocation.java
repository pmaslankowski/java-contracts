package pl.coco.compiler.instrumentation;

import java.util.Optional;

import pl.coco.compiler.ContractType;

public class ContractMethodInvocation extends SimpleMethodInvocation {

    private final ContractType contractType;

    private ContractMethodInvocation(SimpleMethodInvocation invocation,
            ContractType contractType) {

        super(invocation.methodName, invocation.className, invocation.arguments, invocation.pos);
        this.contractType = contractType;
    }

    public static Optional<ContractMethodInvocation> of(SimpleMethodInvocation invocation) {
        return getContractMethod(invocation)
                .map(method -> new ContractMethodInvocation(invocation, method));

    }

    private static Optional<ContractType> getContractMethod(SimpleMethodInvocation invocation) {
        if (isContractMethodInvocation(invocation, ContractType.REQUIRES)) {
            return Optional.of(ContractType.REQUIRES);
        }
        if (isContractMethodInvocation(invocation, ContractType.ENSURES)) {
            return Optional.of(ContractType.ENSURES);
        }
        return Optional.empty();
    }

    private static boolean isContractMethodInvocation(SimpleMethodInvocation invocation,
            ContractType method) {

        return isMethodInvocation(invocation, method.getApiClassName(), method.getMethodName());
    }

    private static boolean isMethodInvocation(SimpleMethodInvocation invocation, String className,
            String methodName) {

        return isClassNameEqual(invocation, className) && isMethodNameEqual(invocation, methodName);
    }

    private static boolean isClassNameEqual(SimpleMethodInvocation invocation, String className) {
        return invocation.getClassName().contentEquals(className);
    }

    private static boolean isMethodNameEqual(SimpleMethodInvocation invocation, String methodName) {
        return invocation.getMethodName().contentEquals(methodName);
    }

    public ContractType getContractType() {
        return contractType;
    }
}
