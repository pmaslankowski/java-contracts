package pl.coco.api.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation specifying method precondition.
 *
 * @author pmaslankowski
 */
@Retention(RetentionPolicy.SOURCE)
@Target(ElementType.METHOD)
@Repeatable(value = Preconditions.class)
public @interface Requires {

    /**
     * Java expression specifying method precondition as string.
     * 
     * @return precondition
     */
    String value();
}
