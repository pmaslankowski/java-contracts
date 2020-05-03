package pl.coco.compiler.instrumentation.contract;

import java.util.stream.Collectors;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.lang.model.type.TypeKind;

import com.sun.tools.javac.code.Flags;
import com.sun.tools.javac.code.Symbol;
import com.sun.tools.javac.code.Symbol.MethodSymbol;
import com.sun.tools.javac.code.Symbol.VarSymbol;
import com.sun.tools.javac.code.Type;
import com.sun.tools.javac.code.Type.ClassType;
import com.sun.tools.javac.code.Type.JCVoidType;
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
import pl.coco.compiler.instrumentation.invocation.ContractInvocation;
import pl.coco.compiler.instrumentation.invocation.MethodInvocationBuilder;
import pl.coco.compiler.instrumentation.invocation.MethodInvocationDescription;
import pl.coco.compiler.instrumentation.registry.ContractsRegistry;
import pl.coco.compiler.instrumentation.registry.MethodKey;

@Singleton
public class PostconditionMethodGenerator extends AbstractMethodGenerator {

    private static final String POSTCONDITION_PREFIX = "coco$postconditions$";
    private static final JCVoidType VOID_TYPE = new JCVoidType();

    private final InternalInvocationBuilder internalInvocationBuilder;
    private final MethodInvocationBuilder methodInvocationBuilder;
    private final ContractsRegistry contractsRegistry;
    private final ContractAnalyzer contractAnalyzer;

    @Inject
    public PostconditionMethodGenerator(TreeMaker treeMaker,
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
        MethodSymbol wrapperSymbol = getMethodSymbol(method);
        JCMethodDecl wrapper = treeMaker.MethodDef(wrapperSymbol, wrapperSymbol.type, null);
        wrapper.body = generateMethodBody(wrapper, clazz, method);
        return wrapper;
    }

    private MethodSymbol getMethodSymbol(JCMethodDecl originalMethod) {
        Name bridgeMethodName = getMethodName(originalMethod);
        long flags = getMethodFlags(originalMethod);
        MethodType type = getMethodType(originalMethod);

        MethodSymbol result =
                new MethodSymbol(flags, bridgeMethodName, type, originalMethod.sym.owner);

        result.params = originalMethod.sym.params;
        if (isVoid(originalMethod)) {
            return result;
        } else {
            VarSymbol resultSymbol = new VarSymbol(0, names.fromString("result"),
                    originalMethod.getReturnType().type, result);

            if (originalMethod.sym.isStatic()) {
                resultSymbol.adr = result.params.length();
            } else {
                resultSymbol.adr = result.params.length() + 1;
            }

            result.params = result.params.append(resultSymbol);
        }

        return result;
    }

    private Name getMethodName(JCMethodDecl originalMethod) {
        return names.fromString(POSTCONDITION_PREFIX).append(originalMethod.getName());
    }

    private long getMethodFlags(JCMethodDecl originalMethod) {
        long result = originalMethod.sym.flags();
        result &= ~Flags.PRIVATE;
        result &= ~Flags.PUBLIC;
        result |= Flags.PROTECTED;
        result |= Flags.SYNTHETIC;
        return result;
    }

    private MethodType getMethodType(JCMethodDecl originalMethod) {
        List<Type> originalParamTypes = originalMethod.sym.type.getParameterTypes();
        if (!isVoid(originalMethod)) {
            Type returnType = originalMethod.sym.getReturnType();
            List<Type> paramTypes = originalParamTypes.append(returnType);
            return new MethodType(paramTypes, VOID_TYPE, List.nil(), null);
        } else {
            return new MethodType(originalParamTypes, VOID_TYPE, List.nil(), null);
        }
    }

    private boolean isVoid(JCMethodDecl method) {
        return method.getReturnType().type.getKind().equals(TypeKind.VOID);
    }

    // TODO: refactor
    private JCBlock generateMethodBody(JCMethodDecl wrapper, JCClassDecl clazz,
            JCMethodDecl method) {

        MethodKey key = new MethodKey(clazz.sym.getQualifiedName(), method.getName(), method.type);
        java.util.List<ContractInvocation> postconditions =
                contractsRegistry.getPostconditions(key);

        if (contractAnalyzer.isFirstClassInInheritanceHierarchyWithContracts(clazz, method)) {
            java.util.List<JCStatement> postconditionStatements =
                    generateThisClassPostconditionStatements(wrapper, postconditions, method);
            return treeMaker.Block(0, List.from(postconditionStatements));
        } else {
            JCStatement superMethodCall =
                    generateSuperPostconditionMethodCall(wrapper, clazz, method);
            java.util.List<JCStatement> postconditionStatements =
                    generateThisClassPostconditionStatements(wrapper, postconditions, method);
            return treeMaker.Block(0,
                    List.of(superMethodCall).appendList(List.from(postconditionStatements)));
        }
    }

    private JCStatement generateSuperPostconditionMethodCall(JCMethodDecl wrapper,
            JCClassDecl clazz,
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

        return treeMaker.at(method.pos).Call(methodInvocation);
    }

    private java.util.List<JCStatement> generateThisClassPostconditionStatements(
            JCMethodDecl wrapper, java.util.List<ContractInvocation> postconditions,
            JCMethodDecl method) {

        if (isVoid(method)) {
            return convertPostconditionsToStatements(wrapper, postconditions, null);
        } else {
            VarSymbol resultSymbol = getResultSymbol(wrapper);
            return convertPostconditionsToStatements(wrapper, postconditions, resultSymbol);
        }
    }

    private VarSymbol getResultSymbol(JCMethodDecl wrapper) {
        List<VarSymbol> parameters = wrapper.sym.getParameters();
        int paramLen = parameters.length();
        return parameters.get(paramLen - 1);
    }

    private java.util.List<JCStatement> convertPostconditionsToStatements(JCMethodDecl wrapper,
            java.util.List<ContractInvocation> postconditions, Symbol resultSymbol) {

        // TODO: change that ugly null
        return postconditions.stream()
                .map(contract -> internalInvocationBuilder.build(contract, wrapper, resultSymbol))
                .collect(Collectors.toList());
    }
}
