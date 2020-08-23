package pl.coas.examples.paper;

import pl.coas.api.Aspect;
import pl.coas.api.AspectClass;
import pl.coas.api.JoinPoint;
import pl.coas.api.Pointcut;
import pl.coas.examples.auth.Authorizer;
import pl.coas.examples.auth.SecurityException;

@AspectClass
public class SecurityAspect {

    public Object onSecured(JoinPoint jp) {
        Aspect.on(Pointcut.annotatedMethod(Secured.class));

        if (Authorizer.isAuthorized()) {
            return jp.proceed();
        } else {
            throw new SecurityException("You are not authorized to call this method");
        }
    }
}
