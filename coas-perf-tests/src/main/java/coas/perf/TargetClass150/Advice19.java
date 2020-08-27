
package coas.perf.TargetClass150;

import pl.coas.api.Aspect;
import pl.coas.api.AspectClass;
import pl.coas.api.JoinPoint;
import pl.coas.api.Pointcut;

import coas.perf.TargetClass150.Subject150;

@AspectClass
public class Advice19 {

    private int counter = 0;

    public Object onTarget(JoinPoint jp) {
        Aspect.on(Pointcut.targetClass(Subject150.class));
        counter++;

        return jp.proceed();
    }
}
