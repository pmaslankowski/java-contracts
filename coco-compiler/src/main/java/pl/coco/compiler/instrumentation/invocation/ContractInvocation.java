package pl.coco.compiler.instrumentation.invocation;

import java.util.Optional;

import pl.coco.compiler.instrumentation.ContractMethod;

public class ContractInvocation extends SimpleMethodInvocation {

    // TODO: get rid of this type marker - change it to other subclass
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

    // TODO: przenieść wszystkie te metody do enuma ContractMethod
    public boolean canOccurAtTheBeginningOfTheMethodOnly() {
        return contractMethod == ContractMethod.ENSURES
                || contractMethod == ContractMethod.REQUIRES
                || contractMethod == ContractMethod.INVARIANT;
    }

    public boolean isContractSpecification() {
        return contractMethod == ContractMethod.ENSURES || contractMethod == ContractMethod.REQUIRES
                || contractMethod == ContractMethod.INVARIANT;
    }

    public boolean canOccurInsideContractSpecificationOnly() {
        return contractMethod == ContractMethod.FOR_ALL || contractMethod == ContractMethod.EXISTS;
    }
}
