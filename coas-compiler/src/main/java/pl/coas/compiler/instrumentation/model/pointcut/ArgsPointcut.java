package pl.coas.compiler.instrumentation.model.pointcut;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import com.sun.tools.javac.tree.JCTree.JCVariableDecl;

import pl.coas.compiler.instrumentation.model.JoinPoint;

public class ArgsPointcut implements Pointcut {

    private final MethodArguments argTypes;

    public ArgsPointcut(MethodArguments argTypes) {
        this.argTypes = argTypes;
    }

    @Override
    public boolean matches(JoinPoint joinPoint) {
        List<String> jpArgTypes = joinPoint.getMethod().getParameters().stream()
                .map(this::getParameterTypeName)
                .collect(Collectors.toList());

        return argTypes.match(jpArgTypes);
    }

    private String getParameterTypeName(JCVariableDecl arg) {
        return arg.type.tsym.getQualifiedName().toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        ArgsPointcut that = (ArgsPointcut) o;
        return Objects.equals(argTypes, that.argTypes);
    }

    @Override
    public int hashCode() {
        return Objects.hash(argTypes);
    }

    @Override
    public String toString() {
        return "ArgsPointcut{" +
                "args=" + argTypes +
                '}';
    }
}
