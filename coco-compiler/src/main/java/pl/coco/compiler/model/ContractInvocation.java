package pl.coco.compiler.model;

import java.util.Optional;

import pl.coco.compiler.instrumentation.ContractMethod;
import pl.compiler.commons.model.SimpleMethodInvocation;

public class ContractInvocation extends SimpleMethodInvocation {

    private final ContractMethod contractMethod;

    private ContractInvocation(SimpleMethodInvocation invocation,
            ContractMethod contractMethod) {

        super(invocation.getMethodName(), invocation.getFullyQualifiedClassName(),
                invocation.getArguments(), invocation.getExpression());
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
