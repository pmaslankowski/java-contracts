package pl.coco.compiler.instrumentation;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Singleton;

import org.apache.commons.collections4.ListUtils;

import com.google.common.collect.Lists;

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
}
