package pl.coas.api;

import java.lang.reflect.Method;

/**
 * Class representing a JoinPoint.
 * 
 * @author pmaslankowski
 */
public class JoinPoint {

    private final Object target;
    private final Object[] arguments;
    private final Method method;
    private final TargetMethod targetMethod;

    public JoinPoint(Object target, Method method, Object[] arguments, TargetMethod targetMethod) {
        this.target = target;
        this.method = method;
        this.arguments = arguments;
        this.targetMethod = targetMethod;
    }

    /**
     * Proceeds to the target method invocation.
     *
     * @return result from target method
     */
    public Object proceed() {
        return targetMethod.proceed(arguments);
    }

    /**
     * Returns the instance of the target class that join point method is being invoked on.
     * 
     * @return instance of the target class
     */
    public Object getThis() {
        return target;
    }

    public Object[] getArguments() {
        return arguments;
    }

    /**
     * Returns the {@link Method} object representing target method.
     * 
     * @return object representing target method
     */
    public Method getMethod() {
        return method;
    }

    public interface TargetMethod {

        Object proceed(Object[] args);
    }
}
