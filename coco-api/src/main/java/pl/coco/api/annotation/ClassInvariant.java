package pl.coco.api.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation specifying class invariants.
 *
 * @author pmaslankowski
 */
@Retention(RetentionPolicy.SOURCE)
@Target(ElementType.TYPE)
@Repeatable(value = ClassInvariants.class)
public @interface ClassInvariant {

    /**
     * Java expression specifying class invariant as string.
     * 
     * @return invariant
     */
    String value();
}
