package pl.coco.examples.annotation;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

import org.junit.jupiter.api.Test;

import pl.coco.api.ContractFailedException;

class SorterWithAnnotationsTest {

    private SorterWithAnnotations sorter = new SorterWithAnnotations();

    @Test
    void sortShouldReturnResult() {
        int[] arr = new int[] { 5, 4, 3, 2, 1 };

        int[] result = sorter.sort(arr);

        assertThat(result).containsExactly(1, 2, 3, 4, 5);
    }

    @Test
    void sortOnEmptyArrayShouldFail() {
        Throwable thrown = catchThrowable(() -> sorter.sort(new int[] {}));

        assertThat(thrown).isInstanceOf(ContractFailedException.class);

        System.out.println("Exception thrown:\n" + thrown.toString());
    }
}
