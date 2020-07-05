package pl.coas.examples.monitoring.code;

import java.util.StringJoiner;

import pl.coas.api.Aspect;
import pl.coas.api.AspectClass;
import pl.coas.api.JoinPoint;
import pl.coas.api.Pointcut;
import pl.coas.examples.monitoring.Monitored;

@AspectClass
public class MonitoringAspect {

    public Object onMonitored(JoinPoint jp) {
        Aspect.on(Pointcut.annotation(Monitored.class));

        StringJoiner joiner = new StringJoiner(",");
        for (Object arg : jp.getArguments()) {
            joiner.add(arg.toString());
        }

        System.out.println(
                "[Code] Method: " + jp.getMethod() + " was invoked  on object: " + jp.getThis()
                        + " with following arguments: " + joiner.toString());

        return jp.proceed();
    }
}
