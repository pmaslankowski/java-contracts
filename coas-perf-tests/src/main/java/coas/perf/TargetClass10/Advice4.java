
package coas.perf.TargetClass10;

import pl.coas.api.Aspect;
import pl.coas.api.JoinPoint;
import pl.coas.api.Pointcut;

public class Advice4 {

    private int counter = 0;

    public Object onTarget(JoinPoint jp) {
        Aspect.on(Pointcut.targetClass(Subject10.class));
        counter++;

        return jp.proceed();
    }
}
