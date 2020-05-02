package pl.coco.compiler.instrumentation;

import static com.sun.tools.javac.code.Symbol.VarSymbol;
import static com.sun.tools.javac.tree.JCTree.JCConditional;
import static com.sun.tools.javac.tree.JCTree.JCUnary;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import javax.inject.Inject;
import javax.inject.Singleton;

import com.sun.tools.javac.code.Symbol;
import com.sun.tools.javac.code.Symbol.ClassSymbol;
import com.sun.tools.javac.code.Symbol.MethodSymbol;
import com.sun.tools.javac.code.Type;
import com.sun.tools.javac.code.Type.ClassType;
import com.sun.tools.javac.tree.JCTree.JCBinary;
import com.sun.tools.javac.tree.JCTree.JCClassDecl;
import com.sun.tools.javac.tree.JCTree.JCExpression;
import com.sun.tools.javac.tree.JCTree.JCIdent;
import com.sun.tools.javac.tree.JCTree.JCMethodDecl;
import com.sun.tools.javac.tree.TreeScanner;
import com.sun.tools.javac.util.Name;

import pl.coco.compiler.instrumentation.invocation.ContractInvocation;
import pl.coco.compiler.instrumentation.registry.ContractsRegistry;
import pl.coco.compiler.instrumentation.registry.MethodKey;
import pl.coco.compiler.util.TreePasser;
import pl.coco.compiler.util.TypeRegistry;

@Singleton
public class ContractProvider {

    private static final String JAVA_LANG_OBJECT = "java.lang.Object";

    private final ContractsRegistry contractsRegistry;
    private final TypeRegistry typeRegistry;

    @Inject
    public ContractProvider(ContractsRegistry contractsRegistry, TypeRegistry typeRegistry) {
        this.contractsRegistry = contractsRegistry;
        this.typeRegistry = typeRegistry;
    }

    public List<ContractInvocation> getContracts(JCClassDecl clazz, JCMethodDecl method) {

        Type objectType = typeRegistry.getClassType(JAVA_LANG_OBJECT);

        Name methodName = method.getName();
        Type methodType = method.type;
        ClassType classType = (ClassType) clazz.type;

        while (!classType.equals(objectType)) {
            Name className = classType.tsym.getQualifiedName();
            MethodKey methodKey = new MethodKey(className, methodName, methodType);

            List<ContractInvocation> contracts = contractsRegistry.getContracts(methodKey);
            if (!contracts.isEmpty()) {
                adjustContractsForSubtypeMethod(contracts, method);
                return contracts;
            }

            classType = (ClassType) classType.supertype_field;
        }

        return Collections.emptyList();
    }

    private void adjustContractsForSubtypeMethod(List<ContractInvocation> contracts,
            JCMethodDecl method) {

        for (ContractInvocation contract : contracts) {
            JCExpression condition = (JCExpression) contract.getArguments().get(0);
            condition.accept(new ArgumentVisitor(method.sym));
        }
    }

    private static class ArgumentVisitor extends TreeScanner {

        private final MethodSymbol ownerMethod;

        public ArgumentVisitor(Symbol ownerSymbol) {
            this.ownerMethod = (MethodSymbol) ownerSymbol;
        }

        @Override
        public void visitUnary(JCUnary unary) {
            fixVariableOwners(unary.arg);
            super.visitUnary(unary);
        }

        @Override
        public void visitConditional(JCConditional conditional) {
            fixVariableOwners(conditional.cond);
            fixVariableOwners(conditional.falsepart);
            fixVariableOwners(conditional.truepart);
            super.visitConditional(conditional);
        }

        @Override
        public void visitBinary(JCBinary binary) {
            fixVariableOwners(binary.lhs);
            fixVariableOwners(binary.rhs);
            super.visitBinary(binary);
        }

        private void fixVariableOwners(JCExpression expr) {
            Optional<Symbol> symbol = TreePasser.of(expr)
                    .as(JCIdent.class)
                    .mapAndGet(identifier -> identifier.sym);

            symbol.ifPresent(sym -> fixSymbol(expr, sym));
        }

        private void fixSymbol(JCExpression expr, Symbol symbol) {
            if (symbol instanceof VarSymbol) {
                VarSymbol varSymbol = (VarSymbol) symbol;
                if (symbol.owner instanceof MethodSymbol) {
                    fixMethodArgumentSymbol((JCIdent) expr, varSymbol);
                } else if (symbol.owner instanceof ClassSymbol) {
                    fixFieldSymbol(symbol);
                }
            }
        }

        private void fixMethodArgumentSymbol(JCIdent expr, VarSymbol symbol) {
            expr.sym = ownerMethod.params.get(symbol.adr);
        }

        private void fixFieldSymbol(Symbol symbol) {
            symbol.owner = ownerMethod.owner;
        }
    }
}
