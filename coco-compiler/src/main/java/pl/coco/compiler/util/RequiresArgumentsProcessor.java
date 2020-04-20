package pl.coco.compiler.util;

import java.util.Arrays;

import com.sun.source.tree.ExpressionTree;
import com.sun.tools.javac.api.JavacTaskImpl;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.tree.TreeMaker;
import com.sun.tools.javac.util.List;

public class RequiresArgumentsProcessor implements ArgumentsProcessor {

    private final TreeMaker treeMaker;
    private final ConditionSupplierProvider conditionSupplierProvider;

    public RequiresArgumentsProcessor(JavacTaskImpl task) {
        this.treeMaker = TreeMaker.instance(task.getContext());
        this.conditionSupplierProvider = new ConditionSupplierProvider(task);
    }

    @Override
    public List<JCTree.JCExpression> processArguments(
            java.util.List<? extends ExpressionTree> arguments) {

        JCTree.JCExpression precondition = (JCTree.JCExpression) arguments.get(0);
        JCTree.JCLiteral preconditionAsStringLiteral = treeMaker.Literal(precondition.toString());

        JCTree.JCLambda conditionSupplier = conditionSupplierProvider.getSupplier(precondition);

        return List.from(Arrays.asList(conditionSupplier, preconditionAsStringLiteral));
    }
}
