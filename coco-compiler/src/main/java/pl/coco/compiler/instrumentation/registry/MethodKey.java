package pl.coco.compiler.instrumentation.registry;

import java.util.Objects;

import javax.lang.model.element.Name;

import pl.coco.compiler.instrumentation.MethodInput;

import com.sun.tools.javac.code.Type;
import com.sun.tools.javac.tree.JCTree;

public class MethodKey {

    private final Name className;
    private final Name methodName;
    private final Type type;

    public MethodKey(Name className, Name methodName, Type type) {
        this.className = className;
        this.methodName = methodName;
        this.type = type;
    }

    public static MethodKey of(MethodInput input) {
        JCTree.JCMethodDecl method = (JCTree.JCMethodDecl) input.getMethod();
        JCTree.JCClassDecl clazz = (JCTree.JCClassDecl) input.getClazz();

        Name className = clazz.sym.getQualifiedName();
        Name methodName = method.getName();
        Type methodType = method.type;

        return new MethodKey(className, methodName, methodType);
    }

    public Name getClassName() {
        return className;
    }

    public Name getMethodName() {
        return methodName;
    }

    public Type getType() {
        return type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        MethodKey methodKey = (MethodKey) o;
        return className.equals(methodKey.className) &&
                methodName.equals(methodKey.methodName) &&
                type.equals(methodKey.type);
    }

    @Override
    public int hashCode() {
        return Objects.hash(className, methodName, type);
    }

    @Override
    public String toString() {
        return "MethodKey{" +
                "className=" + className +
                ", methodName=" + methodName +
                ", type=" + type +
                '}';
    }
}
