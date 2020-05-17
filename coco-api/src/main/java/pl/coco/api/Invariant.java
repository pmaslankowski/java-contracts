package pl.coco.api;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

// TODO: add validation that there are only Contract.invariant calls inside invariant methods
/**
 * Annotation specyfying invariant method in a class.
 * <p>
 * Invariant method should be a public void method with no arguments and it can contain
 * only contract specifications. Contracts specified in invariant method are evaluated before and
 * after call to every public method in given class.
 * 
 * @author pmaslankowski
 */
@Retention(RetentionPolicy.SOURCE)
@Target(ElementType.METHOD)
public @interface Invariant {
}
