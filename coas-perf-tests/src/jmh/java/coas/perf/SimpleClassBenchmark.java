package coas.perf;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Warmup;
import org.openjdk.jmh.infra.Blackhole;

import coas.perf.TargetClass10.Subject10;

public class SimpleClassBenchmark {

    @Benchmark
    @Warmup(iterations = 3, time = 3)
    @Measurement(iterations = 5, time = 5)
    public void base(Blackhole hole, Input input) {
        hole.consume(input.subject10.target(input.x));
    }

//    @Benchmark
//    @Warmup(iterations = 3, time = 3)
//    @Measurement(iterations = 5, time = 5)
//    public void postconditions_10(Blackhole hole, Input input) {
//        hole.consume(input.subject10.target10(input.x));
//    }

//    @Benchmark
//    @Warmup(iterations = 3, time = 3)
//    @Measurement(iterations = 5, time = 5)
//    public void postconditions_100(Blackhole hole, Input input) {
//        hole.consume(input.subject.target100(input.x));
//    }

//    @Benchmark
//    @Warmup(iterations = 3, time = 3)
//    @Measurement(iterations = 5, time = 5)
//    public void postconditions_500(Blackhole hole, Input input) {
//        hole.consume(input.subject10.target500(input.x));
//    }

//    @Benchmark
//    @Warmup(iterations = 3, time = 3)
//    @Measurement(iterations = 5, time = 5)
//    public void postconditions_5000(Blackhole hole, Input input) {
//        hole.consume(input.subject.target5000(input.x));
//    }

    @State(Scope.Benchmark)
    public static class Input {

        public Subject10 subject10 = new Subject10();
        public int x = 1000;
    }
}
