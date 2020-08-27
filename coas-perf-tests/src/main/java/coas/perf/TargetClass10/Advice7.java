
package coas.perf.TargetClass10;

import pl.coas.api.Aspect;
import pl.coas.api.AspectClass;
import pl.coas.api.JoinPoint;
import pl.coas.api.Pointcut;

@AspectClass
public class Advice7 {

    private int counter = 0;

    public Object onTarget(JoinPoint jp) {
        Aspect.on(Pointcut.targetClass(Subject10.class));
        counter++;

        return jp.proceed();
    }
}
