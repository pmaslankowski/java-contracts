package pl.coco.coco.result;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Warmup;
import org.openjdk.jmh.infra.Blackhole;

public class BenchmarkResult {

    @Benchmark
    @Warmup(iterations = 1, time = 1)
    @Measurement(iterations = 3, time = 3)
    public void base(Blackhole hole, Input input) {
        hole.consume(input.subject.target(input.x));
    }

    @Benchmark
    @Warmup(iterations = 1, time = 1)
    @Measurement(iterations = 3, time = 3)
    public void result_10(Blackhole hole, Input input) {
        hole.consume(input.subject.target10(input.x));
    }

    @Benchmark
    @Warmup(iterations = 1, time = 1)
    @Measurement(iterations = 3, time = 3)
    public void result_100(Blackhole hole, Input input) {
        hole.consume(input.subject.target100(input.x));
    }

    @Benchmark
    @Warmup(iterations = 1, time = 1)
    @Measurement(iterations = 3, time = 3)
    public void result_1000(Blackhole hole, Input input) {
        hole.consume(input.subject.target1000(input.x));
    }

    @Benchmark
    @Warmup(iterations = 1, time = 1)
    @Measurement(iterations = 3, time = 3)
    public void result_5000(Blackhole hole, Input input) {
        hole.consume(input.subject.target5000(input.x));
    }

    @State(Scope.Benchmark)
    public static class Input {

        public ResultSubject subject = new ResultSubject();
        public int x = -1;
    }
}
