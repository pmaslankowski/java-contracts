
package coas.perf.TargetClass300;

import pl.coas.api.Aspect;
import pl.coas.api.AspectClass;
import pl.coas.api.JoinPoint;
import pl.coas.api.Pointcut;

import coas.perf.TargetClass300.Subject300;

@AspectClass
public class Advice278 {

    private int counter = 0;

    public Object onTarget(JoinPoint jp) {
        Aspect.on(Pointcut.targetClass(Subject300.class));
        counter++;

        return jp.proceed();
    }
}
