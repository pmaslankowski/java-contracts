package pl.coas.compiler.instrumentation.parsers;

import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.Parser;

import pl.coas.compiler.instrumentation.parsers.util.ThrowingErrorListener;

public abstract class BaseParser<M, L extends Lexer, P extends Parser> {

    public abstract M parse(String expression);

    protected P doParse(String expression) {
        L lexer = getLexer(expression);
        lexer.removeErrorListeners();
        lexer.addErrorListener(new ThrowingErrorListener());

        CommonTokenStream tokens = new CommonTokenStream(lexer);

        P parser = getParser(tokens);
        parser.removeErrorListeners();
        parser.addErrorListener(new ThrowingErrorListener());

        return parser;
    }

    protected abstract L getLexer(String expression);

    protected abstract P getParser(CommonTokenStream tokens);
}
