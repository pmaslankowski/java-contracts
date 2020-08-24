package pl.coco.coco.mixed;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Warmup;
import org.openjdk.jmh.infra.Blackhole;

public class MixedBenchmark {

    @Benchmark
    @Warmup(iterations = 3, time = 3)
    @Measurement(iterations = 5, time = 5)
    public void base(Blackhole hole, Input input) {
        hole.consume(input.subject.target(input.x));
    }

    @Benchmark
    @Warmup(iterations = 3, time = 3)
    @Measurement(iterations = 5, time = 5)
    public void mixed_10(Blackhole hole, Input input) {
        hole.consume(input.subject.target_10(input.x));
    }

    @Benchmark
    @Warmup(iterations = 3, time = 3)
    @Measurement(iterations = 5, time = 5)
    public void mixed_100(Blackhole hole, Input input) {
        hole.consume(input.subject.target_100(input.x));
    }

    @State(Scope.Benchmark)
    public static class Input {

        public MixedSubject subject = new MixedSubject();
        public int x = -1;
    }

}
