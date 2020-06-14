package pl.coas.compiler.instrumentation.model.pointcut;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class MethodPointcut implements Pointcut {

    private final MethodKind kind;
    private final List<MethodModifier> modifiers;
    private final WildcardString returnType;
    private final WildcardString className;
    private final WildcardString methodName;
    private final MethodArguments argumentTypes;
    private final List<WildcardString> exceptionsThrown;

    public MethodPointcut(MethodKind kind, List<MethodModifier> modifiers,
            WildcardString returnType, WildcardString className, WildcardString methodName,
            MethodArguments argumentTypes, List<WildcardString> exceptionsThrown) {
        this.kind = kind;
        this.modifiers = modifiers;
        this.returnType = returnType;
        this.className = className;
        this.methodName = methodName;
        this.argumentTypes = argumentTypes;
        this.exceptionsThrown = exceptionsThrown;
    }

    public MethodKind getKind() {
        return kind;
    }

    public List<MethodModifier> getModifiers() {
        return modifiers;
    }

    public WildcardString getReturnType() {
        return returnType;
    }

    public WildcardString getClassName() {
        return className;
    }

    public WildcardString getMethodName() {
        return methodName;
    }

    public MethodArguments getArgumentTypes() {
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
        return kind == that.kind &&
                Objects.equals(modifiers, that.modifiers) &&
                Objects.equals(returnType, that.returnType) &&
                Objects.equals(className, that.className) &&
                Objects.equals(methodName, that.methodName) &&
                Objects.equals(argumentTypes, that.argumentTypes) &&
                Objects.equals(exceptionsThrown, that.exceptionsThrown);
    }

    @Override
    public int hashCode() {
        return Objects.hash(kind, modifiers, returnType, className, methodName, argumentTypes,
                exceptionsThrown);
    }

    @Override
    public String toString() {
        return "MethodPointcut{" +
                "kind=" + kind +
                ", modifiers=" + modifiers +
                ", returnType=" + returnType +
                ", className=" + className +
                ", methodName=" + methodName +
                ", argumentTypes=" + argumentTypes +
                ", exceptionsThrown=" + exceptionsThrown +
                '}';
    }

    public static class Builder {

        private MethodKind kind;
        private List<MethodModifier> modifiers;
        private WildcardString returnType;
        private WildcardString className;
        private WildcardString methodName;
        private MethodArguments argumentTypes;
        private List<WildcardString> exceptionsThrown;

        public Builder withKind(String kind) {
            this.kind = MethodKind.of(kind);
            return this;
        }

        public Builder withModifiers(List<String> modifiers) {
            this.modifiers = modifiers.stream()
                    .map(MethodModifier::of)
                    .collect(Collectors.toList());
            return this;
        }

        public Builder withReturnType(String returnType) {
            this.returnType = new WildcardString(returnType);
            return this;
        }

        public Builder withClassName(String className) {
            this.className = new WildcardString(className);
            return this;
        }

        public Builder withMethodName(String methodName) {
            this.methodName = new WildcardString(methodName);
            return this;
        }

        public Builder withArgumentTypes(MethodArguments argumentTypes) {
            this.argumentTypes = argumentTypes;
            return this;
        }

        public Builder withExceptionsThrown(List<String> exceptionsThrown) {
            this.exceptionsThrown = exceptionsThrown.stream()
                    .map(WildcardString::new)
                    .collect(Collectors.toList());
            return this;
        }

        public MethodPointcut build() {
            return new MethodPointcut(kind, modifiers, returnType, className, methodName,
                    argumentTypes, exceptionsThrown);
        }
    }
}
