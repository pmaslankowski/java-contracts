
package coas.perf.TargetClass50;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import coas.perf.TargetClass50.Subject50;

@Aspect
@Component("Advice_50_17")
public class Advice17 {

    private int counter = 0;

    @Around("execution(* Subject50.*(..))")
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
