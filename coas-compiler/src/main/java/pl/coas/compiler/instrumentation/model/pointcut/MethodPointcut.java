package pl.coas.compiler.instrumentation.model.pointcut;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class MethodPointcut implements Pointcut {

    private final WildcardString className;
    private final WildcardString methodName;
    private final WildcardString returnType;
    private final List<WildcardString> argumentTypes;
    private final List<WildcardString> exceptionsThrown;

    private MethodPointcut(WildcardString className, WildcardString methodName,
            WildcardString returnType,
            List<WildcardString> argumentTypes, List<WildcardString> exceptionsThrown) {
        this.className = className;
        this.methodName = methodName;
        this.returnType = returnType;
        this.argumentTypes = argumentTypes;
        this.exceptionsThrown = exceptionsThrown;
    }

    public WildcardString getClassName() {
        return className;
    }

    public WildcardString getMethodName() {
        return methodName;
    }

    public WildcardString getReturnType() {
        return returnType;
    }

    public List<WildcardString> getArgumentTypes() {
        return argumentTypes;
    }

    public List<WildcardString> getExceptionsThrown() {
        return exceptionsThrown;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        MethodPointcut that = (MethodPointcut) o;
        return Objects.equals(className, that.className) &&
                Objects.equals(methodName, that.methodName) &&
                Objects.equals(returnType, that.returnType) &&
                Objects.equals(argumentTypes, that.argumentTypes) &&
                Objects.equals(exceptionsThrown, that.exceptionsThrown);
    }

    @Override
    public int hashCode() {
        return Objects.hash(className, methodName, returnType, argumentTypes, exceptionsThrown);
    }

    @Override
    public String toString() {
        return "MethodPointcut{" +
                "className=" + className +
                ", methodName=" + methodName +
                ", returnType=" + returnType +
                ", argumentTypes=" + argumentTypes +
                ", exceptionsThrown=" + exceptionsThrown +
                '}';
    }

    public static class Builder {

        private WildcardString className;
        private WildcardString methodName;
        private WildcardString returnType;
        private List<WildcardString> argumentTypes;
        private List<WildcardString> exceptionsThrown;

        public Builder withClassName(String className) {
            this.className = new WildcardString(className);
            return this;
        }

        public Builder withMethodName(String methodName) {
            this.methodName = new WildcardString(methodName);
            return this;
        }

        public Builder withReturnType(String returnType) {
            this.returnType = new WildcardString(returnType);
            return this;
        }

        public Builder withArgumentTypes(List<String> argumentTypes) {
            this.argumentTypes = argumentTypes.stream()
                    .map(WildcardString::new)
                    .collect(Collectors.toList());
            return this;
        }

        public Builder withExceptionsThrown(List<String> exceptionsThrown) {
            this.exceptionsThrown = exceptionsThrown.stream()
                    .map(WildcardString::new)
                    .collect(Collectors.toList());
            return this;
        }

        public MethodPointcut build() {
            return new MethodPointcut(className, methodName, returnType, argumentTypes,
                    exceptionsThrown);
        }
    }
}
