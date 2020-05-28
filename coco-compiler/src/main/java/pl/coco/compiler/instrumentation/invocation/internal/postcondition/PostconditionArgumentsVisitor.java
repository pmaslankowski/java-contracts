package pl.coco.compiler.instrumentation.invocation.internal.postcondition;

import com.sun.source.tree.MethodInvocationTree;
import com.sun.tools.javac.code.Symbol;
import com.sun.tools.javac.tree.JCTree.JCArrayAccess;
import com.sun.tools.javac.tree.JCTree.JCBinary;
import com.sun.tools.javac.tree.JCTree.JCConditional;
import com.sun.tools.javac.tree.JCTree.JCExpression;
import com.sun.tools.javac.tree.JCTree.JCIdent;
import com.sun.tools.javac.tree.JCTree.JCUnary;
import com.sun.tools.javac.tree.TreeMaker;
import com.sun.tools.javac.tree.TreeScanner;

import pl.coco.compiler.instrumentation.ContractMethod;
import pl.coco.compiler.model.ContractInvocation;
import pl.coco.compiler.model.SimpleMethodInvocation;
import pl.coco.compiler.util.TreePasser;

public class PostconditionArgumentsVisitor extends TreeScanner {

    private final TreeMaker treeMaker;
    private final Symbol resultSymbol;

    public PostconditionArgumentsVisitor(TreeMaker treeMaker, Symbol resultSymbol) {
        this.treeMaker = treeMaker;
        this.resultSymbol = resultSymbol;
    }

    // TODO: dodać na to unit test
    @Override
    public void visitIndexed(JCArrayAccess arrayAccess) {
        if (isResultInvocation(arrayAccess.index)) {
            arrayAccess.index = newResultReference();
        }
        if (isResultInvocation(arrayAccess.indexed)) {
            arrayAccess.indexed = newResultReference();
        }

        super.visitIndexed(arrayAccess);
    }

    @Override
    public void visitUnary(JCUnary unary) {
        if (isResultInvocation(unary.arg)) {
            unary.arg = newResultReference();
        }
        super.visitUnary(unary);
    }

    private JCIdent newResultReference() {
        return treeMaker.Ident(resultSymbol);
    }

    @Override
    public void visitConditional(JCConditional conditional) {
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
    public void visitBinary(JCBinary binary) {
        if (isResultInvocation(binary.lhs)) {
            binary.lhs = newResultReference();
        }
        if (isResultInvocation(binary.rhs)) {
            binary.rhs = newResultReference();
        }
        super.visitBinary(binary);
    }

    private boolean isResultInvocation(JCExpression expr) {
        return TreePasser.of(expr)
                .as(MethodInvocationTree.class)
                .flatMapAndGet(SimpleMethodInvocation::of)
                .flatMap(ContractInvocation::of)
                .map(invocation -> invocation.getContractMethod() == ContractMethod.RESULT)
                .orElse(false);
    }
}
