package pl.coas.compiler.instrumentation.parsers;

import javax.inject.Singleton;

import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTreeWalker;

import pl.coas.compiler.instrumentation.antlr.ClassPointcutLexer;
import pl.coas.compiler.instrumentation.antlr.ClassPointcutParser;
import pl.coas.compiler.instrumentation.model.pointcut.WildcardString;

@Singleton
public class ClassNameParserImpl
        extends BaseParser<WildcardString, ClassPointcutLexer, ClassPointcutParser> {

    @Override
    public WildcardString parse(String expression) {
        ClassPointcutParser parser = doParse(expression);
        ParseTreeWalker walker = new ParseTreeWalker();
        ClassNameListener listener = new ClassNameListener();
        walker.walk(listener, parser.classExpr());
        return new WildcardString(listener.getClassName());
    }

    @Override
    protected ClassPointcutLexer getLexer(String expression) {
        return new ClassPointcutLexer(CharStreams.fromString(expression));
    }

    @Override
    protected ClassPointcutParser getParser(CommonTokenStream tokens) {
        return new ClassPointcutParser(tokens);
    }
}
