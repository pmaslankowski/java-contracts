package pl.coco.examples.annotation.min;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

import java.util.Arrays;
import java.util.Collection;

import org.junit.jupiter.api.Test;

import pl.coco.api.ContractFailedException;

class IncorrectMinCalculatorWithAnnotationsTest {

    private MinCalculatorWithAnnotations minCalculator =
            new IncorrectMinCalculatorWithAnnotations();

    @Test
    void shouldThrowContractFailedException() {
        Collection<Integer> collection = Arrays.asList(1, 2, 3, 4);

        Throwable thrown = catchThrowable(() -> minCalculator.min(collection));

        assertThat(thrown).isInstanceOf(ContractFailedException.class);

        System.out.println("Exception thrown:\n" + thrown.toString());
    }

}
