package pl.test.compiler.commons;

public class JavacTestUtils {

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

        TestCompilerInput input = new TestCompilerInput.Builder()
                .addCompilationUnit(qualifiedClassName, code)
                .withPluginArg(pluginArg)
                .build();
        CompiledClasses compiled = compiler.compile(input);

        return runner.run(compiled, qualifiedClassName, methodName, argumentTypes, arguments);
    }
}
