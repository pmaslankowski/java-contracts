
package coas.perf.TargetClass300;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import coas.perf.TargetClass300.Subject300;

@Aspect
@Component("Advice_300_27")
public class Advice27 {

    private int counter = 0;

    @Around("execution(* Subject300.*(..))")
    public Object onTarget(ProceedingJoinPoint joinPoint) throws Throwable {
        int res = (int) joinPoint.proceed();
        
        for (int i=0; i < 1000; i++) {
            if (res % 2 == 0) {
                res /= 2;
            } else {
                res = 2 * res + 1;
            }
        }

        return res;

    }
}
