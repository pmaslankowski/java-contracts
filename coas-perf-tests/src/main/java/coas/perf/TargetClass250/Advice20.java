
package coas.perf.TargetClass250;

import pl.coas.api.Aspect;
import pl.coas.api.AspectClass;
import pl.coas.api.JoinPoint;
import pl.coas.api.Pointcut;

import coas.perf.TargetClass250.Subject250;

@AspectClass
public class Advice20 {

    private int counter = 0;

    public Object onTarget(JoinPoint jp) {
        Aspect.on(Pointcut.targetClass(Subject250.class));
        counter++;

        return jp.proceed();
    }
}
