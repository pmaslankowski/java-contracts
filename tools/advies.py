#!/usr/bin/python3


def code(i):
    return f'''
package coas.perf;

import pl.coas.api.Aspect;
import pl.coas.api.JoinPoint;
import pl.coas.api.Pointcut;

public class Advice{i} {{

    private int counter = 0;

    public Object onTarget(JoinPoint jp) {{
        Aspect.on(Pointcut.annotatedClass(Subject.class));
        counter++;

        return jp.proceed();
    }}
}}
'''


for i in range(10):
    with open(f'Advice{i}.java', 'w+') as f:
        f.writelines(code(i))
