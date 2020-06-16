package pl.coas.compiler.instrumentation.model.pointcut;

import java.util.Objects;

import com.sun.tools.javac.code.Type.ClassType;
import com.sun.tools.javac.tree.JCTree.JCClassDecl;

import pl.coas.compiler.instrumentation.model.JoinPoint;

public class TargetPointcut implements Pointcut {

    private static final String JAVA_LANG_OBJECT = "java.lang.Object";

    private final WildcardString className;

    public TargetPointcut(String className) {
        this.className = new WildcardString(className);
    }

    @Override
    public boolean matches(JoinPoint joinPoint) {
        return anySuperclassMatches(joinPoint.getClazz());
    }

    private boolean anySuperclassMatches(JCClassDecl clazz) {
        ClassType classType = (ClassType) clazz.type;
        String jpClassName = getClassName(classType);

        while (!jpClassName.equals(JAVA_LANG_OBJECT)) {
            if (className.matches(jpClassName)) {
                return true;
            }
            classType = (ClassType) classType.supertype_field;
            jpClassName = getClassName(classType);
        }

        return className.matches(jpClassName);
    }

    private String getClassName(ClassType classType) {
        return classType.tsym.getQualifiedName().toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        TargetPointcut that = (TargetPointcut) o;
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
