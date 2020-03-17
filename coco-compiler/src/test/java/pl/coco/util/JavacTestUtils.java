package pl.coco.util;

public class JavacTestUtils {

    public static Object compileAndRun(String qualifiedClassName, String methodName, String code)
            throws Throwable {

        return compileAndRun(qualifiedClassName, methodName, code, new Class<?>[] {},
                new Object[] {});
    }

    public static Object compileAndRun(String qualifiedClassName, String methodName, String code,
            Class<?> argumentType, Object argument) throws Throwable {

        return compileAndRun(qualifiedClassName, methodName, code, new Class<?>[] { argumentType },
                new Object[] { argument });
    }

    public static Object compileAndRun(String qualifiedClassName, String methodName, String code,
            Class<?>[] argumentTypes, Object[] arguments) throws Throwable {

        TestCompiler compiler = new TestCompiler();
        TestRunner runner = new TestRunner();

        byte[] byteCode = compiler.compile(qualifiedClassName, code);
        return runner.run(byteCode, qualifiedClassName, methodName, argumentTypes, arguments);
    }
}
