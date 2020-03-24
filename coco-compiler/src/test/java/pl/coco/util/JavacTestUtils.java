package pl.coco.util;

public class JavacTestUtils {

    public static final String CONTRACTS_ENABLED = "-Xplugin:coco";
    public static final String CONTRACTS_DISABLED = "-Xplugin:coco --disabled";

    public static Object compileAndRun(String qualifiedClassName, String methodName, String code)
            throws Throwable {

        return compileAndRun(qualifiedClassName, methodName, code, new Class<?>[] {},
                new Object[] {}, CONTRACTS_ENABLED);
    }

    public static Object compileAndRun(String qualifiedClassName, String methodName, String code,
            String pluginArg) throws Throwable {

        return compileAndRun(qualifiedClassName, methodName, code, new Class<?>[] {},
                new Object[] {}, pluginArg);
    }

    public static Object compileAndRun(String qualifiedClassName, String methodName, String code,
            Class<?> argumentType, Object argument, String pluginArg) throws Throwable {

        return compileAndRun(qualifiedClassName, methodName, code, new Class<?>[] { argumentType },
                new Object[] { argument }, pluginArg);
    }

    public static Object compileAndRun(String qualifiedClassName, String methodName, String code,
            Class<?>[] argumentTypes, Object[] arguments, String pluginArg) throws Throwable {

        TestCompiler compiler = new TestCompiler();
        TestRunner runner = new TestRunner();

        byte[] byteCode = compiler.compile(qualifiedClassName, code, pluginArg);
        return runner.run(byteCode, qualifiedClassName, methodName, argumentTypes, arguments);
    }
}
