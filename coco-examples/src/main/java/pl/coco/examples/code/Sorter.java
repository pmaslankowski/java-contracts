package pl.coco.examples.code;

import java.util.Arrays;

import pl.coco.api.code.Contract;

public class Sorter {

    /**
     * Returns sorted non-empty array.
     * 
     * @param arr array to sort
     * @return sorted array
     */
    public int[] sort(int[] arr) {
        /* Contracts */
        Contract.requires(arr.length > 0);
        Contract.ensures(Contract.forAll(Contract.range(arr.length),
                (i, j) -> Contract.implies(
                        i < j,
                        Contract.result(int[].class)[i] <= Contract.result(int[].class)[j])));
        /* End of contracts */

        int[] result = arr.clone();
        Arrays.sort(result);
        return result;
    }
}
