
package coas.perf.TargetClass300;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import coas.perf.TargetClass300.Subject300;

@Aspect
@Component("Advice_300_66")
public class Advice66 {

    private int counter = 0;

    @Around("execution(* Subject300.*(..))")
    public Object onTarget(ProceedingJoinPoint joinPoint) throws Throwable {
        counter++;
        return joinPoint.proceed();
    }
}
