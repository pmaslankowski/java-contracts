package pl.coco.compiler.instrumentation.invocation.arguments;

import java.util.Arrays;

import javax.inject.Inject;

import pl.coco.compiler.instrumentation.invocation.ConditionSupplierProvider;

import com.sun.source.tree.ExpressionTree;
import com.sun.tools.javac.code.Symbol;
import com.sun.tools.javac.tree.JCTree.JCExpression;
import com.sun.tools.javac.tree.JCTree.JCLambda;
import com.sun.tools.javac.tree.JCTree.JCLiteral;
import com.sun.tools.javac.tree.TreeMaker;
import com.sun.tools.javac.util.List;

public class RequiresArgumentsProcessor implements ArgumentsProcessor {

    private final TreeMaker treeMaker;
    private final ConditionSupplierProvider conditionSupplierProvider;

    @Inject
    public RequiresArgumentsProcessor(TreeMaker treeMaker,
            ConditionSupplierProvider conditionSupplierProvider) {

        this.treeMaker = treeMaker;
        this.conditionSupplierProvider = conditionSupplierProvider;
    }

    @Override
    public List<JCExpression> processArguments(java.util.List<? extends ExpressionTree> arguments,
            Symbol resultSymbol) {

        JCExpression precondition = (JCExpression) arguments.get(0);
        JCLiteral preconditionAsStringLiteral = treeMaker.Literal(precondition.toString());

        JCLambda conditionSupplier = conditionSupplierProvider.getSupplier(precondition);

        return List.from(Arrays.asList(conditionSupplier, preconditionAsStringLiteral));
    }
}
