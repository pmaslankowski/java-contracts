package pl.coco.compiler.annotation;

import javax.inject.Inject;
import javax.inject.Singleton;

import com.google.common.base.Strings;
import com.sun.tools.javac.parser.JavacParser;
import com.sun.tools.javac.parser.ParserFactory;
import com.sun.tools.javac.tree.JCTree.JCExpression;
import com.sun.tools.javac.tree.JCTree.JCMethodInvocation;
import com.sun.tools.javac.tree.JCTree.JCStatement;
import com.sun.tools.javac.tree.TreeMaker;
import com.sun.tools.javac.util.List;

import pl.coco.compiler.annotation.util.SimpleAccessBuilder;

@Singleton
public class AnnotationTranslator {

    private final ParserFactory parserFactory;
    private final TreeMaker treeMaker;
    private final SimpleAccessBuilder accessBuilder;

    @Inject
    public AnnotationTranslator(ParserFactory parserFactory, TreeMaker treeMaker,
            SimpleAccessBuilder accessBuilder) {
        this.parserFactory = parserFactory;
        this.treeMaker = treeMaker;
        this.accessBuilder = accessBuilder;
    }

    public JCStatement translate(ContractAnnotation annotation) {
        int pos = annotation.getPos();
        JCExpression parsed = getParsedExpression(annotation);

        JCExpression classAccess = accessBuilder.build(annotation.getCorrespondingMethod(), pos);

        JCMethodInvocation methodInvocation = treeMaker.at(pos)
                .Apply(null, classAccess, List.of(parsed));
        return treeMaker.at(pos).Exec(methodInvocation);
    }

    private JCExpression getParsedExpression(ContractAnnotation annotation) {
        JavacParser parser = getParser(annotation);
        JCExpression parsed = parser.parseExpression();
        return parsed;
    }

    private JavacParser getParser(ContractAnnotation annotation) {
        String expr = getExpressionExpandedToCorrectPosition(annotation);
        return parserFactory.newParser(expr, false, true, true);
    }

    private String getExpressionExpandedToCorrectPosition(ContractAnnotation annotation) {
        // this is workaround for getting correct positions in parsed tree
        // we add appropriate number of new line characters which will be ignored by parser
        return Strings.repeat("\n", annotation.getPos()) + annotation.getExpr();
    }
}
