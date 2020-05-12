package pl.coco.compiler.instrumentation.synthetic;

import javax.inject.Inject;
import javax.inject.Singleton;

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
import pl.coco.compiler.instrumentation.invocation.InternalInvocationBuilder;
import pl.coco.compiler.instrumentation.invocation.MethodInvocationBuilder;
import pl.coco.compiler.instrumentation.invocation.MethodInvocationDescription;
import pl.coco.compiler.instrumentation.registry.ContractsRegistry;
import pl.coco.compiler.instrumentation.registry.MethodKey;
import pl.coco.compiler.util.AstUtil;

@Singleton
public class PostconditionMethodGenerator extends AbstractMethodGenerator {

    private static final JCVoidType VOID_TYPE = new JCVoidType();

    private final MethodInvocationBuilder methodInvocationBuilder;
    private final ContractsRegistry contractsRegistry;
    private final ContractAnalyzer contractAnalyzer;
    private final SyntheticMethodNameGenerator nameGenerator;

    @Inject
    public PostconditionMethodGenerator(TreeMaker treeMaker,
            InternalInvocationBuilder internalInvocationBuilder,
            MethodInvocationBuilder methodInvocationBuilder, Names names,
            ContractsRegistry contractsRegistry, ContractAnalyzer contractAnalyzer,
            SyntheticMethodNameGenerator nameGenerator) {

        super(treeMaker, names, internalInvocationBuilder);
        this.methodInvocationBuilder = methodInvocationBuilder;
        this.contractsRegistry = contractsRegistry;
        this.contractAnalyzer = contractAnalyzer;
        this.nameGenerator = nameGenerator;
    }

    @Override
    public JCMethodDecl generate(JCClassDecl clazz, JCMethodDecl method) {
        MethodSymbol wrapperSymbol = getMethodSymbol(method);
        JCMethodDecl wrapper = treeMaker.MethodDef(wrapperSymbol, wrapperSymbol.type, null);
        wrapper.body = generateMethodBody(wrapper, clazz, method);
        return wrapper;
    }

    private MethodSymbol getMethodSymbol(JCMethodDecl originalMethod) {
        Name methodName = nameGenerator.getPostconditionMethodName(originalMethod);
        long flags = getProtectedMethodFlags(originalMethod);
        MethodType type = getMethodType(originalMethod);

        MethodSymbol result = new MethodSymbol(flags, methodName, type, originalMethod.sym.owner);

        result.params = originalMethod.sym.params;
        if (AstUtil.isVoid(originalMethod)) {
            return result;
        } else {
            VarSymbol resultSymbol = createResultSymbol(originalMethod, result);
            result.params = result.params.append(resultSymbol);
            return result;
        }
    }

    private VarSymbol createResultSymbol(JCMethodDecl originalMethod, MethodSymbol result) {
        VarSymbol resultSymbol = new VarSymbol(0, names.fromString("result"),
                originalMethod.getReturnType().type, result);

        if (originalMethod.sym.isStatic()) {
            resultSymbol.adr = result.params.length();
        } else {
            resultSymbol.adr = result.params.length() + 1;
        }
        return resultSymbol;
    }

    private MethodType getMethodType(JCMethodDecl originalMethod) {
        List<Type> originalParamTypes = originalMethod.sym.type.getParameterTypes();
        if (!AstUtil.isVoid(originalMethod)) {
            Type returnType = originalMethod.sym.getReturnType();
            List<Type> paramTypes = originalParamTypes.append(returnType);
            return new MethodType(paramTypes, VOID_TYPE, List.nil(), null);
        } else {
            return new MethodType(originalParamTypes, VOID_TYPE, List.nil(), null);
        }
    }

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

        if (AstUtil.isVoid(method)) {
            return convertContractsToStatements(wrapper, postconditions, null);
        } else {
            VarSymbol resultSymbol = getResultSymbol(wrapper);
            return convertContractsToStatements(wrapper, postconditions, resultSymbol);
        }
    }

    private VarSymbol getResultSymbol(JCMethodDecl wrapper) {
        List<VarSymbol> parameters = wrapper.sym.getParameters();
        int paramLen = parameters.length();
        return parameters.get(paramLen - 1);
    }
}
