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

        Type objectType = typeRegistry.getClassType(JAVA_LANG_OBJECT);

        Name methodName = method.getName();
        Type methodType = method.type;
        ClassType classType = (ClassType) clazz.type;

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

    // TODO: refactor
    public boolean isFirstClassInInheritanceHierarchyWithContracts(JCClassDecl clazz,
            JCMethodDecl method) {

        Type objectType = typeRegistry.getClassType(JAVA_LANG_OBJECT);

        Name methodName = method.getName();
        Type methodType = method.type;
        ClassType classType = (ClassType) clazz.type;

        Name thisClassName = classType.tsym.getQualifiedName();
        MethodKey thisMethodKey = new MethodKey(thisClassName, methodName, methodType);
        List<ContractInvocation> thisClassContracts = contractsRegistry.getContracts(thisMethodKey);
        if (thisClassContracts.isEmpty()) {
            return false;
        }

        classType = (ClassType) classType.supertype_field;

        while (!classType.equals(objectType)) {
            Name className = classType.tsym.getQualifiedName();
            MethodKey methodKey = new MethodKey(className, methodName, methodType);

            List<ContractInvocation> contracts = contractsRegistry.getContracts(methodKey);
            if (!contracts.isEmpty()) {
                return false;
            }

            classType = (ClassType) classType.supertype_field;
        }

        return true;
    }
}
