
package coas.perf.ComplexCondition100;

import pl.coas.api.Aspect;
import pl.coas.api.AspectClass;
import pl.coas.api.JoinPoint;
import pl.coas.api.Pointcut;

import coas.perf.ComplexCondition100.Subject100;

@AspectClass
public class Advice9 {

    public static final Advice9 coas$aspect$instance = new Advice9();

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
