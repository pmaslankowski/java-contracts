
package coas.perf.TargetClass250;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import coas.perf.TargetClass250.Subject250;

@Aspect
@Component("Advice_250_216")
public class Advice216 {

    private int counter = 0;

    @Around("execution(* Subject250.*(..))")
    public Object onTarget(ProceedingJoinPoint joinPoint) throws Throwable {
        counter++;
        return joinPoint.proceed();
    }
}
