package pl.coco.examples.annotation.min;

import java.util.Collection;

import pl.coco.api.annotation.Ensures;
import pl.coco.api.annotation.Requires;

public class MinCalculatorWithAnnotations {

    /**
     * Finds minimum in given non-empty collection of integers.
     *
     * @param collection collection
     * @return smallest number from given collection
     */
    @Requires("!collection.isEmpty()")
    @Ensures("Contract.exists(collection, i -> Contract.result(Integer.class).equals(i))")
    @Ensures("Contract.forAll(collection, i -> Contract.result(Integer.class) <= i)")
    public int min(Collection<Integer> collection) {
        // we have to return something, but we should think of this method as an abstract method
        return 0;
    }
}
