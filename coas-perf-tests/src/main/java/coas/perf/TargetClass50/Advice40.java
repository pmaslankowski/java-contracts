
package coas.perf.TargetClass50;

import pl.coas.api.Aspect;
import pl.coas.api.AspectClass;
import pl.coas.api.JoinPoint;
import pl.coas.api.Pointcut;

import coas.perf.TargetClass50.Subject50;

@AspectClass
public class Advice40 {

    private int counter = 0;

    public Object onTarget(JoinPoint jp) {
        Aspect.on(Pointcut.targetClass(Subject50.class));
        counter++;

        return jp.proceed();
    }
}
