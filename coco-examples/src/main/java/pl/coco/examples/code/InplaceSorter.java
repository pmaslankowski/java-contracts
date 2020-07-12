package pl.coco.examples.code;

import java.util.Arrays;

import pl.coco.api.code.Contract;

public class InplaceSorter {

    public void sort(int[] arr) {
        /* Contracts */
        Contract.ensures(Contract.forAll(Contract.range(arr.length),
                i -> i == arr.length - 1 || arr[i] <= arr[i + 1]));
        /* End of contracts */

        Arrays.sort(arr);
    }
}
