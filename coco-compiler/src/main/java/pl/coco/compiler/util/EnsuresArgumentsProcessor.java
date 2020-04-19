package pl.coco.compiler.util;

import java.util.Arrays;

import com.sun.source.tree.ExpressionTree;
import com.sun.source.tree.MethodInvocationTree;
import com.sun.tools.javac.code.Symbol;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.tree.TreeMaker;
import com.sun.tools.javac.tree.TreeScanner;
import com.sun.tools.javac.util.List;

import pl.coco.compiler.ContractMethod;
import pl.coco.compiler.instrumentation.ContractInvocation;
import pl.coco.compiler.instrumentation.SimpleMethodInvocation;

public class EnsuresArgumentsProcessor implements ArgumentsProcessor {

    private final TreeMaker treeMaker;
    private final Symbol resultSymbol;

    public EnsuresArgumentsProcessor(TreeMaker treeMaker, Symbol resultSymbol) {
        this.treeMaker = treeMaker;
        this.resultSymbol = resultSymbol;
    }

    @Override
    public List<JCTree.JCExpression> processArguments(
            java.util.List<? extends ExpressionTree> arguments) {

        JCTree.JCExpression postcondition = (JCTree.JCExpression) arguments.get(0);
        JCTree.JCLiteral postconditionAsStringLiteral = treeMaker.Literal(postcondition.toString());

        postcondition.accept(new PostconditionArgumentVisitor(treeMaker, resultSymbol));

        return List.from(Arrays.asList(postcondition, postconditionAsStringLiteral));
    }

    private static class PostconditionArgumentVisitor extends TreeScanner {

        private final TreeMaker treeMaker;
        private final Symbol resultSymbol;

        public PostconditionArgumentVisitor(TreeMaker treeMaker, Symbol resultSymbol) {
            this.treeMaker = treeMaker;
            this.resultSymbol = resultSymbol;
        }

        @Override
        public void visitUnary(JCTree.JCUnary unary) {
            if (isResultInvocation(unary.arg)) {
                unary.arg = newResultReference();
            }
            super.visitUnary(unary);
        }

        private JCTree.JCIdent newResultReference() {
            return treeMaker.Ident(resultSymbol);
        }

        @Override
        public void visitConditional(JCTree.JCConditional conditional) {
            if (isResultInvocation(conditional.cond)) {
                conditional.cond = newResultReference();
            }
            if (isResultInvocation(conditional.truepart)) {
                conditional.truepart = newResultReference();
            }
            if (isResultInvocation(conditional.falsepart)) {
                conditional.falsepart = newResultReference();
            }
            super.visitConditional(conditional);
        }

        @Override
        public void visitBinary(JCTree.JCBinary binary) {
            if (isResultInvocation(binary.lhs)) {
                binary.lhs = newResultReference();
            }
            if (isResultInvocation(binary.rhs)) {
                binary.rhs = newResultReference();
            }
            super.visitBinary(binary);
        }

        private boolean isResultInvocation(JCTree.JCExpression expr) {
            return TreePasser.of(expr)
                    .as(MethodInvocationTree.class)
                    .flatMapAndGet(SimpleMethodInvocation::of)
                    .flatMap(ContractInvocation::of)
                    .map(invocation -> invocation.getContractMethod() == ContractMethod.RESULT)
                    .orElse(false);
        }
    }
}
