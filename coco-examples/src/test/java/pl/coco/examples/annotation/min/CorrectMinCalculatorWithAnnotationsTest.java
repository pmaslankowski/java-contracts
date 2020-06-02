package pl.coco.examples.annotation.min;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

import org.junit.jupiter.api.Test;

import pl.coco.api.ContractFailedException;

class CorrectMinCalculatorWithAnnotationsTest {

    private MinCalculatorWithAnnotations minCalculator = new CorrectMinCalculatorWithAnnotations();

    @Test
    void minShouldReturnMinimum() {
        Collection<Integer> collection = Arrays.asList(1, 2, 3, 4);

        int min = minCalculator.min(collection);

        assertThat(min).isEqualTo(1);
    }

    @Test
    void minOnEmptyListViolatesContract() {
        Throwable thrown = catchThrowable(() -> minCalculator.min(Collections.emptyList()));

        assertThat(thrown).isInstanceOf(ContractFailedException.class);

        System.out.println("Exception thrown:\n" + thrown.toString());
    }
}
