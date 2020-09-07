package coas.perf;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Warmup;
import org.openjdk.jmh.infra.Blackhole;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import coas.perf.TargetClass.Subject;
import coas.perf.TargetClass100.Subject100;
import coas.perf.TargetClass150.Subject150;
import coas.perf.TargetClass200.Subject200;
import coas.perf.TargetClass250.Subject250;
import coas.perf.TargetClass300.Subject300;
import coas.perf.TargetClass50.Subject50;
import coas.perf.TargetClass500.Subject500;

public class SimpleClassBenchmark {

    @Benchmark
    @Warmup(iterations = 1, time = 1)
    @Measurement(iterations = 5, time = 5)
    public void base(Blackhole hole, Input input) {
        hole.consume(input.subject.target(input.x));
    }

    @Benchmark
    @Warmup(iterations = 1, time = 1)
    @Measurement(iterations = 5, time = 5)
    public void advices_50(Blackhole hole, Input input) {
        hole.consume(input.subject50.target(input.x));
    }

    @Benchmark
    @Warmup(iterations = 1, time = 1)
    @Measurement(iterations = 5, time = 5)
    public void advices_100(Blackhole hole, Input input) {
        hole.consume(input.subject100.target(input.x));
    }

    @Benchmark
    @Warmup(iterations = 1, time = 1)
    @Measurement(iterations = 5, time = 5)
    public void advices_150(Blackhole hole, Input input) {
        hole.consume(input.subject150.target(input.x));
    }

    @Benchmark
    @Warmup(iterations = 1, time = 1)
    @Measurement(iterations = 5, time = 5)
    public void advices_200(Blackhole hole, Input input) {
        hole.consume(input.subject200.target(input.x));
    }

    @Benchmark
    @Warmup(iterations = 1, time = 1)
    @Measurement(iterations = 5, time = 5)
    public void advices_250(Blackhole hole, Input input) {
        hole.consume(input.subject250.target(input.x));
    }

    @Benchmark
    @Warmup(iterations = 1, time = 1)
    @Measurement(iterations = 5, time = 5)
    public void advices_300(Blackhole hole, Input input) {
        hole.consume(input.subject300.target(input.x));
    }

    @Benchmark
    @Warmup(iterations = 1, time = 1)
    @Measurement(iterations = 5, time = 5)
    public void advices_500(Blackhole hole, Input input) {
        hole.consume(input.subject500.target(input.x));
    }

    @State(Scope.Benchmark)
    public static class Input {

        public ApplicationContext ctx = new ClassPathXmlApplicationContext("user-bean-config.xml");
        public Subject subject = ctx.getBean(Subject.class);
        public Subject50 subject50 = ctx.getBean(Subject50.class);
        public Subject100 subject100 = ctx.getBean(Subject100.class);
        public Subject150 subject150 = ctx.getBean(Subject150.class);
        public Subject200 subject200 = ctx.getBean(Subject200.class);
        public Subject250 subject250 = ctx.getBean(Subject250.class);
        public Subject300 subject300 = ctx.getBean(Subject300.class);
        public Subject500 subject500 = ctx.getBean(Subject500.class);

        public int x = 1000;
    }
}
