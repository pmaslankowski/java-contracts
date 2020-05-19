package pl.coco.compiler.instrumentation.registry;

import java.util.Objects;

import javax.lang.model.element.Name;

import com.sun.tools.javac.code.Type;
import com.sun.tools.javac.tree.JCTree;

import pl.coco.compiler.instrumentation.MethodInput;

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

    /*
     * There is a little hack in equals and hashCode methods: class MethodType doesn't implement
     * equals and hashCode methods but as this class is designed to be used as a key in map we would
     * like to implement correct equals and hashCode methods. Therefore instead of comparing type
     * field itself, we compare its string representation.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        MethodKey methodKey = (MethodKey) o;
        return className.equals(methodKey.className) &&
                methodName.equals(methodKey.methodName) &&
                type.toString().equals(methodKey.type.toString());
    }

    @Override
    public int hashCode() {
        return Objects.hash(className, methodName, type.toString());
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
