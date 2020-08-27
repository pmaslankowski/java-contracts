
package coas.perf.TargetClass500;

import pl.coas.api.Aspect;
import pl.coas.api.AspectClass;
import pl.coas.api.JoinPoint;
import pl.coas.api.Pointcut;

import coas.perf.TargetClass500.Subject500;

@AspectClass
public class Advice82 {

    private int counter = 0;

    public Object onTarget(JoinPoint jp) {
        Aspect.on(Pointcut.targetClass(Subject500.class));
        counter++;

        return jp.proceed();
    }
}
