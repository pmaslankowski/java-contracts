package coas.perf;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Warmup;
import org.openjdk.jmh.infra.Blackhole;

import coas.perf.ComplexCondition.Subject;
import coas.perf.ComplexCondition100.Subject100;
import coas.perf.ComplexCondition150.Subject150;
import coas.perf.ComplexCondition200.Subject200;
import coas.perf.ComplexCondition250.Subject250;
import coas.perf.ComplexCondition300.Subject300;
import coas.perf.ComplexCondition50.Subject50;
import coas.perf.ComplexCondition500.Subject500;

public class ComplexConditionBenchmark {

    @Benchmark
    @Warmup(iterations = 3, time = 3)
    @Measurement(iterations = 5, time = 5)
    public void base(Blackhole hole, Input input) {
        hole.consume(input.subject.target(input.x));
    }

    @Benchmark
    @Warmup(iterations = 3, time = 3)
    @Measurement(iterations = 5, time = 5)
    public void advices_50(Blackhole hole, Input input) {
        hole.consume(input.subject50.target(input.x));
    }

    @Benchmark
    @Warmup(iterations = 3, time = 3)
    @Measurement(iterations = 5, time = 5)
    public void advices_100(Blackhole hole, Input input) {
        hole.consume(input.subject100.target(input.x));
    }

    @Benchmark
    @Warmup(iterations = 3, time = 3)
    @Measurement(iterations = 5, time = 5)
    public void advices_150(Blackhole hole, Input input) {
        hole.consume(input.subject150.target(input.x));
    }

    @Benchmark
    @Warmup(iterations = 3, time = 3)
    @Measurement(iterations = 5, time = 5)
    public void advices_200(Blackhole hole, Input input) {
        hole.consume(input.subject200.target(input.x));
    }

    @Benchmark
    @Warmup(iterations = 3, time = 3)
    @Measurement(iterations = 5, time = 5)
    public void advices_250(Blackhole hole, Input input) {
        hole.consume(input.subject250.target(input.x));
    }

    @Benchmark
    @Warmup(iterations = 3, time = 3)
    @Measurement(iterations = 5, time = 5)
    public void advices_300(Blackhole hole, Input input) {
        hole.consume(input.subject300.target(input.x));
    }

    @Benchmark
    @Warmup(iterations = 3, time = 3)
    @Measurement(iterations = 5, time = 5)
    public void advices_500(Blackhole hole, Input input) {
        hole.consume(input.subject500.target(input.x));
    }

    @State(Scope.Benchmark)
    public static class Input {

        public Subject subject = new Subject();
        public Subject50 subject50 = new Subject50();
        public Subject100 subject100 = new Subject100();
        public Subject150 subject150 = new Subject150();
        public Subject200 subject200 = new Subject200();
        public Subject250 subject250 = new Subject250();
        public Subject300 subject300 = new Subject300();
        public Subject500 subject500 = new Subject500();

        public int x = 1000;
    }
}
