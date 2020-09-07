
package coas.perf.ComplexCondition50;

import pl.coas.api.Aspect;
import pl.coas.api.AspectClass;
import pl.coas.api.JoinPoint;
import pl.coas.api.Pointcut;

import coas.perf.ComplexCondition50.Subject50;

@AspectClass
public class Advice33 {

    public static final Advice33 coas$aspect$instance = new Advice33();

    public Object onTarget(JoinPoint jp) {

        int res = (int) jp.proceed();
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