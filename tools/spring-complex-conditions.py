#!/usr/bin/python3

def code(i, case):
    conds = conditions(case)
    return f'''
package coas.perf.ComplexCondition{case};

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import coas.perf.ComplexCondition{case}.Subject{case};

@Aspect
@Component("Advice_{case}_{i}_cc")
public class Advice{i} {{

    @Around("{conds}")
    public Object onTarget(ProceedingJoinPoint joinPoint) throws Throwable {{
         int res = (int) joinPoint.proceed();
        
        for (int i=0; i < 1000; i++) {{
            if (res % 2 == 0) {{
                res /= 2;
            }} else {{
                res = 2 * res + 1;
            }}
        }}

        return res;

    }}
}}
'''


def conditions(case):
    res = f'execution(* coas.perf.ComplexCondition{case}.Subject{case}.*(..)) and ('
    res += ' or '.join([f'execution(* *.junk{i}(..))' for i in range(30)])
    res += ' or execution(* *.target(..))'
    res += ')'
    return res


def subject(case):
    return f'''
package coas.perf.ComplexCondition{case};

import org.springframework.stereotype.Component;

@Component("Subject_{case}_cc")
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
    return f'/home/pma/university/java-contracts/spring-aop-perf-tests/src/main/java/coas/perf/ComplexCondition{case}/'


tests = [50, 100, 150, 200, 250, 300, 500]
#tests = [10]

for case in tests:
    with open(path(case) + f'Subject{case}.java', 'w+') as f:
        f.writelines(subject(case))
    
    for i in range(case):
        with open(path(case) + f'Advice{i}.java', 'w+') as f:
            f.writelines(code(i, case))
