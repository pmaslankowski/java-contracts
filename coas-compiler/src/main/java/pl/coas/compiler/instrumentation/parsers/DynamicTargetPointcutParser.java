package pl.coas.compiler.instrumentation.parsers;

import javax.inject.Inject;
import javax.inject.Singleton;

import pl.coas.compiler.instrumentation.model.pointcut.DynamicTargetPointcut;

@Singleton
public class DynamicTargetPointcutParser {

    private final ClassNameParserImpl classNameParser;

    @Inject
    public DynamicTargetPointcutParser(ClassNameParserImpl classNameParser) {
        this.classNameParser = classNameParser;
    }

    public DynamicTargetPointcut parse(String expression) {
        String className = classNameParser.parse(expression);
        return new DynamicTargetPointcut(className);
    }
}
