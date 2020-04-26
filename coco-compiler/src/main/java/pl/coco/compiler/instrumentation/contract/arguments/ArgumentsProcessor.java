package pl.coco.compiler.instrumentation.contract.arguments;

import com.sun.source.tree.ExpressionTree;
import com.sun.tools.javac.code.Symbol;
import com.sun.tools.javac.tree.JCTree.JCExpression;
import com.sun.tools.javac.util.List;

public interface ArgumentsProcessor {

    List<JCExpression> processArguments(java.util.List<? extends ExpressionTree> arguments,
            Symbol resultSymbol);
}
