package pl.coco.coco.old;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Warmup;
import org.openjdk.jmh.infra.Blackhole;

public class BenchmarkOld {

    @Benchmark
    @Warmup(iterations = 3, time = 3)
    @Measurement(iterations = 5, time = 5)
    public void base(Blackhole hole, Input input) {
        hole.consume(input.subject.target(input.x));
    }

    @Benchmark
    @Warmup(iterations = 3, time = 3)
    @Measurement(iterations = 5, time = 5)
    public void old_10(Blackhole hole, Input input) {
        hole.consume(input.subject.target_10(input.x));
    }

    @Benchmark
    @Warmup(iterations = 3, time = 3)
    @Measurement(iterations = 5, time = 5)
    public void old_100(Blackhole hole, Input input) {
        hole.consume(input.subject.target_100(input.x));
    }

    @Benchmark
    @Warmup(iterations = 3, time = 3)
    @Measurement(iterations = 5, time = 5)
    public void old_1000(Blackhole hole, Input input) {
        hole.consume(input.subject.target_1000(input.x));
    }

    @Benchmark
    @Warmup(iterations = 3, time = 3)
    @Measurement(iterations = 5, time = 5)
    public void old_5000(Blackhole hole, Input input) {
        hole.consume(input.subject.target_5000(input.x));
    }

    @State(Scope.Benchmark)
    public static class Input {

        public OldSubject subject = new OldSubject();
        public int x = -1;
    }
}
