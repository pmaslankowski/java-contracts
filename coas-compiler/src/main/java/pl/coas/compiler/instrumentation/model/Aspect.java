package pl.coas.compiler.instrumentation.model;

import java.util.Objects;

import pl.coas.compiler.instrumentation.model.pointcut.Pointcut;

public class Aspect {

    private final Pointcut pointcut;
    private final Advice advice;

    public Aspect(Pointcut pointcut, Advice advice) {
        this.pointcut = pointcut;
        this.advice = advice;
    }

    public Pointcut getPointcut() {
        return pointcut;
    }

    public Advice getAdvice() {
        return advice;
    }

    public boolean matches(JoinPoint joinPoint) {
        return pointcut.matches(joinPoint);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Aspect aspect = (Aspect) o;
        return Objects.equals(pointcut, aspect.pointcut) &&
                Objects.equals(advice, aspect.advice);
    }

    @Override
    public int hashCode() {
        return Objects.hash(pointcut, advice);
    }

    @Override
    public String toString() {
        return "Aspect{" +
                "pointcut=" + pointcut +
                ", advice=" + advice +
                '}';
    }
}
