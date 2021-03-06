package pl.coco.perf.cofoja.requires;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Warmup;
import org.openjdk.jmh.infra.Blackhole;

import pl.coco.perf.EnsuresSubject;

public class EnsuresBenchmark {

    @Benchmark
    @Warmup(iterations = 1, time = 1)
    @Measurement(iterations = 3, time = 3)
    public void base(Blackhole hole, Input input) {
        hole.consume(input.subject.target(input.x));
    }

    @Benchmark
    @Warmup(iterations = 1, time = 1)
    @Measurement(iterations = 3, time = 3)
    public void postconditions_10(Blackhole hole, Input input) {
        hole.consume(input.subject.target10(input.x));
    }

    @Benchmark
    @Warmup(iterations = 1, time = 1)
    @Measurement(iterations = 3, time = 3)
    public void postconditions_100(Blackhole hole, Input input) {
        hole.consume(input.subject.target100(input.x));
    }

    @Benchmark
    @Warmup(iterations = 1, time = 1)
    @Measurement(iterations = 3, time = 3)
    public void postconditions_500(Blackhole hole, Input input) {
        hole.consume(input.subject.target500(input.x));
    }

    @State(Scope.Benchmark)
    public static class Input {

        public EnsuresSubject subject = new EnsuresSubject();
        public int x = -1;
    }

}
