package pl.coas.compiler.instrumentation.parsers;

import javax.inject.Singleton;

import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTreeWalker;

import pl.coas.compiler.instrumentation.antlr.ArgsPointcutLexer;
import pl.coas.compiler.instrumentation.antlr.ArgsPointcutParser;
import pl.coas.compiler.instrumentation.model.pointcut.ArgsPointcut;

@Singleton
public class ArgsPointcutParserImpl extends BaseParser<ArgsPointcutLexer, ArgsPointcutParser> {

    public ArgsPointcut parse(String expression) {
        ArgsPointcutParser parser = doParse(expression);
        ParseTreeWalker walker = new ParseTreeWalker();
        ArgsPointcutListener listener = new ArgsPointcutListener();
        walker.walk(listener, parser.args());
        return new ArgsPointcut(listener.getArgs());
    }

    @Override
    protected ArgsPointcutLexer getLexer(String expression) {
        return new ArgsPointcutLexer(CharStreams.fromString(expression));
    }

    @Override
    protected ArgsPointcutParser getParser(CommonTokenStream tokens) {
        return new ArgsPointcutParser(tokens);
    }
}
