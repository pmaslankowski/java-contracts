package pl.coas.compiler.instrumentation.model;

import java.util.Objects;

import com.sun.tools.javac.tree.JCTree.JCClassDecl;
import com.sun.tools.javac.tree.JCTree.JCMethodDecl;

public class JoinPoint {

    private final JCClassDecl clazz;
    private final JCMethodDecl method;

    public JoinPoint(JCClassDecl clazz, JCMethodDecl method) {
        this.clazz = clazz;
        this.method = method;
    }

    public JCClassDecl getClazz() {
        return clazz;
    }

    public JCMethodDecl getMethod() {
        return method;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        JoinPoint joinpoint = (JoinPoint) o;
        return Objects.equals(clazz, joinpoint.clazz) &&
                Objects.equals(method, joinpoint.method);
    }

    @Override
    public int hashCode() {
        return Objects.hash(clazz, method);
    }

    @Override
    public String toString() {
        return "Pointcut{" +
                "clazz=" + clazz +
                ", method=" + method +
                '}';
    }
}
