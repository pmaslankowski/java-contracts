
package coas.perf.TargetClass100;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import coas.perf.TargetClass100.Subject100;

@Aspect
@Component("Advice_100_0")
public class Advice0 {

    private int counter = 0;

    @Around("execution(* Subject100.*(..))")
    public Object onTarget(ProceedingJoinPoint joinPoint) throws Throwable {
        counter++;
        return joinPoint.proceed();
    }
}
