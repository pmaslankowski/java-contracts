package pl.coco.compiler.instrumentation.invocation.internal.invariant;

import java.util.Arrays;

import javax.inject.Inject;
import javax.inject.Singleton;

import com.sun.source.tree.ExpressionTree;
import com.sun.tools.javac.code.Symbol;
import com.sun.tools.javac.code.Symbol.VarSymbol;
import com.sun.tools.javac.code.Symtab;
import com.sun.tools.javac.code.Type;
import com.sun.tools.javac.tree.JCTree.JCExpression;
import com.sun.tools.javac.tree.JCTree.JCIdent;
import com.sun.tools.javac.tree.JCTree.JCLambda;
import com.sun.tools.javac.tree.JCTree.JCLiteral;
import com.sun.tools.javac.tree.JCTree.JCMethodDecl;
import com.sun.tools.javac.tree.JCTree.JCMethodInvocation;
import com.sun.tools.javac.tree.JCTree.JCStatement;
import com.sun.tools.javac.tree.TreeMaker;
import com.sun.tools.javac.util.List;

import pl.coco.compiler.instrumentation.ContractMethod;
import pl.coco.compiler.instrumentation.invocation.ContractInvocation;
import pl.coco.compiler.instrumentation.invocation.MethodInvocationBuilder;
import pl.coco.compiler.instrumentation.invocation.MethodInvocationDescription;
import pl.coco.compiler.instrumentation.invocation.internal.ConditionSupplierProvider;
import pl.coco.compiler.instrumentation.synthetic.IsBeforeSymbolProvider;
import pl.coco.compiler.util.TypeRegistry;

@Singleton
public class InvariantInvocationBuilder {

    private static final String STRING_TYPE_NAME = "java.lang.String";
    private static final String CONDITION_SUPPLIER_TYPE = "pl.coco.internal.ConditionSupplier";

    private final TreeMaker treeMaker;
    private final TypeRegistry typeRegistry;
    private final MethodInvocationBuilder methodInvocationBuilder;
    private final ConditionSupplierProvider conditionSupplierProvider;
    private final Symtab symtab;
    private final IsBeforeSymbolProvider isBeforeSymbolProvider;

    @Inject
    public InvariantInvocationBuilder(TreeMaker treeMaker,
            TypeRegistry typeRegistry, MethodInvocationBuilder methodInvocationBuilder,
            ConditionSupplierProvider conditionSupplierProvider, Symtab symtab,
            IsBeforeSymbolProvider isBeforeSymbolProvider) {

        this.treeMaker = treeMaker;
        this.typeRegistry = typeRegistry;
        this.methodInvocationBuilder = methodInvocationBuilder;
        this.conditionSupplierProvider = conditionSupplierProvider;
        this.symtab = symtab;
        this.isBeforeSymbolProvider = isBeforeSymbolProvider;
    }

    public JCStatement build(ContractInvocation invocation, JCMethodDecl currMethod) {

        MethodInvocationDescription desc = new MethodInvocationDescription.Builder()
                .withClassName(invocation.getContractMethod().getInternalClassName())
                .withArguments(getArgumentsForContractCall(currMethod, invocation.getArguments()))
                .withPosition(currMethod.pos)
                .withMethodSymbol(getInternalContractMethodSymbol(invocation, currMethod))
                .build();
        JCMethodInvocation methodInvocation = methodInvocationBuilder.build(desc);

        return treeMaker.at(currMethod.pos)
                .Call(methodInvocation);
    }

    private Symbol getInternalContractMethodSymbol(ContractInvocation contractInvocation,
            JCMethodDecl currentMethod) {

        ContractMethod contractMethod = contractInvocation.getContractMethod();
        Type string = typeRegistry.getType(STRING_TYPE_NAME);
        Type bool = symtab.booleanType;
        Type conditionSupplier = typeRegistry.getType(CONDITION_SUPPLIER_TYPE);

        List<Type> arguments = List.from(Arrays.asList(conditionSupplier, string, bool));
        return typeRegistry.getMethodSymbol(contractMethod.getInternalClassName(),
                contractMethod.getMethodName(), currentMethod, arguments);
    }

    private List<JCExpression> getArgumentsForContractCall(JCMethodDecl method,
            java.util.List<? extends ExpressionTree> arguments) {

        JCExpression invariant = (JCExpression) arguments.get(0);
        JCLiteral invariantAsStringLiteral = treeMaker.Literal(invariant.toString());
        JCIdent isBefore = getIsBeforeIdentifier(method);
        JCLambda conditionSupplier = conditionSupplierProvider.getSupplier(invariant);

        return List.from(Arrays.asList(conditionSupplier, invariantAsStringLiteral, isBefore));
    }

    private JCIdent getIsBeforeIdentifier(JCMethodDecl method) {
        VarSymbol isBeforeSymbol = isBeforeSymbolProvider.get(method);
        isBeforeSymbol.adr = 1;
        return treeMaker.Ident(isBeforeSymbol);
    }
}
