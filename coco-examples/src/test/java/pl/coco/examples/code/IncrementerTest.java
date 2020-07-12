package pl.coco.examples.code;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.concurrent.atomic.AtomicInteger;

import org.junit.jupiter.api.Test;

class IncrementerTest {

    private Incrementer incrementer = new Incrementer();

    @Test
    void shouldIncrement() {
        AtomicInteger x = new AtomicInteger(0);
        incrementer.increment(x);
        assertThat(x.get()).isEqualTo(1);
    }

}
