
package coas.perf.TargetClass100;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import coas.perf.TargetClass100.Subject100;

@Aspect
@Component("Advice_100_55")
public class Advice55 {

    private int counter = 0;

    @Around("execution(* Subject100.*(..))")
    public Object onTarget(ProceedingJoinPoint joinPoint) throws Throwable {
        counter++;
        return joinPoint.proceed();
    }
}
