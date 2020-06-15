package pl.coas.compiler.instrumentation.parsers;

import java.util.List;

import javax.inject.Singleton;

import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTreeWalker;

import pl.coas.compiler.instrumentation.antlr.TypeNamesLexer;
import pl.coas.compiler.instrumentation.antlr.TypeNamesParser;

@Singleton
public class TypeNamesParserImpl
        extends BaseParser<List<String>, TypeNamesLexer, TypeNamesParser> {

    @Override
    public List<String> parse(String expression) {
        TypeNamesParser parser = doParse(expression);
        ParseTreeWalker walker = new ParseTreeWalker();
        TypeNamesListener listener = new TypeNamesListener();
        walker.walk(listener, parser.args());
        return listener.getArgs();
    }

    @Override
    protected TypeNamesLexer getLexer(String expression) {
        return new TypeNamesLexer(CharStreams.fromString(expression));
    }

    @Override
    protected TypeNamesParser getParser(CommonTokenStream tokens) {
        return new TypeNamesParser(tokens);
    }
}
