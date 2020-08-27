
package coas.perf.TargetClass200;

import pl.coas.api.Aspect;
import pl.coas.api.AspectClass;
import pl.coas.api.JoinPoint;
import pl.coas.api.Pointcut;

import coas.perf.TargetClass200.Subject200;

@AspectClass
public class Advice102 {

    private int counter = 0;

    public Object onTarget(JoinPoint jp) {
        Aspect.on(Pointcut.targetClass(Subject200.class));
        counter++;

        return jp.proceed();
    }
}
