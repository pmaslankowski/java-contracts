package pl.coas.api;

import java.lang.reflect.Method;

/**
 * Class representing a JoinPoint.
 * 
 * @author pmaslankowski
 */
public class JoinPoint {

    private final Object target;
    private final Method method;
    private final TargetMethod targetMethod;

    public JoinPoint(Object target, Method method, TargetMethod targetMethod) {
        this.target = target;
        this.method = method;
        this.targetMethod = targetMethod;
    }

    /**
     * Proceeds to the target method invocation.
     * 
     * @param args arguments for the target method
     * @return result from target method
     */
    public Object proceed(Object... args) throws Exception {
        return targetMethod.proceed(args);
    }

    /**
     * Returns the instance of the target class that join point method is being invoked on.
     * 
     * @return instance of the target class
     */
    public Object getThis() {
        return target;
    }

    /**
     * Returns the {@link Method} object representing target method.
     * 
     * @return object representing target method
     */
    public Method getMethod() {
        return method;
    }

    // TODO: może zwrócić optionala?
    public interface TargetMethod {

        Object proceed(Object... args) throws Exception;
    }
}
