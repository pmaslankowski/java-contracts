
package coas.perf.ComplexCondition50;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import coas.perf.ComplexCondition50.Subject50;

@Aspect
@Component("Advice_50_0_cc")
public class Advice0 {

    @Around("execution(* coas.perf.ComplexCondition50.Subject50.*(..)) and (" +
            "execution(* *.junk0(..)) or " +
            "execution(* *.junk1(..)) or " +
            "execution(* *.junk2(..)) or " +
            "execution(* *.junk3(..)) or " +
            "execution(* *.junk29(..)) or " +
            "execution(* *.target(..)))")
    public Object onTarget(ProceedingJoinPoint joinPoint) throws Throwable {
         int res = (int) joinPoint.proceed();

        System.out.println("halo");
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
