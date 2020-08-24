package pl.coco.perf.cofoja.requires;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Warmup;
import org.openjdk.jmh.infra.Blackhole;

import pl.coco.perf.RequiresSubject;

public class RequiresBenchmark {

    @Benchmark
    @Warmup(iterations = 3, time = 3)
    @Measurement(iterations = 5, time = 5)
    public void base(Blackhole hole, Input input) {
        hole.consume(input.subject.target(input.x));
    }

    @Benchmark
    @Warmup(iterations = 3, time = 3)
    @Measurement(iterations = 5, time = 5)
    public void preconditions_10(Blackhole hole, Input input) {
        hole.consume(input.subject.target10(input.x));
    }

    @Benchmark
    @Warmup(iterations = 3, time = 3)
    @Measurement(iterations = 5, time = 5)
    public void preconditions_100(Blackhole hole, Input input) {
        hole.consume(input.subject.target100(input.x));
    }

    @Benchmark
    @Warmup(iterations = 3, time = 3)
    @Measurement(iterations = 5, time = 5)
    public void preconditions_1000(Blackhole hole, Input input) {
        hole.consume(input.subject.target1000(input.x));
    }

    @State(Scope.Benchmark)
    public static class Input {

        public RequiresSubject subject = new RequiresSubject();
        public int x = -1;
    }
}
