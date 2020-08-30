
package coas.perf.TargetClass500;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import coas.perf.TargetClass500.Subject500;

@Aspect
@Component("Advice_500_220")
public class Advice220 {

    private int counter = 0;

    @Around("execution(* Subject500.*(..))")
    public Object onTarget(ProceedingJoinPoint joinPoint) throws Throwable {
        counter++;
        return joinPoint.proceed();
    }
}
