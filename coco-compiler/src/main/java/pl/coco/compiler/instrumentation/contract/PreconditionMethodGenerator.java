package pl.coco.compiler.instrumentation.contract;

import java.util.stream.Collectors;

import javax.inject.Inject;
import javax.inject.Singleton;

import com.sun.tools.javac.code.Symbol.MethodSymbol;
import com.sun.tools.javac.code.Type;
import com.sun.tools.javac.code.Type.ClassType;
import com.sun.tools.javac.tree.JCTree.JCBlock;
import com.sun.tools.javac.tree.JCTree.JCClassDecl;
import com.sun.tools.javac.tree.JCTree.JCMethodDecl;
import com.sun.tools.javac.tree.JCTree.JCMethodInvocation;
import com.sun.tools.javac.tree.JCTree.JCStatement;
import com.sun.tools.javac.tree.TreeMaker;
import com.sun.tools.javac.util.List;
import com.sun.tools.javac.util.Name;
import com.sun.tools.javac.util.Names;

import pl.coco.compiler.instrumentation.ContractAnalyzer;
import pl.coco.compiler.instrumentation.invocation.ContractInvocation;
import pl.coco.compiler.instrumentation.invocation.MethodInvocationBuilder;
import pl.coco.compiler.instrumentation.invocation.MethodInvocationDescription;
import pl.coco.compiler.instrumentation.registry.ContractsRegistry;
import pl.coco.compiler.instrumentation.registry.MethodKey;

@Singleton
public class PreconditionMethodGenerator extends AbstractMethodGenerator {

    public static final String PRECONDITION_PREFIX = "coco$preconditions$";

    private final InternalInvocationBuilder internalInvocationBuilder;
    private final MethodInvocationBuilder methodInvocationBuilder;
    private final ContractsRegistry contractsRegistry;
    private final ContractAnalyzer contractAnalyzer;

    @Inject
    public PreconditionMethodGenerator(TreeMaker treeMaker,
            InternalInvocationBuilder internalInvocationBuilder,
            MethodInvocationBuilder methodInvocationBuilder, Names names,
            ContractsRegistry contractsRegistry, ContractAnalyzer contractAnalyzer) {

        super(treeMaker, names);
        this.internalInvocationBuilder = internalInvocationBuilder;
        this.methodInvocationBuilder = methodInvocationBuilder;
        this.contractsRegistry = contractsRegistry;
        this.contractAnalyzer = contractAnalyzer;
    }

    @Override
    public JCMethodDecl generate(JCClassDecl clazz, JCMethodDecl method) {
        MethodSymbol wrapperSymbol = getMethodSymbolMirroring(PRECONDITION_PREFIX, method);
        JCMethodDecl wrapper = treeMaker.MethodDef(wrapperSymbol, wrapperSymbol.type, null);
        wrapper.body = generateMethodBody(wrapper, clazz, method);
        return wrapper;
    }

    // TODO: trochę refactor
    private JCBlock generateMethodBody(JCMethodDecl wrapper, JCClassDecl clazz,
            JCMethodDecl method) {

        MethodKey key = new MethodKey(clazz.sym.getQualifiedName(), method.getName(), method.type);
        java.util.List<ContractInvocation> preconditions = contractsRegistry.getPreconditions(key);

        if (preconditions.isEmpty()
                && !contractAnalyzer.isFirstClassInInheritanceHierarchyWithContracts(clazz,
                        method)) {

            return generateSuperPreconditionMethodCall(wrapper, clazz, method);
        } else {
            return generateBodyFromThisClassPreconditions(wrapper, preconditions);
        }
    }

    private JCBlock generateSuperPreconditionMethodCall(JCMethodDecl wrapper, JCClassDecl clazz,
            JCMethodDecl method) {

        Type superType = ((ClassType) clazz.type).supertype_field;
        Name superClassName = superType.tsym.getQualifiedName();
        MethodInvocationDescription desc = new MethodInvocationDescription.Builder()
                .withClassName(superClassName.toString())
                .withArguments(List.from(getMethodArguments(wrapper)))
                .withPosition(method.pos)
                .withMethodSymbol(wrapper.sym)
                .build();
        JCMethodInvocation methodInvocation = methodInvocationBuilder.build(desc);

        JCStatement superPreconditionWrapperCall = treeMaker.at(method.pos)
                .Call(methodInvocation);
        return treeMaker.Block(0, List.of(superPreconditionWrapperCall));
    }

    private JCBlock generateBodyFromThisClassPreconditions(JCMethodDecl wrapper,
            java.util.List<ContractInvocation> preconditions) {

        java.util.List<JCStatement> statements =
                convertPreconditionsToStatements(wrapper, preconditions);
        return treeMaker.Block(0, List.from(statements));
    }

    private java.util.List<JCStatement> convertPreconditionsToStatements(JCMethodDecl wrapper,
            java.util.List<ContractInvocation> preconditions) {

        // TODO: get id of that ugly null
        return preconditions.stream()
                .map(contract -> internalInvocationBuilder.build(contract, wrapper, null))
                .collect(Collectors.toList());
    }
}
