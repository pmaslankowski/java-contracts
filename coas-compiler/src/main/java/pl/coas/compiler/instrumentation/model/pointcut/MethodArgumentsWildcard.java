package pl.coas.compiler.instrumentation.model.pointcut;

import java.util.List;

public class MethodArgumentsWildcard implements MethodArguments {

    @Override
    public boolean match(List<String> arguments) {
        return true;
    }
}
