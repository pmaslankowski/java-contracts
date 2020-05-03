package pl.coco.compiler.instrumentation.registry;

import static java.util.stream.Collectors.toList;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Singleton;

import org.apache.commons.collections4.ListUtils;

import com.google.common.collect.Lists;

import pl.coco.compiler.instrumentation.ContractMethod;
import pl.coco.compiler.instrumentation.invocation.ContractInvocation;

@Singleton
public class ContractsRegistry {

    private final Map<MethodKey, List<ContractInvocation>> allContracts = new HashMap<>();

    public void put(MethodKey key, ContractInvocation invocation) {
        allContracts.merge(key, Lists.newArrayList(invocation), ListUtils::union);
    }

    public List<ContractInvocation> getContracts(MethodKey key) {
        List<ContractInvocation> contracts = allContracts.get(key);
        return contracts == null ? Collections.emptyList() : contracts;
    }

    public List<ContractInvocation> getPreconditions(MethodKey key) {
        List<ContractInvocation> contracts = getContracts(key);
        return filterByMethod(contracts, ContractMethod.REQUIRES);
    }

    public List<ContractInvocation> getPostconditions(MethodKey key) {
        List<ContractInvocation> contracts = getContracts(key);
        return filterByMethod(contracts, ContractMethod.ENSURES);
    }

    private List<ContractInvocation> filterByMethod(List<ContractInvocation> contracts,
            ContractMethod method) {

        return contracts.stream()
                .filter(contract -> contract.getContractMethod() == method)
                .collect(toList());
    }
}
