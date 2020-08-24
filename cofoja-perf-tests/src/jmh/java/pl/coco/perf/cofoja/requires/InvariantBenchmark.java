package pl.coco.perf.cofoja.requires;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Warmup;
import org.openjdk.jmh.infra.Blackhole;

import pl.coco.perf.InvariantSubject;
import pl.coco.perf.InvariantSubject10;
import pl.coco.perf.InvariantSubject100;
import pl.coco.perf.InvariantSubject500;

public class InvariantBenchmark {

    @Benchmark
    @Warmup(iterations = 1, time = 1)
    @Measurement(iterations = 3, time = 3)
    public void base(Blackhole hole, Input input) {
        hole.consume(input.subject.target(input.x));
    }

    @Benchmark
    @Warmup(iterations = 1, time = 1)
    @Measurement(iterations = 3, time = 3)
    public void invariants_10(Blackhole hole, Input input) {
        hole.consume(input.subject10.target(input.x));
    }

    @Benchmark
    @Warmup(iterations = 1, time = 1)
    @Measurement(iterations = 3, time = 3)
    public void invariants_100(Blackhole hole, Input input) {
        hole.consume(input.subject100.target(input.x));
    }

    @Benchmark
    @Warmup(iterations = 1, time = 1)
    @Measurement(iterations = 3, time = 3)
    public void invariants_500(Blackhole hole, Input input) {
        hole.consume(input.subject500.target(input.x));
    }

    @State(Scope.Benchmark)
    public static class Input {

        public InvariantSubject subject = new InvariantSubject();
        public InvariantSubject10 subject10 = new InvariantSubject10();
        public InvariantSubject100 subject100 = new InvariantSubject100();
        public InvariantSubject500 subject500 = new InvariantSubject500();

        public int x = -1;
    }
}
