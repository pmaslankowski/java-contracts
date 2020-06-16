package pl.coas.compiler.instrumentation.model.pointcut;

import pl.coas.compiler.instrumentation.model.JoinPoint;

public interface Pointcut {

    boolean matches(JoinPoint joinPoint);
}
