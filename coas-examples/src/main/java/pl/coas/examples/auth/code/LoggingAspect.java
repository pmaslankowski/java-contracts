package pl.coas.examples.auth.code;

import pl.coas.api.Aspect;
import pl.coas.api.AspectClass;
import pl.coas.api.JoinPoint;
import pl.coas.api.Pointcut;
import pl.coas.examples.auth.Logged;

@AspectClass
public class LoggingAspect {

    public Object onLogged(JoinPoint jp) {
        Aspect.on(Pointcut.annotatedClass(Logged.class));

        System.out.println("Method " + jp.getMethod().toString() + " has been called.");

        return jp.proceed();
    }
}
