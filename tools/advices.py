#!/usr/bin/python3


def code(i):
    return f'''
package coas.perf;

import pl.coas.api.Aspect;
import pl.coas.api.JoinPoint;
import pl.coas.api.Pointcut;

@AspectClass
public class Advice{i} {{

    private int counter = 0;

    public Object onTarget(JoinPoint jp) {{
        Aspect.on(Pointcut.targetClass(Subject.class));
        counter++;

        return jp.proceed();
    }}
}}
'''

def path(case):
    return '/home/pma/university/java-contracts/coas-perf-tests/src/main/java/coas/perf/TargetClass{case}/'


tests = [50, 100, 150, 200, 250, 300, 500]
for case in tests:
    for i in range(case):
        with open(path(case) + f'Advice{i}.java', 'w+') as f:
            f.writelines(code(i))
