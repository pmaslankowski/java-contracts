package pl.coas.compiler;

import pl.coas.api.JoinPoint;

public class MyAspectExample {

    public MyAspectExample() {
        super();
    }

    public static void main(String[] args) {
        System.out.println(entry());
    }

    public static int entry() {
        return testedMethod(0);
    }

    private static int testedMethod(Integer arg) {
        final Object coas$instance = null;
        final java.lang.reflect.Method coas$method = pl.coas.internal.MethodCache.getMethod(MyAspectExample.class, "testedMethod", java.lang.Integer.class);
        Object[] coas$args = new Object[]{arg};
        return (int)new MyAspectExample().advice0(new pl.coas.api.JoinPoint(coas$instance, coas$method, coas$args, (Object[] coas$args$0)-> MyAspectExample.coas$aspect$instance.advice1(new pl.coas.api.JoinPoint(coas$instance, coas$method, coas$args$0, (Object[] coas$args$1)-> MyAspectExample.coas$aspect$instance.advice2(new pl.coas.api.JoinPoint(coas$instance, coas$method, coas$args$1, (Object[] coas$args$2)->coas$target$testedMethod((java.lang.Integer)coas$args$2[0])))))));
    }

    public Object advice0(JoinPoint jp) {
        jp.getArguments()[0] = (int)jp.getArguments()[0] + 1;
        return jp.proceed();
    }

    public Object advice1(JoinPoint jp) {
        jp.getArguments()[0] = (int)jp.getArguments()[0] + 1;
        return jp.proceed();
    }

    public Object advice2(JoinPoint jp) {
        jp.getArguments()[0] = (int)jp.getArguments()[0] + 1;
        return jp.proceed();
    }
    public static final MyAspectExample coas$aspect$instance = new MyAspectExample();

    private static int coas$target$testedMethod(java.lang.Integer arg) {
        return arg;
    }
}
