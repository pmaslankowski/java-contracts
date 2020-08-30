
package coas.perf.TargetClass50;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import coas.perf.TargetClass50.Subject50;

@Aspect
@Component("Advice_50_11")
public class Advice11 {

    private int counter = 0;

    @Around("execution(* Subject50.*(..))")
    public Object onTarget(ProceedingJoinPoint joinPoint) throws Throwable {
        counter++;
        return joinPoint.proceed();
    }
}
