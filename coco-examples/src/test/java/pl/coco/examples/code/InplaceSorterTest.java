package pl.coco.examples.code;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class InplaceSorterTest {

    private InplaceSorter sorter = new InplaceSorter();

    @Test
    void sortShouldReturnResult() {
        int[] arr = new int[] { 5, 4, 3, 2, 1 };

        sorter.sort(arr);

        assertThat(arr).containsExactly(1, 2, 3, 4, 5);
    }
}
