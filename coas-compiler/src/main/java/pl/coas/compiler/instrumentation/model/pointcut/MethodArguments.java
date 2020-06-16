package pl.coas.compiler.instrumentation.model.pointcut;

import java.util.List;

public interface MethodArguments {

    boolean match(List<String> arguments);
}
