package pl.coas.compiler.instrumentation.parsers;

import javax.inject.Inject;
import javax.inject.Singleton;

import pl.coas.compiler.instrumentation.model.pointcut.TargetPointcut;

@Singleton
public class TargetPointcutParser {

    private final ClassNameParserImpl classNameParser;

    @Inject
    public TargetPointcutParser(ClassNameParserImpl classNameParser) {
        this.classNameParser = classNameParser;
    }

    public TargetPointcut parse(String expression) {
        String className = classNameParser.parse(expression);
        return new TargetPointcut(className);
    }
}
