package pl.coco.compiler.instrumentation;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import com.sun.tools.javac.code.Type;
import com.sun.tools.javac.code.Type.ClassType;
import com.sun.tools.javac.tree.JCTree.JCClassDecl;
import com.sun.tools.javac.tree.JCTree.JCMethodDecl;
import com.sun.tools.javac.util.Name;

import pl.coco.compiler.instrumentation.invocation.ContractInvocation;
import pl.coco.compiler.instrumentation.registry.ContractsRegistry;
import pl.coco.compiler.instrumentation.registry.MethodKey;
import pl.coco.compiler.util.TypeRegistry;

@Singleton
public class ContractAnalyzer {

    private static final String JAVA_LANG_OBJECT = "java.lang.Object";

    private final ContractsRegistry contractsRegistry;
    private final TypeRegistry typeRegistry;

    @Inject
    public ContractAnalyzer(ContractsRegistry contractsRegistry, TypeRegistry typeRegistry) {
        this.contractsRegistry = contractsRegistry;
        this.typeRegistry = typeRegistry;
    }

    public boolean hasContracts(JCClassDecl clazz, JCMethodDecl method) {
        return hasContractsInHierarchy((ClassType) clazz.type, method.getName(), method.type);
    }

    private boolean hasContractsInHierarchy(ClassType classType, Name methodName, Type methodType) {

        Type objectType = typeRegistry.getClassType(JAVA_LANG_OBJECT);

        while (!classType.equals(objectType)) {
            Name className = classType.tsym.getQualifiedName();
            MethodKey methodKey = new MethodKey(className, methodName, methodType);

            List<ContractInvocation> contracts = contractsRegistry.getContracts(methodKey);
            if (!contracts.isEmpty()) {
                return true;
            }

            classType = (ClassType) classType.supertype_field;
        }

        return false;
    }

    public boolean hasContractsUpInHierarchy(JCClassDecl clazz, JCMethodDecl method) {
        ClassType superType = (ClassType) ((ClassType) clazz.type).supertype_field;
        return hasContractsInHierarchy(superType, method.getName(), method.type);
    }
}
