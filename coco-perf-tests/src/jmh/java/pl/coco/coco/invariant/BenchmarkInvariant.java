package pl.coco.coco.invariant;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Warmup;
import org.openjdk.jmh.infra.Blackhole;

public class BenchmarkInvariant {

    @Benchmark
    @Warmup(iterations = 1, time = 1)
    @Measurement(iterations = 3, time = 3)
    public void base(Blackhole hole, Input input) {
        hole.consume(input.subject.target(input.x));
    }

    @Benchmark
    @Warmup(iterations = 1, time = 1)
    @Measurement(iterations = 3, time = 3)
    public void invariant_10(Blackhole hole, Input input) {
        hole.consume(input.subject10.target(input.x));
    }

    @Benchmark
    @Warmup(iterations = 1, time = 1)
    @Measurement(iterations = 3, time = 3)
    public void invariant_100(Blackhole hole, Input input) {
        hole.consume(input.subject100.target(input.x));
    }

    @Benchmark
    @Warmup(iterations = 1, time = 1)
    @Measurement(iterations = 3, time = 3)
    public void invariant_1000(Blackhole hole, Input input) {
        hole.consume(input.subject1000.target(input.x));
    }

    @State(Scope.Benchmark)
    public static class Input {

        public InvariantSubject subject = new InvariantSubject();
        public InvariantSubject10 subject10 = new InvariantSubject10();
        public InvariantSubject100 subject100 = new InvariantSubject100();
        public InvariantSubject1000 subject1000 = new InvariantSubject1000();
        public int x = -1;
    }
}
