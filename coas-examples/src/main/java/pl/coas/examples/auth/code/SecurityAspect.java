package pl.coas.examples.auth.code;

import pl.coas.api.Aspect;
import pl.coas.api.AspectClass;
import pl.coas.api.JoinPoint;
import pl.coas.api.Pointcut;
import pl.coas.examples.auth.Authorizer;
import pl.coas.examples.auth.SecurityException;
import pl.coas.examples.auth.Sensitive;

@AspectClass
public class SecurityAspect {

    public Object onSecuredMethodWithSensitiveArgs(JoinPoint jp) {
        Aspect.on(Pointcut.targetClass("pl.coas.examples.auth.Secure*")
                && Pointcut.annotatedArgs(Sensitive.class));

        if (!Authorizer.isAuthorized()) {
            throw new SecurityException(
                    "You aren't authorized to call this method with sensitive data");
        } else {
            return jp.proceed();
        }
    }
}
