package pl.coas.compiler.instrumentation.model;

import java.util.Objects;

import com.sun.tools.javac.tree.JCTree.JCClassDecl;
import com.sun.tools.javac.tree.JCTree.JCMethodDecl;

public class Advice {

    private final JCClassDecl clazz;
    private final JCMethodDecl method;

    public Advice(JCClassDecl clazz, JCMethodDecl method) {
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
        Advice advice = (Advice) o;
        return Objects.equals(clazz, advice.clazz) &&
                Objects.equals(method, advice.method);
    }

    @Override
    public int hashCode() {
        return Objects.hash(clazz, method);
    }

    @Override
    public String toString() {
        return "Advice{" +
                "clazz=" + clazz +
                ", method=" + method +
                '}';
    }
}
