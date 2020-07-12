package pl.coco.compiler.instrumentation.synthetic;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import com.sun.tools.javac.code.Symbol.VarSymbol;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.tree.JCTree.JCArrayAccess;
import com.sun.tools.javac.tree.JCTree.JCConditional;
import com.sun.tools.javac.tree.JCTree.JCExpression;
import com.sun.tools.javac.tree.JCTree.JCFieldAccess;
import com.sun.tools.javac.tree.JCTree.JCIdent;
import com.sun.tools.javac.tree.JCTree.JCMethodDecl;
import com.sun.tools.javac.tree.JCTree.JCStatement;
import com.sun.tools.javac.tree.JCTree.JCUnary;
import com.sun.tools.javac.tree.JCTree.JCVariableDecl;
import com.sun.tools.javac.tree.TreeMaker;
import com.sun.tools.javac.tree.TreeScanner;

import pl.coco.compiler.instrumentation.ContractMethod;
import pl.coco.compiler.instrumentation.invocation.internal.old.OldValue;
import pl.coco.compiler.instrumentation.invocation.internal.old.OldValueSymbolProvider;
import pl.coco.compiler.model.ContractInvocation;
import pl.coco.compiler.util.ContractAstUtil;

@Singleton
public class OldValuesProcessor {

    private final OldValueSymbolProvider oldValueSymProvider;
    private final TreeMaker tm;

    @Inject
    public OldValuesProcessor(OldValueSymbolProvider oldValueSymbolProvider, TreeMaker tm) {
        this.oldValueSymProvider = oldValueSymbolProvider;
        this.tm = tm;
    }

    public List<OldValue> processOldValues(JCMethodDecl method) {
        List<JCStatement> statements = method.getBody().getStatements();
        OldValueProcessingVisitor visitor =
                new OldValueProcessingVisitor(method, oldValueSymProvider, tm);

        for (JCStatement statement : statements) {
            if (ContractAstUtil.isContractInvocation(statement)) {
                ContractInvocation invocation = ContractAstUtil.getContractInvocation(statement);
                ContractMethod contractMethod = invocation.getContractMethod();
                if (contractMethod.isContractSpecification()) {
                    JCExpression condition = (JCExpression) invocation.getArguments().get(0);
                    condition.accept(visitor);
                }
            }
        }

        return visitor.getOldValues();
    }

    // Old values should be instrumented in two passes: in the first pass we should scan all
    // preconditions in order to find all Contract.old() usages. The second pass (changing
    // Contract.old() invocations to corresponding local variables) should take place later, after
    // postcondition contract messages are generated. This way the messages will be clearer and
    // more user friendly. Now the message contains a synthetic symbol e. g. coco$old$val.
    private static class OldValueProcessingVisitor extends TreeScanner {

        private final OldValueSymbolProvider symbolProvider;
        private final TreeMaker tm;
        private final JCMethodDecl method;
        private final List<OldValue> oldValues = new ArrayList<>();

        private OldValueProcessingVisitor(JCMethodDecl method,
                OldValueSymbolProvider symbolProvider, TreeMaker tm) {
            this.method = method;
            this.symbolProvider = symbolProvider;
            this.tm = tm;
        }

        public List<OldValue> getOldValues() {
            return oldValues;
        }

        @Override
        public void visitSelect(JCFieldAccess fieldAccess) {
            if (isOldInvocation(fieldAccess.selected)) {
                fieldAccess.selected = processOldValue(fieldAccess.selected);
            }

            super.visitSelect(fieldAccess);
        }

        @Override
        public void visitIndexed(JCArrayAccess arrayAccess) {
            if (isOldInvocation(arrayAccess.index)) {
                arrayAccess.index = processOldValue(arrayAccess.index);
            }
            if (isOldInvocation(arrayAccess.indexed)) {
                arrayAccess.indexed = processOldValue(arrayAccess.indexed);
            }

            super.visitIndexed(arrayAccess);
        }

        @Override
        public void visitUnary(JCUnary unary) {
            if (isOldInvocation(unary.arg)) {
                unary.arg = processOldValue(unary.arg);
            }
            super.visitUnary(unary);
        }

        @Override
        public void visitConditional(JCConditional conditional) {
            if (isOldInvocation(conditional.cond)) {
                conditional.cond = processOldValue(conditional.cond);
            }
            if (isOldInvocation(conditional.truepart)) {
                conditional.truepart = processOldValue(conditional.truepart);
            }
            if (isOldInvocation(conditional.falsepart)) {
                conditional.falsepart = processOldValue(conditional.falsepart);
            }
            super.visitConditional(conditional);
        }

        @Override
        public void visitBinary(JCTree.JCBinary binary) {
            if (isOldInvocation(binary.lhs)) {
                binary.lhs = processOldValue(binary.lhs);
            }
            if (isOldInvocation(binary.rhs)) {
                binary.rhs = processOldValue(binary.rhs);
            }
            super.visitBinary(binary);
        }

        private boolean isOldInvocation(JCExpression expr) {
            if (ContractAstUtil.isContractInvocation(expr)) {
                ContractInvocation invocation = ContractAstUtil.getContractInvocation(expr);
                return invocation.getContractMethod() == ContractMethod.OLD;
            }
            return false;
        }

        private JCExpression processOldValue(JCExpression expr) {
            ContractInvocation oldInvocation = getOldInvocation(expr);
            JCIdent argument = (JCIdent) oldInvocation.getArguments().get(0);
            JCVariableDecl originalArgument = method.getParameters().stream()
                    .filter(param -> param.getName().contentEquals(argument.getName()))
                    .findAny()
                    .orElseThrow(() -> new IllegalStateException(
                            "Contract.old argument has to be one of the original method " +
                                    "arguments"));
            VarSymbol oldSymbolCopy = symbolProvider.getOldSymbol(method, originalArgument);
            oldValues.add(new OldValue(originalArgument, oldSymbolCopy));
            return tm.Ident(oldSymbolCopy);
        }

        private ContractInvocation getOldInvocation(JCExpression expr) {
            if (ContractAstUtil.isContractInvocation(expr)) {
                return ContractAstUtil.getContractInvocation(expr);
            }
            throw new IllegalArgumentException(
                    "Expr: " + expr + "is not a Contract.old invocation");
        }
    }
}
