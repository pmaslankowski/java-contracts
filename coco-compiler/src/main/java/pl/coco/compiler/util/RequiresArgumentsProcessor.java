package pl.coco.compiler.util;

import java.util.Arrays;

import com.sun.source.tree.ExpressionTree;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.tree.TreeMaker;
import com.sun.tools.javac.util.List;

public class RequiresArgumentsProcessor implements ArgumentsProcessor {

    private final TreeMaker treeMaker;

    public RequiresArgumentsProcessor(TreeMaker treeMaker) {
        this.treeMaker = treeMaker;
    }

    @Override
    public List<JCTree.JCExpression> processArguments(
            java.util.List<? extends ExpressionTree> arguments) {

        JCTree.JCExpression precondition = (JCTree.JCExpression) arguments.get(0);
        JCTree.JCLiteral preconditionAsStringLiteral = treeMaker.Literal(precondition.toString());
        return List.from(Arrays.asList(precondition, preconditionAsStringLiteral));
    }
}
