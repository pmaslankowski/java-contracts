package pl.coco.compiler.instrumentation.synthetic;

import java.util.stream.Collectors;

import javax.inject.Inject;
import javax.inject.Singleton;

import com.sun.tools.javac.code.Symbol.MethodSymbol;
import com.sun.tools.javac.code.Type;
import com.sun.tools.javac.code.Type.ClassType;
import com.sun.tools.javac.code.Type.MethodType;
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
import pl.coco.compiler.model.ContractInvocation;
import pl.coco.compiler.instrumentation.invocation.internal.precondition.RequiresInvocationBuilder;
import pl.coco.compiler.instrumentation.registry.ContractsRegistry;
import pl.coco.compiler.instrumentation.registry.MethodKey;
import pl.compiler.commons.invocation.MethodInvocationBuilder;
import pl.compiler.commons.invocation.MethodInvocationDescription;

@Singleton
public class PreconditionMethodGenerator extends AbstractMethodGenerator {

    private final MethodInvocationBuilder methodInvocationBuilder;
    private final RequiresInvocationBuilder requiresInvocationBuilder;
    private final ContractsRegistry contractsRegistry;
    private final ContractAnalyzer contractAnalyzer;
    private final SyntheticMethodNameGenerator nameGenerator;

    @Inject
    public PreconditionMethodGenerator(TreeMaker treeMaker,
            MethodInvocationBuilder methodInvocationBuilder, Names names,
            RequiresInvocationBuilder requiresInvocationBuilder,
            ContractsRegistry contractsRegistry, ContractAnalyzer contractAnalyzer,
            SyntheticMethodNameGenerator nameGenerator) {

        super(treeMaker, names);
        this.methodInvocationBuilder = methodInvocationBuilder;
        this.requiresInvocationBuilder = requiresInvocationBuilder;
        this.contractsRegistry = contractsRegistry;
        this.contractAnalyzer = contractAnalyzer;
        this.nameGenerator = nameGenerator;
    }

    @Override
    public JCMethodDecl generate(JCClassDecl clazz, JCMethodDecl method) {
        MethodSymbol wrapperSymbol = getPreconditionSymbol(method);
        JCMethodDecl wrapper = treeMaker.MethodDef(wrapperSymbol, wrapperSymbol.type, null);
        wrapper.body = generateMethodBody(wrapper, clazz, method);
        return wrapper;
    }

    private MethodSymbol getPreconditionSymbol(JCMethodDecl originalMethod) {
        Name preconditionMethodName = nameGenerator.getPreconditionsMethodName(originalMethod);
        long flags = getProtectedMethodFlags(originalMethod);
        MethodType type = getPreconditionMethodType(originalMethod);

        MethodSymbol result =
                new MethodSymbol(flags, preconditionMethodName, type, originalMethod.sym.owner);

        result.params = originalMethod.sym.params;

        return result;
    }

    private MethodType getPreconditionMethodType(JCMethodDecl originalMethod) {
        return new MethodType(originalMethod.sym.type.getParameterTypes(),
                new Type.JCVoidType(), List.nil(), null);
    }

    private JCBlock generateMethodBody(JCMethodDecl wrapper, JCClassDecl clazz,
            JCMethodDecl method) {

        MethodKey key = new MethodKey(clazz.sym.getQualifiedName(), method.getName(), method.type);
        java.util.List<ContractInvocation> preconditions = contractsRegistry.getPreconditions(key);

        if (preconditions.isEmpty() && contractAnalyzer.hasContractsUpInHierarchy(clazz, method)) {
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

        return preconditions.stream()
                .map(contract -> requiresInvocationBuilder.build(contract, wrapper))
                .collect(Collectors.toList());
    }
}
