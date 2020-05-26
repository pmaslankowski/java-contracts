package pl.coco.compiler.model;

import java.util.Optional;

import pl.coco.compiler.instrumentation.ContractMethod;

public class ContractInvocation extends SimpleMethodInvocation {

    private final ContractMethod contractMethod;

    private ContractInvocation(SimpleMethodInvocation invocation,
            ContractMethod contractMethod) {

        super(invocation.methodName, invocation.fullyQualifiedClassName, invocation.arguments, invocation.expression);
        this.contractMethod = contractMethod;
    }

    public static Optional<ContractInvocation> of(SimpleMethodInvocation invocation) {
        return ContractMethod.of(invocation)
                .map(method -> new ContractInvocation(invocation, method));

    }

    public ContractMethod getContractMethod() {
        return contractMethod;
    }
}
