#!/usr/bin/python3


def code(i, case):
    return f'''
package coas.perf.ComplexCondition{case};

import pl.coas.api.Aspect;
import pl.coas.api.AspectClass;
import pl.coas.api.JoinPoint;
import pl.coas.api.Pointcut;

import coas.perf.ComplexCondition{case}.Subject{case};

@AspectClass
public class Advice{i} {{

    private int counter = 0;

    public Object onTarget(JoinPoint jp) {{
        Aspect.on({conditions(case)});
        counter++;

        return jp.proceed();
    }}
}}
'''


def conditions(case):
    res = f'Pointcut.targetClass(Subject{case}.class) && ('
    res += ' || '.join([f'Pointcut.method("junk{i}")' for i in range(30)])
    res += ' || Pointcut.method("target")'
    res += ')'
    return res


def subject(case):
    return f'''
    package coas.perf.ComplexCondition{case};

public class Subject{case} {{

    public int target(int x) {{
        int[] fib = new int[x + 2];
        fib[0] = 0;
        fib[1] = 1;
        for (int i = 2; i <= x; i++) {{
            fib[i] = fib[i - 1] + fib[i - 2];
        }}
        return fib[x] % 10;
    }}
}}
'''


def path(case):
    return f'/home/pma/university/java-contracts/coas-perf-tests/src/main/java/coas/perf/ComplexCondition{case}/'


tests = [10, 50, 100, 150, 200, 250, 300, 500]
#tests = [10]

for case in tests:
    with open(path(case) + f'Subject{case}.java', 'w+') as f:
        f.writelines(subject(case))
    
    for i in range(case):
        with open(path(case) + f'Advice{i}.java', 'w+') as f:
            f.writelines(code(i, case))
