package pl.coas.compiler.instrumentation.parsers;

import javax.inject.Singleton;

import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTreeWalker;

import pl.coas.compiler.instrumentation.antlr.ClassNameLexer;
import pl.coas.compiler.instrumentation.antlr.ClassNameParser;

@Singleton
public class ClassNameParserImpl
        extends BaseParser<String, ClassNameLexer, ClassNameParser> {

    @Override
    public String parse(String expression) {
        ClassNameParser parser = doParse(expression);
        ParseTreeWalker walker = new ParseTreeWalker();
        ClassNameListener listener = new ClassNameListener();
        walker.walk(listener, parser.classExpr());
        return listener.getClassName();
    }

    @Override
    protected ClassNameLexer getLexer(String expression) {
        return new ClassNameLexer(CharStreams.fromString(expression));
    }

    @Override
    protected ClassNameParser getParser(CommonTokenStream tokens) {
        return new ClassNameParser(tokens);
    }
}
