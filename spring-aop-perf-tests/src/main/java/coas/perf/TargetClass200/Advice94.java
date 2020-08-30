
package coas.perf.TargetClass200;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import coas.perf.TargetClass200.Subject200;

@Aspect
@Component("Advice_200_94")
public class Advice94 {

    private int counter = 0;

    @Around("execution(* Subject200.*(..))")
    public Object onTarget(ProceedingJoinPoint joinPoint) throws Throwable {
        counter++;
        return joinPoint.proceed();
    }
}
