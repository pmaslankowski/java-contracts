package pl.coas.compiler.instrumentation.parsers;

import javax.inject.Inject;
import javax.inject.Singleton;

import pl.coas.compiler.instrumentation.model.pointcut.StaticTargetPointcut;

@Singleton
public class StaticTargetPointcutParser {

    private final ClassNameParserImpl classNameParser;

    @Inject
    public StaticTargetPointcutParser(ClassNameParserImpl classNameParser) {
        this.classNameParser = classNameParser;
    }

    public StaticTargetPointcut parse(String expression) {
        String className = classNameParser.parse(expression);
        return new StaticTargetPointcut(className);
    }
}
