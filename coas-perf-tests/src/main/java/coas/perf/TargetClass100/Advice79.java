
package coas.perf.TargetClass100;

import pl.coas.api.Aspect;
import pl.coas.api.AspectClass;
import pl.coas.api.JoinPoint;
import pl.coas.api.Pointcut;

import coas.perf.TargetClass100.Subject100;

@AspectClass
public class Advice79 {

    private int counter = 0;

    public Object onTarget(JoinPoint jp) {
        Aspect.on(Pointcut.targetClass(Subject100.class));
        counter++;

        return jp.proceed();
    }
}
