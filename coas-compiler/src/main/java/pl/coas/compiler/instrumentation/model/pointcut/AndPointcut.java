package pl.coas.compiler.instrumentation.model.pointcut;

import java.util.Objects;

import pl.coas.compiler.instrumentation.model.JoinPoint;

public class AndPointcut implements Pointcut {

    private final Pointcut leftOperand;
    private final Pointcut rightOperand;

    public AndPointcut(Pointcut leftOperand, Pointcut rightOperand) {
        this.leftOperand = leftOperand;
        this.rightOperand = rightOperand;
    }

    @Override
    public boolean matches(JoinPoint joinPoint) {
        return leftOperand.matches(joinPoint) && rightOperand.matches(joinPoint);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        AndPointcut that = (AndPointcut) o;
        return Objects.equals(leftOperand, that.leftOperand) &&
                Objects.equals(rightOperand, that.rightOperand);
    }

    @Override
    public int hashCode() {
        return Objects.hash(leftOperand, rightOperand);
    }

    @Override
    public String toString() {
        return "AndPointcut{" +
                "leftOperand=" + leftOperand +
                ", rightOperand=" + rightOperand +
                '}';
    }
}
