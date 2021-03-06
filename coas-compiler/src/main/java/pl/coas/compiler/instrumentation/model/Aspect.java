package pl.coas.compiler.instrumentation.model;

import java.util.Objects;

import pl.coas.api.AspectType;
import pl.coas.compiler.instrumentation.model.pointcut.Pointcut;

public class Aspect {

    private final Pointcut pointcut;
    private final Advice advice;
    private final int order;
    private final AspectType type;

    public Aspect(Pointcut pointcut, Advice advice, int order, AspectType type) {
        this.pointcut = pointcut;
        this.advice = advice;
        this.order = order;
        this.type = type;
    }

    public Pointcut getPointcut() {
        return pointcut;
    }

    public Advice getAdvice() {
        return advice;
    }

    public int getOrder() {
        return order;
    }

    public AspectType getType() {
        return type;
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
        return order == aspect.order &&
                Objects.equals(pointcut, aspect.pointcut) &&
                Objects.equals(advice, aspect.advice) &&
                type == aspect.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(pointcut, advice, order, type);
    }

    @Override
    public String toString() {
        return "Aspect{" +
                "pointcut=" + pointcut +
                ", advice=" + advice +
                ", order=" + order +
                ", type=" + type +
                '}';
    }
}
