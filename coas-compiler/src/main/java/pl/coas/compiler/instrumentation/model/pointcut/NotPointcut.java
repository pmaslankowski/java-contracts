package pl.coas.compiler.instrumentation.model.pointcut;

import java.util.Objects;

import pl.coas.compiler.instrumentation.model.JoinPoint;

public class NotPointcut implements Pointcut {

    private final Pointcut operand;

    public NotPointcut(Pointcut operand) {
        this.operand = operand;
    }

    @Override
    public boolean matches(JoinPoint joinPoint) {
        return !operand.matches(joinPoint);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        NotPointcut that = (NotPointcut) o;
        return Objects.equals(operand, that.operand);
    }

    @Override
    public int hashCode() {
        return Objects.hash(operand);
    }

    @Override
    public String toString() {
        return "NotPointcut{" +
                "operand=" + operand +
                '}';
    }
}
