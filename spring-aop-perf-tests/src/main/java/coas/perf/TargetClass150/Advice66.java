
package coas.perf.TargetClass150;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import coas.perf.TargetClass150.Subject150;

@Aspect
@Component("Advice_150_66")
public class Advice66 {

    private int counter = 0;

    @Around("execution(* Subject150.*(..))")
    public Object onTarget(ProceedingJoinPoint joinPoint) throws Throwable {
        counter++;
        return joinPoint.proceed();
    }
}
