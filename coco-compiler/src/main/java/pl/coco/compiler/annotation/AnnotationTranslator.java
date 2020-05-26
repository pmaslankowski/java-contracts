package pl.coco.compiler.annotation;

import javax.inject.Inject;
import javax.inject.Singleton;

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

        JCExpression parsed = getParsedExpression(annotation);

        JCExpression classAccess = accessBuilder.build(annotation.getCorrespondingMethod(),
                annotation.getPos());

        JCMethodInvocation methodInvocation = treeMaker.Apply(null, classAccess, List.of(parsed));
        return treeMaker.Exec(methodInvocation);
    }

    private JCExpression getParsedExpression(ContractAnnotation annotation) {
        JavacParser parser = getParser(annotation);
        return parser.parseExpression();
    }

    private JavacParser getParser(ContractAnnotation annotation) {
        return parserFactory.newParser(annotation.getExpr(), false, false, false);
    }
}
