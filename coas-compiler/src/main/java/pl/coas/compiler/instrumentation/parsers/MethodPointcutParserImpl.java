package pl.coas.compiler.instrumentation.parsers;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTreeWalker;

import pl.coas.compiler.instrumentation.antlr.MethodPointcutLexer;
import pl.coas.compiler.instrumentation.antlr.MethodPointcutParser;
import pl.coas.compiler.instrumentation.model.pointcut.MethodPointcut;
import pl.coas.compiler.instrumentation.parsers.util.MethodArgumentsFactory;

@Singleton
public class MethodPointcutParserImpl
        extends BaseParser<MethodPointcut, MethodPointcutLexer, MethodPointcutParser> {

    private final MethodArgumentsFactory methodArgumentsFactory;

    @Inject
    public MethodPointcutParserImpl(MethodArgumentsFactory methodArgumentsFactory) {
        this.methodArgumentsFactory = methodArgumentsFactory;
    }

    @Override
    public MethodPointcut parse(String expression) {
        MethodPointcutParser parser = doParse(expression);
        ParseTreeWalker walker = new ParseTreeWalker();
        MethodPointcutListener listener = new MethodPointcutListener();
        walker.walk(listener, parser.method());

        return new MethodPointcut.Builder()
                .withKind(listener.getKind())
                .withModifiers(listener.getModifiers())
                .withReturnType(listener.getReturnType())
                .withClassName(listener.getClassName())
                .withMethodName(listener.getMethodName())
                .withArgumentTypes(methodArgumentsFactory.create(listener.getArgs()))
                .withExceptionsThrown(listener.getExceptions())
                .build();
    }

    @Override
    protected MethodPointcutLexer getLexer(String expression) {
        return new MethodPointcutLexer(CharStreams.fromString(expression));
    }

    @Override
    protected MethodPointcutParser getParser(CommonTokenStream tokens) {
        return new MethodPointcutParser(tokens);
    }
}
