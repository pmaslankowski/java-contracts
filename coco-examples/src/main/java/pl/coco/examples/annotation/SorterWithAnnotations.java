package pl.coco.examples.annotation;

import java.util.Arrays;

import pl.coco.api.annotation.Ensures;
import pl.coco.api.annotation.Requires;

public class SorterWithAnnotations {

    /**
     * Returns sorted non-empty array.
     *
     * @param arr array to sort
     * @return sorted array
     */
    @Requires("arr.length > 0")
    @Ensures("Contract.forAll(Contract.range(arr.length), " +
            "(i, j) -> Contract.implies(i < j, " +
            "Contract.result(int[].class)[i] <= Contract.result(int[].class)[j])))")
    public int[] sort(int[] arr) {
        int[] result = arr.clone();
        Arrays.sort(result);
        return result;
    }
}
