package pl.coas.compiler.instrumentation.model.pointcut;

import java.util.Objects;

public class DynamicTargetPointcut implements Pointcut {

    private final WildcardString className;

    public DynamicTargetPointcut(String className) {
        this.className = new WildcardString(className);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        DynamicTargetPointcut that = (DynamicTargetPointcut) o;
        return Objects.equals(className, that.className);
    }

    @Override
    public int hashCode() {
        return Objects.hash(className);
    }

    @Override
    public String toString() {
        return "DynamicTargetPointcut{" +
                "className=" + className +
                '}';
    }
}
