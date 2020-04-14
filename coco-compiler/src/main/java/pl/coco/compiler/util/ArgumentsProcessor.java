package pl.coco.compiler.util;

import com.sun.source.tree.ExpressionTree;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.util.List;

public interface ArgumentsProcessor {

    List<JCTree.JCExpression> processArguments(java.util.List<? extends ExpressionTree> arguments);
}
