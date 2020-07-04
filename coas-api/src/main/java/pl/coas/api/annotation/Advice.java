package pl.coas.api.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import pl.coas.api.Aspect;
import pl.coas.api.AspectType;

/**
 * Marks given method as an aspect advice. This annotation corresponds to {@link Aspect} class.
 * 
 * @author pmaslankowski
 */
@Retention(RetentionPolicy.SOURCE)
@Target(ElementType.METHOD)
public @interface Advice {

    /**
     * Specifies joinpoint as a joinpoint expression
     * 
     * @return joinpoint expression
     */
    String on();

    /**
     * Specifies advice order
     * 
     * @return order
     */
    int order() default 0;

    /**
     * Specifies advice type
     * 
     * @return advice type
     */
    AspectType type() default AspectType.SINGLETON;
}
