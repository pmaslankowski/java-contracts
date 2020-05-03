package pl.coco.compiler.instrumentation.contract;

import static java.util.stream.Collectors.toList;

import java.util.stream.Collectors;

import javax.inject.Inject;

import com.sun.source.tree.MethodTree;
import com.sun.source.tree.VariableTree;
import com.sun.tools.javac.code.Flags;
import com.sun.tools.javac.code.Symbol.MethodSymbol;
import com.sun.tools.javac.code.Type;
import com.sun.tools.javac.code.Type.ClassType;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.tree.JCTree.JCBlock;
import com.sun.tools.javac.tree.JCTree.JCClassDecl;
import com.sun.tools.javac.tree.JCTree.JCMethodDecl;
import com.sun.tools.javac.tree.JCTree.JCStatement;
import com.sun.tools.javac.tree.TreeMaker;
import com.sun.tools.javac.util.List;
import com.sun.tools.javac.util.Name;
import com.sun.tools.javac.util.Names;

import pl.coco.compiler.instrumentation.ContractMethod;
import pl.coco.compiler.instrumentation.invocation.ContractInvocation;
import pl.coco.compiler.instrumentation.invocation.MethodInvocationBuilder;
import pl.coco.compiler.instrumentation.invocation.MethodInvocationDescription;
import pl.coco.compiler.instrumentation.registry.ContractsRegistry;
import pl.coco.compiler.instrumentation.registry.MethodKey;

public class PreconditionMethodGenerator {

    public static final String PRECONDITION_PREFIX = "coco$preconditions$";

    private final TreeMaker treeMaker;
    private final InternalInvocationBuilder internalInvocationBuilder;
    private final MethodInvocationBuilder methodInvocationBuilder;
    private final Names names;
    private final ContractsRegistry contractsRegistry;

    @Inject
    public PreconditionMethodGenerator(TreeMaker treeMaker,
            InternalInvocationBuilder internalInvocationBuilder,
            MethodInvocationBuilder methodInvocationBuilder, Names names,
            ContractsRegistry contractsRegistry) {
        this.treeMaker = treeMaker;
        this.internalInvocationBuilder = internalInvocationBuilder;
        this.methodInvocationBuilder = methodInvocationBuilder;
        this.names = names;
        this.contractsRegistry = contractsRegistry;
    }

    public JCMethodDecl buildPreconditionWrapper(JCClassDecl clazz, JCMethodDecl method) {
        MethodSymbol wrapperSymbol = getPreconditionWrapperSymbol(method);
        JCMethodDecl wrapper = treeMaker.MethodDef(wrapperSymbol, method.type, null);
        wrapper.body = getPreconditionMethodBody(wrapper, clazz, method);
        return wrapper;
    }

    // TODO: refactor
    private JCBlock getPreconditionMethodBody(JCMethodDecl wrapper, JCClassDecl clazz,
            JCMethodDecl method) {

        MethodKey methodKey = new MethodKey(clazz.sym.getQualifiedName(),
                method.getName(), method.type);
        java.util.List<ContractInvocation> contracts = contractsRegistry.getContracts(methodKey);
        java.util.List<ContractInvocation> preconditions = contracts
                .stream()
                .filter(contract -> contract.getContractMethod() == ContractMethod.REQUIRES)
                .collect(toList());

        if (preconditions.isEmpty()) {
            Type superType = ((ClassType) clazz.type).supertype_field;
            Name superClassName = superType.tsym.getQualifiedName();
            MethodInvocationDescription desc = new MethodInvocationDescription.Builder()
                    .withClassName(superClassName.toString())
                    .withArguments(List.from(getArguments(wrapper)))
                    .withPosition(method.pos)
                    .withMethodSymbol(wrapper.sym)
                    .build();
            JCTree.JCMethodInvocation methodInvocation = methodInvocationBuilder.build(desc);
            JCStatement superPreconditionWrapperCall = treeMaker.at(method.pos)
                    .Call(methodInvocation);
            return treeMaker.Block(0, List.of(superPreconditionWrapperCall));
        } else {
            java.util.List<JCStatement> statements =
                    convertPreconditionsToStatements(wrapper, preconditions);
            return treeMaker.Block(0, List.from(statements));
        }
    }

    // TODO: powtórzenie z klasy ContractProcessor
    private java.util.List<JCTree.JCExpression> getArguments(MethodTree method) {
        return method.getParameters().stream()
                .map(this::toIdentifier)
                .collect(toList());
    }

    private JCTree.JCIdent toIdentifier(VariableTree param) {
        JCTree.JCVariableDecl variableDec = (JCTree.JCVariableDecl) param;
        return treeMaker.Ident(variableDec.sym);
    }

    private java.util.List<JCStatement> convertPreconditionsToStatements(JCMethodDecl wrapper,
            java.util.List<ContractInvocation> preconditions) {

        // TODO: change that ugly null
        return preconditions.stream()
                .map(contract -> internalInvocationBuilder.build(contract, wrapper, null))
                .collect(Collectors.toList());
    }

    private MethodSymbol getPreconditionWrapperSymbol(JCMethodDecl originalMethod) {
        Name bridgeMethodName = getPreconditionWrapperName(originalMethod);

        long flags = getWrapperFlags(originalMethod);

        Type.MethodType type = new Type.MethodType(originalMethod.sym.type.getParameterTypes(),
                new Type.JCVoidType(), List.nil(), null);
        MethodSymbol result =
                new MethodSymbol(flags, bridgeMethodName, type, originalMethod.sym.owner);
        result.params = originalMethod.sym.params;

        return result;
    }

    private long getWrapperFlags(JCMethodDecl originalMethod) {
        long result = originalMethod.sym.flags();
        result &= ~Flags.PRIVATE;
        result &= ~Flags.PUBLIC;
        result |= Flags.PROTECTED;
        result |= Flags.SYNTHETIC;
        return result;
    }

    private Name getPreconditionWrapperName(JCMethodDecl originalMethod) {
        return names.fromString(PRECONDITION_PREFIX).append(originalMethod.getName());
    }
}
