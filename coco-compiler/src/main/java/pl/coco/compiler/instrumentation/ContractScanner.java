package pl.coco.compiler.instrumentation;

import javax.inject.Inject;
import javax.inject.Singleton;

import com.sun.source.tree.BlockTree;
import com.sun.source.tree.StatementTree;

import pl.coco.compiler.instrumentation.invocation.ContractInvocation;
import pl.coco.compiler.instrumentation.registry.ContractsRegistry;
import pl.coco.compiler.instrumentation.registry.MethodKey;
import pl.coco.compiler.instrumentation.synthetic.MethodInput;
import pl.coco.compiler.util.ContractAstUtil;

@Singleton
public class ContractScanner {

    private final ContractsRegistry registry;

    @Inject
    public ContractScanner(ContractsRegistry registry) {
        this.registry = registry;
    }

    public void scan(MethodInput input) {
        MethodKey methodKey = MethodKey.of(input);

        BlockTree methodBody = input.getMethod().getBody();
        for (StatementTree statement : methodBody.getStatements()) {
            if (ContractAstUtil.isContractInvocation(statement)) {
                ContractInvocation contract = ContractAstUtil.getContractInvocation(statement);
                registry.put(methodKey, contract);
            }
        }
    }
}
