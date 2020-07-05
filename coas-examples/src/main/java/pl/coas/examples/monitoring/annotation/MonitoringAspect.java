package pl.coas.examples.monitoring.annotation;

import java.util.StringJoiner;

import pl.coas.api.AspectClass;
import pl.coas.api.JoinPoint;
import pl.coas.api.annotation.Advice;

@AspectClass
public class MonitoringAspect {

    @Advice(on = "annotation(pl.coas.examples.monitoring.Monitored)", order = 1)
    public Object onMonitored(JoinPoint jp) {
        StringJoiner joiner = new StringJoiner(",");
        for (Object arg : jp.getArguments()) {
            joiner.add(arg.toString());
        }

        System.out.println(
                "[Annotation] Method: " + jp.getMethod() + " was invoked  on object: "
                        + jp.getThis()
                        + " with following arguments: " + joiner.toString());

        return jp.proceed();
    }
}
