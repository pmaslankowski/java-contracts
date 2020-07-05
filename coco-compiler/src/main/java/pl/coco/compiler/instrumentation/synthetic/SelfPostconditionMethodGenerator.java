package pl.coco.compiler.instrumentation.synthetic;

import java.util.stream.Collectors;

import javax.inject.Inject;
import javax.inject.Singleton;

import com.sun.tools.javac.code.Symbol;
import com.sun.tools.javac.code.Symbol.MethodSymbol;
import com.sun.tools.javac.code.Symbol.VarSymbol;
import com.sun.tools.javac.code.Type;
import com.sun.tools.javac.code.Type.MethodType;
import com.sun.tools.javac.code.Types;
import com.sun.tools.javac.tree.JCTree.JCBlock;
import com.sun.tools.javac.tree.JCTree.JCClassDecl;
import com.sun.tools.javac.tree.JCTree.JCMethodDecl;
import com.sun.tools.javac.tree.JCTree.JCStatement;
import com.sun.tools.javac.tree.TreeMaker;
import com.sun.tools.javac.util.List;
import com.sun.tools.javac.util.Name;
import com.sun.tools.javac.util.Names;

import pl.coco.compiler.instrumentation.ContractMethod;
import pl.coco.compiler.instrumentation.invocation.internal.postcondition.EnsuresInvocationBuilder;
import pl.coco.compiler.model.ContractInvocation;
import pl.coco.compiler.util.ContractAstUtil;
import pl.compiler.commons.util.AstUtil;

@Singleton
public class SelfPostconditionMethodGenerator extends AbstractMethodGenerator {

    private static final Type.JCVoidType VOID_TYPE = new Type.JCVoidType();

    private final EnsuresInvocationBuilder ensuresInvocationBuilder;
    private final SyntheticMethodNameGenerator nameGenerator;
    private final Types types;

    @Inject
    public SelfPostconditionMethodGenerator(TreeMaker treeMaker,
            EnsuresInvocationBuilder ensuresInvocationBuilder,
            Names names,
            SyntheticMethodNameGenerator nameGenerator,
            Types types) {

        super(treeMaker, names);
        this.ensuresInvocationBuilder = ensuresInvocationBuilder;
        this.nameGenerator = nameGenerator;
        this.types = types;
    }

    @Override
    public JCMethodDecl generate(JCClassDecl clazz, JCMethodDecl method) {
        MethodSymbol wrapperSymbol = getMethodSymbol(method);
        JCMethodDecl wrapper = treeMaker.MethodDef(wrapperSymbol, wrapperSymbol.type, null);
        wrapper.body = generateMethodBody(wrapper, method);
        return wrapper;
    }

    private MethodSymbol getMethodSymbol(JCMethodDecl originalMethod) {
        Name methodName = nameGenerator.getSelfPostconditionMethodName(originalMethod);
        long flags = getProtectedMethodFlags(originalMethod);
        MethodType type = getMethodType(originalMethod);

        MethodSymbol result =
                new MethodSymbol(flags, methodName, type, originalMethod.sym.owner);

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
                getBoxedReturnType(originalMethod), result);

        if (originalMethod.sym.isStatic()) {
            resultSymbol.adr = result.params.length();
        } else {
            resultSymbol.adr = result.params.length() + 1;
        }
        return resultSymbol;
    }

    private Type getBoxedReturnType(JCMethodDecl originalMethod) {
        return types.boxedTypeOrType(originalMethod.getReturnType().type);
    }

    private MethodType getMethodType(JCMethodDecl originalMethod) {
        List<Type> originalParamTypes = originalMethod.sym.type.getParameterTypes();
        if (!AstUtil.isVoid(originalMethod)) {
            Type returnType = getBoxedReturnType(originalMethod);
            List<Type> paramTypes = originalParamTypes.append(returnType);
            return new MethodType(paramTypes, VOID_TYPE, List.nil(), null);
        } else {
            return new MethodType(originalParamTypes, VOID_TYPE, List.nil(), null);
        }
    }

    private JCBlock generateMethodBody(JCMethodDecl wrapper, JCMethodDecl method) {
        java.util.List<ContractInvocation> postconditions = getSelfPostconditions(method);
        java.util.List<JCStatement> postconditionStatements =
                generateThisClassPostconditionStatements(wrapper, postconditions, method);
        return treeMaker.Block(0, List.from(postconditionStatements));

    }

    private java.util.List<ContractInvocation> getSelfPostconditions(JCMethodDecl method) {
        return method.getBody().getStatements().stream()
                .filter(stmt -> ContractAstUtil.isContractInvocation(stmt,
                        ContractMethod.ENSURES_SELF))
                .map(ContractAstUtil::getContractInvocation)
                .collect(Collectors.toList());
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

    private java.util.List<JCStatement> convertContractsToStatements(
            JCMethodDecl wrapper,
            java.util.List<ContractInvocation> postconditions, Symbol resultSymbol) {

        return postconditions.stream()
                .map(contract -> ensuresInvocationBuilder.build(contract, wrapper, resultSymbol))
                .collect(Collectors.toList());
    }

    private VarSymbol getResultSymbol(JCMethodDecl wrapper) {
        List<VarSymbol> parameters = wrapper.sym.getParameters();
        int paramLen = parameters.length();
        return parameters.get(paramLen - 1);
    }
}
