package pl.coco.examples.code;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

import org.apache.commons.lang3.reflect.FieldUtils;
import org.junit.jupiter.api.Test;

import pl.coco.api.ContractFailedException;

class StackTest {

    @Test
    void popOnEmptyStackShouldThrowContractFailedException() {
        Stack stack = new Stack();

        Throwable thrown = catchThrowable(stack::pop);

        assertThat(thrown).isInstanceOf(ContractFailedException.class);

        System.out.println("Exception thrown:\n" + thrown.toString());
    }

    @Test
    void peekOnEmptyStackShouldThrowContractFailedException() {
        Stack stack = new Stack();

        Throwable thrown = catchThrowable(stack::peek);

        assertThat(thrown).isInstanceOf(ContractFailedException.class);

        System.out.println("Exception thrown:\n" + thrown.toString());
    }

    @Test
    void pushShouldEnsureThatStackIsNotEmpty() {
        Stack stack = new Stack();

        stack.push(1);

        int actual = stack.pop();

        assertThat(actual).isEqualTo(1);
    }

    @Test
    void shouldThrowContractFailedExceptionWhenInvariantFails() throws IllegalAccessException {
        Stack stack = new Stack();

        stack.push(1);

        // we break one of stack invariants intentionally:
        FieldUtils.writeField(stack, "top", -2, true);

        Throwable thrown = catchThrowable(stack::pop);

        assertThat(thrown).isInstanceOf(ContractFailedException.class);

        System.out.println("Exception thrown:\n" + thrown.toString());
    }
}
