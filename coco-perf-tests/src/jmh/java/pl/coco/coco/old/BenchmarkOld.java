package pl.coco.coco.old;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Warmup;
import org.openjdk.jmh.infra.Blackhole;

public class BenchmarkOld {

    @Benchmark
    @Warmup(iterations = 1, time = 1)
    @Measurement(iterations = 3, time = 3)
    public void base(Blackhole hole, Input input) {
        hole.consume(input.subject.target(input.x));
    }

    @Benchmark
    @Warmup(iterations = 1, time = 1)
    @Measurement(iterations = 3, time = 3)
    public void old_10(Blackhole hole, Input input) {
        hole.consume(input.subject.target_10(input.x));
    }

    @Benchmark
    @Warmup(iterations = 1, time = 1)
    @Measurement(iterations = 3, time = 3)
    public void old_100(Blackhole hole, Input input) {
        hole.consume(input.subject.target_100(input.x));
    }

    @State(Scope.Benchmark)
    public static class Input {

        public OldSubject subject = new OldSubject();
        public int x = -1;
    }
}
