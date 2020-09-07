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

    public static final Advice{i} coas$aspect$instance = new Advice{i}();

    public Object onTarget(JoinPoint jp) {{

        int res = (int) jp.proceed();
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


def target():
    return f'''public int target(int x) {{
final Object coas$instance = this;
final Object[] args = new Object[] {{ x }};
return (int) coas$target$advice0(args);
}}
'''


def advice(i):
    return f'''private Object coas$target$advice{i}(Object[] args) {{
    JoinPoint joinPoint = new JoinPoint(this, coas$method, args, this::coas$target$advice{i+1});
    return Advice{i}.coas$aspect$instance.onTarget(joinPoint);
}}
    '''


def last(n):
    return f'''private Object coas$target$advice{n}(Object[] args) {{
    JoinPoint joinPoint = new JoinPoint(this, coas$method, args,
    innerArgs -> coas$target$target((int) innerArgs[0]));
    return Advice{n}.coas$aspect$instance.onTarget(joinPoint);
}}
    '''


def instrumented_subject(case):
    advices = '\n'.join([target()] + [advice(i) for i in range(case-1)] + [last(case-1)])

    return f'''package coas.perf.ComplexCondition{case};

import java.lang.reflect.Method;

import pl.coas.api.JoinPoint;

public class Subject{case} {{

    final Method coas$method = pl.coas.internal.MethodCache
            .getMethod(coas.perf.ComplexCondition{case}.Subject{case}.class, "target", int.class);

    {advices}

    private int coas$target$target(int x) {{
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


tests = [50, 100, 150, 200, 250, 300, 500] #, 150, 200, 250, 300, 500]
for case in tests:
    #shutil.copyfile(f'/home/pma/university/Subject{case}.java', path(case) + f'Subject{case}.java')
    with open(path(case) + f'Subject{case}.java', 'w+') as f:
        f.writelines(instrumented_subject(case))
    for i in range(case):
        with open(path(case) + f'Advice{i}.java', 'w+') as f:
            f.writelines(code(i, case))
