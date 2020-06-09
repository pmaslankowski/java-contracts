package pl.coas.compiler.instrumentation.parsers.util;

import java.text.MessageFormat;

import org.antlr.v4.runtime.BaseErrorListener;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.Recognizer;

import pl.coas.compiler.instrumentation.parsers.ParsingException;

public class ThrowingErrorListener extends BaseErrorListener {

    @Override
    public void syntaxError(Recognizer<?, ?> recognizer, Object offendingSymbol, int line,
            int charPositionInLine, String msg, RecognitionException e) {

        throw new ParsingException(
                MessageFormat.format("Parsing error ({0}:{1}): {2}", line, charPositionInLine, msg));
    }
}
