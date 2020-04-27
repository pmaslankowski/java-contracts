package pl.coco.util;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.MessageFormat;

public class TestRunner {

    public Object run(CompiledClasses classes, String qualifiedClassName, String methodName,
            Class<?>[] argumentTypes, Object... args) throws Throwable {

        Class<?> clazz = loadClass(classes, qualifiedClassName);
        Method method = getMethod(clazz, methodName, argumentTypes);

        return invokeMethodWithArguments(method, args);
    }

    private Class<?> loadClass(CompiledClasses classes, String qualifiedClassName) {
        ClassLoader classLoader = new ClassLoader() {

            @Override
            protected Class<?> findClass(String name) {
                byte[] byteCode = classes.getCompiledClass(name);
                return defineClass(name, byteCode, 0, byteCode.length);
            }
        };

        return doLoadClass(qualifiedClassName, classLoader);
    }

    private Class<?> doLoadClass(String qualifiedClassName, ClassLoader classLoader) {
        try {
            return classLoader.loadClass(qualifiedClassName);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Can't load compiled test class", e);
        }
    }

    private Method getMethod(Class<?> clazz, String methodName, Class<?>[] argumentTypes) {
        try {
            return clazz.getMethod(methodName, argumentTypes);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(MessageFormat.format(
                    "Can't find the {0} method in the compiled test class", methodName),
                    e);
        }
    }

    private Object invokeMethodWithArguments(Method method, Object[] args) throws Throwable {
        try {
            return method.invoke(null, args);
        } catch (InvocationTargetException e) {
            throw e.getCause();
        }
    }
}
