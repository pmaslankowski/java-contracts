
package coas.perf.ComplexCondition100;

import pl.coas.api.Aspect;
import pl.coas.api.AspectClass;
import pl.coas.api.JoinPoint;
import pl.coas.api.Pointcut;

import coas.perf.ComplexCondition100.Subject100;

@AspectClass
public class Advice43 {

    private int counter = 0;

    public Object onTarget(JoinPoint jp) {
        Aspect.on(Pointcut.targetClass(Subject100.class) && (Pointcut.method("junk0") || Pointcut.method("junk1") || Pointcut.method("junk2") || Pointcut.method("junk3") || Pointcut.method("junk4") || Pointcut.method("junk5") || Pointcut.method("junk6") || Pointcut.method("junk7") || Pointcut.method("junk8") || Pointcut.method("junk9") || Pointcut.method("junk10") || Pointcut.method("junk11") || Pointcut.method("junk12") || Pointcut.method("junk13") || Pointcut.method("junk14") || Pointcut.method("junk15") || Pointcut.method("junk16") || Pointcut.method("junk17") || Pointcut.method("junk18") || Pointcut.method("junk19") || Pointcut.method("junk20") || Pointcut.method("junk21") || Pointcut.method("junk22") || Pointcut.method("junk23") || Pointcut.method("junk24") || Pointcut.method("junk25") || Pointcut.method("junk26") || Pointcut.method("junk27") || Pointcut.method("junk28") || Pointcut.method("junk29") || Pointcut.method("target")));
        counter++;

        return jp.proceed();
    }
}
