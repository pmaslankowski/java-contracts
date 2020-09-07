
package coas.perf.ComplexCondition500;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import coas.perf.ComplexCondition500.Subject500;

@Aspect
@Component("Advice_500_180_cc")
public class Advice180 {

    @Around("execution(* coas.perf.ComplexCondition500.Subject500.*(..)) and (execution(* *.junk0(..)) or execution(* *.junk1(..)) or execution(* *.junk2(..)) or execution(* *.junk3(..)) or execution(* *.junk4(..)) or execution(* *.junk5(..)) or execution(* *.junk6(..)) or execution(* *.junk7(..)) or execution(* *.junk8(..)) or execution(* *.junk9(..)) or execution(* *.junk10(..)) or execution(* *.junk11(..)) or execution(* *.junk12(..)) or execution(* *.junk13(..)) or execution(* *.junk14(..)) or execution(* *.junk15(..)) or execution(* *.junk16(..)) or execution(* *.junk17(..)) or execution(* *.junk18(..)) or execution(* *.junk19(..)) or execution(* *.junk20(..)) or execution(* *.junk21(..)) or execution(* *.junk22(..)) or execution(* *.junk23(..)) or execution(* *.junk24(..)) or execution(* *.junk25(..)) or execution(* *.junk26(..)) or execution(* *.junk27(..)) or execution(* *.junk28(..)) or execution(* *.junk29(..)) or execution(* *.target(..)))")
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
