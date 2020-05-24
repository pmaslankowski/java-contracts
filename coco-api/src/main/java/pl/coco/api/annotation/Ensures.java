package pl.coco.api.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation specifying method postcondition.
 * 
 * @author pmaslankowski
 */
@Retention(RetentionPolicy.SOURCE)
@Target(ElementType.METHOD)
@Repeatable(value = Postconditions.class)
public @interface Ensures {

    /**
     * Java expression specifying method postcondition as string.
     * 
     * @return postcondition
     */
    String value();
}
