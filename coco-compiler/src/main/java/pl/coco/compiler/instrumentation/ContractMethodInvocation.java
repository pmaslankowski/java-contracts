package pl.coco.compiler.instrumentation;

import java.util.List;
import java.util.Optional;

import javax.lang.model.element.Name;

import com.sun.source.tree.ExpressionTree;

public class ContractMethodInvocation extends SimpleMethodInvocation {

    private ContractMethodInvocation(Name methodName, Name className,
            List<? extends ExpressionTree> arguments, int pos) {

        super(methodName, className, arguments, pos);
    }

    public static Optional<ContractMethodInvocation> of(SimpleMethodInvocation invocation) {

        if (isContractMethodInvocation(invocation)) {
            return Optional.of(new ContractMethodInvocation(invocation.getMethodName(),
                    invocation.getClassName(), invocation.getArguments(),
                    invocation.getPosition()));
        } else {
            return Optional.empty();
        }
    }

    private static boolean isContractMethodInvocation(SimpleMethodInvocation invocation) {
        boolean doesMethodNameMatch =
                isEnsuresInvocation(invocation) || isRequiresInvocation(invocation);
        return isInvocationOfContractClass(invocation) && doesMethodNameMatch;
    }

    private static boolean isRequiresInvocation(SimpleMethodInvocation invocation) {
        return isMethodNameEqual(invocation, "requires");
    }

    private static boolean isEnsuresInvocation(SimpleMethodInvocation invocation) {
        return isMethodNameEqual(invocation, "ensures");
    }

    private static boolean isMethodNameEqual(SimpleMethodInvocation invocation, String name) {
        return invocation.getMethodName().contentEquals(name);
    }

    private static boolean isInvocationOfContractClass(SimpleMethodInvocation invocation) {
        return invocation.getClassName().contentEquals("Contract");
    }
}
