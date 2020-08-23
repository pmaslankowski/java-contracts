package pl.coas.examples.paper;

import pl.coas.api.Aspect;
import pl.coas.api.AspectClass;
import pl.coas.api.JoinPoint;
import pl.coas.api.Pointcut;

@AspectClass
public class LoggingAspect {

    public Object onLogged(JoinPoint jp) {
        Aspect.on(Pointcut.annotatedMethod(Logged.class));
        Aspect.order(1);

        System.out.println("Method " + jp.getMethod().toString() + " has been called.");

        return jp.proceed();
    }
}
