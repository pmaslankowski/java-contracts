package pl.coco.coco.ensures;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Warmup;
import org.openjdk.jmh.infra.Blackhole;

public class BenchmarkEnsures {

    @Benchmark
    @Warmup(iterations = 3, time = 3)
    @Measurement(iterations = 5, time = 5)
    public void base(Blackhole hole, Input input) {
        hole.consume(input.subject.target(input.x));
    }

    @Benchmark
    @Warmup(iterations = 3, time = 3)
    @Measurement(iterations = 5, time = 5)
    public void postconditions_10(Blackhole hole, Input input) {
        hole.consume(input.subject.target10(input.x));
    }

    @Benchmark
    @Warmup(iterations = 3, time = 3)
    @Measurement(iterations = 5, time = 5)
    public void postconditions_100(Blackhole hole, Input input) {
        hole.consume(input.subject.target100(input.x));
    }

    @Benchmark
    @Warmup(iterations = 3, time = 3)
    @Measurement(iterations = 5, time = 5)
    public void postconditions_500(Blackhole hole, Input input) {
        hole.consume(input.subject.target500(input.x));
    }

//    @Benchmark
//    @Warmup(iterations = 3, time = 3)
//    @Measurement(iterations = 5, time = 5)
//    public void postconditions_5000(Blackhole hole, Input input) {
//        hole.consume(input.subject.target5000(input.x));
//    }

    @State(Scope.Benchmark)
    public static class Input {

        public EnsuresSubject subject = new EnsuresSubject();
        public int x = -1;
    }
}
