package pl.coas.compiler.e2e;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

import org.junit.jupiter.api.Test;

import pl.coas.api.VoidResult;
import pl.coas.util.CoasTestUtils;

class AspectTests {

    @Test
    void testVoidMethod() throws Throwable {
        String code = "package pl.coas.compiler;\n"
                + "\n"
                + "import pl.coas.api.Aspect;\n"
                + "import pl.coas.api.Pointcut;\n"
                + "import pl.coas.api.AspectClass;\n"
                + "import pl.coas.api.AspectType;\n"
                + "import pl.coas.api.JoinPoint;\n"
                + "\n"
                + "@AspectClass\n"
                + "public class MyAspect {\n"
                + "\n"
                + "    private static int result = 0;\n"
                + "\n"
                + "    public static int entry() {\n"
                + "        MyAspect instance = new MyAspect();\n"
                + "        instance.testedMethod(0);\n"
                + "        return result;\n"
                + "    }\n"
                + "\n"
                + "    public void testedMethod(Integer arg) {\n"
                + "        result = arg;\n"
                + "    }\n"
                + "\n"
                + "    public Object advice0(JoinPoint jp) {\n"
                + "        Aspect.on(Pointcut.method(\"* *.tested*(..)\"));\n"
                + "        Aspect.type(AspectType.PROTOTYPE);\n"
                + "        Aspect.order(0);\n"
                + "\n"
                + "        jp.getArguments()[0] = (int) jp.getArguments()[0] + 1;\n"
                + "        return jp.proceed()\n;"
                + "    }\n"
                + "\n"
                + "   public Object advice1(JoinPoint jp) {\n"
                + "        Aspect.on(Pointcut.method(\"* *.tested*(..)\"));\n"
                + "        Aspect.order(1);\n"
                + "\n"
                + "        jp.getArguments()[0] = (int) jp.getArguments()[0] + 1;\n"
                + "        return jp.proceed()\n;"
                + "   }"
                + "\n"
                + "   public Object advice2(JoinPoint jp) {\n"
                + "        Aspect.on(Pointcut.method(\"* *.tested*(..)\"));\n"
                + "        Aspect.order(2);\n"
                + "\n"
                + "        jp.getArguments()[0] = (int) jp.getArguments()[0] + 1;\n"
                + "        return jp.proceed();\n"
                + "   }"
                + "}\n";

        Object actual = CoasTestUtils.compileAndRun("pl.coas.compiler.MyAspect", "entry", code);

        assertThat(actual).isEqualTo(3);
    }

    @Test
    void testProceedOnVoidMethodShouldReturnVoidResult() throws Throwable {
        String code = "package pl.coas.compiler;\n"
                + "\n"
                + "import pl.coas.api.Aspect;\n"
                + "import pl.coas.api.Pointcut;\n"
                + "import pl.coas.api.AspectClass;\n"
                + "import pl.coas.api.AspectType;\n"
                + "import pl.coas.api.JoinPoint;\n"
                + "\n"
                + "@AspectClass\n"
                + "public class MyAspect {\n"
                + "\n"
                + "    private static Object returnedFromProceed;\n"
                + "\n"
                + "    public static Object entry() {\n"
                + "        MyAspect instance = new MyAspect();\n"
                + "        instance.testedMethod(0);\n"
                + "        return returnedFromProceed;\n"
                + "    }\n"
                + "\n"
                + "    public void testedMethod(Integer arg) {\n"
                + "    }\n"
                + "\n"
                + "    public Object advice0(JoinPoint jp) {\n"
                + "        Aspect.on(Pointcut.method(\"* *.tested*(..)\"));\n"
                + "        Aspect.type(AspectType.PROTOTYPE);\n"
                + "        Aspect.order(0);\n"
                + "\n"
                + "        returnedFromProceed = jp.proceed();\n"
                + "        return returnedFromProceed;\n"
                + "    }\n"
                + "}\n";

        Object actual = CoasTestUtils.compileAndRun("pl.coas.compiler.MyAspect", "entry", code);

        assertThat(actual).isSameAs(VoidResult.INSTANCE);
    }

    @Test
    void testMethodThatReturnsPrimitiveValue() throws Throwable {
        String code = "package pl.coas.compiler;\n"
                + "\n"
                + "import pl.coas.api.Aspect;\n"
                + "import pl.coas.api.Pointcut;\n"
                + "import pl.coas.api.AspectClass;\n"
                + "import pl.coas.api.AspectType;\n"
                + "import pl.coas.api.JoinPoint;\n"
                + "\n"
                + "@AspectClass\n"
                + "public class MyAspect {\n"
                + "\n"
                + "    public static int entry() {\n"
                + "        MyAspect instance = new MyAspect();\n"
                + "        return instance.testedMethod(0);\n"
                + "    }\n"
                + "\n"
                + "    public int testedMethod(Integer arg) {\n"
                + "        return arg;\n"
                + "    }\n"
                + "\n"
                + "    public Object advice0(JoinPoint jp) {\n"
                + "        Aspect.on(Pointcut.method(\"* *.tested*(..)\"));\n"
                + "        Aspect.type(AspectType.PROTOTYPE);\n"
                + "        Aspect.order(0);\n"
                + "\n"
                + "        jp.getArguments()[0] = (int) jp.getArguments()[0] + 1;\n"
                + "        return jp.proceed()\n;"
                + "    }\n"
                + "\n"
                + "   public Object advice1(JoinPoint jp) {\n"
                + "        Aspect.on(Pointcut.method(\"* *.tested*(..)\"));\n"
                + "        Aspect.order(1);\n"
                + "\n"
                + "        jp.getArguments()[0] = (int) jp.getArguments()[0] + 1;\n"
                + "        return jp.proceed()\n;"
                + "   }"
                + "\n"
                + "   public Object advice2(JoinPoint jp) {\n"
                + "        Aspect.on(Pointcut.method(\"* *.tested*(..)\"));\n"
                + "        Aspect.order(2);\n"
                + "\n"
                + "        jp.getArguments()[0] = (int) jp.getArguments()[0] + 1;\n"
                + "        return jp.proceed();\n"
                + "   }"
                + "}\n";

        Object actual = CoasTestUtils.compileAndRun("pl.coas.compiler.MyAspect", "entry", code);

        assertThat(actual).isEqualTo(3);
    }

    @Test
    void testMethodWithMultipleArguments() throws Throwable {
        String code = "package pl.coas.compiler;\n"
                + "\n"
                + "import pl.coas.api.Aspect;\n"
                + "import pl.coas.api.Pointcut;\n"
                + "import pl.coas.api.AspectClass;\n"
                + "import pl.coas.api.AspectType;\n"
                + "import pl.coas.api.JoinPoint;\n"
                + "\n"
                + "@AspectClass\n"
                + "public class MyAspect {\n"
                + "\n"
                + "    public static int entry() {\n"
                + "        MyAspect instance = new MyAspect();\n"
                + "        return instance.testedMethod(0, 0, 0);\n"
                + "    }\n"
                + "\n"
                + "    public int testedMethod(Integer a, Integer b, Integer c) {\n"
                + "        return a + b + c;\n"
                + "    }\n"
                + "\n"
                + "    public Object advice0(JoinPoint jp) {\n"
                + "        Aspect.on(Pointcut.method(\"* *.tested*(..)\"));\n"
                + "        Aspect.type(AspectType.PROTOTYPE);\n"
                + "        Aspect.order(0);\n"
                + "\n"
                + "        jp.getArguments()[0] = (int) jp.getArguments()[0] + 1;\n"
                + "        return jp.proceed()\n;"
                + "    }\n"
                + "\n"
                + "   public Object advice1(JoinPoint jp) {\n"
                + "        Aspect.on(Pointcut.method(\"* *.tested*(..)\"));\n"
                + "        Aspect.order(1);\n"
                + "\n"
                + "        jp.getArguments()[1] = (int) jp.getArguments()[1] + 1;\n"
                + "        return jp.proceed()\n;"
                + "   }"
                + "\n"
                + "   public Object advice2(JoinPoint jp) {\n"
                + "        Aspect.on(Pointcut.method(\"* *.tested*(..)\"));\n"
                + "        Aspect.order(2);\n"
                + "\n"
                + "        jp.getArguments()[2] = (int) jp.getArguments()[2] + 1;\n"
                + "        return jp.proceed();\n"
                + "   }"
                + "}\n";

        Object actual = CoasTestUtils.compileAndRun("pl.coas.compiler.MyAspect", "entry", code);

        assertThat(actual).isEqualTo(3);
    }

    @Test
    void testChangingReturnValue() throws Throwable {
        String code = "package pl.coas.compiler;\n"
                + "\n"
                + "import pl.coas.api.Aspect;\n"
                + "import pl.coas.api.Pointcut;\n"
                + "import pl.coas.api.AspectClass;\n"
                + "import pl.coas.api.AspectType;\n"
                + "import pl.coas.api.JoinPoint;\n"
                + "\n"
                + "@AspectClass\n"
                + "public class MyAspect {\n"
                + "\n"
                + "    public static int entry() {\n"
                + "        MyAspect instance = new MyAspect();\n"
                + "        return instance.testedMethod(0, 0, 0);\n"
                + "    }\n"
                + "\n"
                + "    public int testedMethod(Integer a, Integer b, Integer c) {\n"
                + "        return a + b + c;\n"
                + "    }\n"
                + "\n"
                + "    public Object advice0(JoinPoint jp) {\n"
                + "        Aspect.on(Pointcut.method(\"* *.tested*(..)\"));\n"
                + "        Aspect.type(AspectType.PROTOTYPE);\n"
                + "        Aspect.order(0);\n"
                + "\n"
                + "        jp.getArguments()[0] = (int) jp.getArguments()[0] + 1;\n"
                + "        return jp.proceed()\n;"
                + "    }\n"
                + "\n"
                + "   public Object advice1(JoinPoint jp) {\n"
                + "        Aspect.on(Pointcut.method(\"* *.tested*(..)\"));\n"
                + "        Aspect.order(1);\n"
                + "\n"
                + "        return 42;\n"
                + "   }\n"
                + "}\n";

        Object actual = CoasTestUtils.compileAndRun("pl.coas.compiler.MyAspect", "entry", code);

        assertThat(actual).isEqualTo(42);
    }

    @Test
    void testWithCheckedException() {
        String code = "package pl.coas.compiler;\n"
                + "\n"
                + "import pl.coas.api.Aspect;\n"
                + "import pl.coas.api.Pointcut;\n"
                + "import pl.coas.api.AspectClass;\n"
                + "import pl.coas.api.AspectType;\n"
                + "import pl.coas.api.JoinPoint;\n"
                + "\n"
                + "@AspectClass\n"
                + "public class MyAspect {\n"
                + "\n"
                + "    public static int entry() throws Exception {\n"
                + "        MyAspect instance = new MyAspect();\n"
                + "        return instance.testedMethod(0, 0, 0);\n"
                + "    }\n"
                + "\n"
                + "    public int testedMethod(Integer a, Integer b, Integer c) throws Exception {\n"
                + "        if (a == 0) throw new Exception(\"Test\");\n "
                + "        return a + b + c;\n"
                + "    }\n"
                + "\n"
                + "    public Object advice0(JoinPoint jp) {\n"
                + "        Aspect.on(Pointcut.method(\"* *.tested*(..)\"));\n"
                + "        Aspect.type(AspectType.PROTOTYPE);\n"
                + "        Aspect.order(0);\n"
                + "\n"
                + "        return jp.proceed()\n;"
                + "    }\n"
                + "\n"
                + "   public Object advice1(JoinPoint jp) throws Exception{\n"
                + "        Aspect.on(Pointcut.method(\"* *.tested*(..)\"));\n"
                + "        Aspect.order(1);\n"
                + "        try {\n"
                + "            return jp.proceed();\n"
                + "        } catch (Exception e) {\n"
                + "            throw new Exception(\"Rewritten exception\");\n"
                + "        }"
                + "   }\n"
                + "}\n";

        Throwable thrown = catchThrowable(
                () -> CoasTestUtils.compileAndRun("pl.coas.compiler.MyAspect", "entry", code));

        assertThat(thrown).hasMessage("Rewritten exception");
    }

    @Test
    void testWithGenericMethod() throws Throwable {
        String code = "package pl.coas.compiler;\n"
                + "\n"
                + "import pl.coas.api.Aspect;\n"
                + "import pl.coas.api.Pointcut;\n"
                + "import pl.coas.api.AspectClass;\n"
                + "import pl.coas.api.AspectType;\n"
                + "import pl.coas.api.JoinPoint;\n"
                + "\n"
                + "@AspectClass\n"
                + "public class MyAspect {\n"
                + "\n"
                + "    public static String entry() throws Exception {\n"
                + "        MyAspect instance = new MyAspect();\n"
                + "        return instance.testedMethod(\"abcd\");\n"
                + "    }\n"
                + "\n"
                + "    public <T> T testedMethod(T a) {\n"
                + "        return a;\n"
                + "    }\n"
                + "\n"
                + "    public Object advice0(JoinPoint jp) {\n"
                + "        Aspect.on(Pointcut.method(\"* *.tested*(..)\"));\n"
                + "        Aspect.type(AspectType.PROTOTYPE);\n"
                + "        Aspect.order(0);\n"
                + "\n"
                + "        if (jp.getArguments()[0].getClass().isAssignableFrom(String.class)) {\n"
                + "            return \"changed\";\n"
                + "        }\n"
                + "\n"
                + "        return jp.proceed()\n;"
                + "    }\n"
                + "}\n";

        Object actual = CoasTestUtils.compileAndRun("pl.coas.compiler.MyAspect", "entry", code);

        assertThat(actual).isEqualTo("changed");
    }

    @Test
    void testMethodWithPrimitiveArguments() throws Throwable {
        String code = "package pl.coas.compiler;\n"
                + "\n"
                + "import pl.coas.api.Aspect;\n"
                + "import pl.coas.api.Pointcut;\n"
                + "import pl.coas.api.AspectClass;\n"
                + "import pl.coas.api.AspectType;\n"
                + "import pl.coas.api.JoinPoint;\n"
                + "\n"
                + "@AspectClass\n"
                + "public class MyAspect {\n"
                + "\n"
                + "    public static int entry() {\n"
                + "        MyAspect instance = new MyAspect();\n"
                + "        return instance.testedMethod(0);\n"
                + "    }\n"
                + "\n"
                + "    public int testedMethod(int arg) {\n"
                + "        return arg;\n"
                + "    }\n"
                + "\n"
                + "    public Object advice0(JoinPoint jp) {\n"
                + "        Aspect.on(Pointcut.method(\"* *.tested*(..)\"));\n"
                + "        Aspect.type(AspectType.PROTOTYPE);\n"
                + "        Aspect.order(0);\n"
                + "\n"
                + "        jp.getArguments()[0] = (int) jp.getArguments()[0] + 1;\n"
                + "        return jp.proceed()\n;"
                + "    }\n"
                + "\n"
                + "   public Object advice1(JoinPoint jp) {\n"
                + "        Aspect.on(Pointcut.method(\"* *.tested*(..)\"));\n"
                + "        Aspect.order(1);\n"
                + "\n"
                + "        jp.getArguments()[0] = (int) jp.getArguments()[0] + 1;\n"
                + "        return jp.proceed()\n;"
                + "   }"
                + "\n"
                + "   public Object advice2(JoinPoint jp) {\n"
                + "        Aspect.on(Pointcut.method(\"* *.tested*(..)\"));\n"
                + "        Aspect.order(2);\n"
                + "\n"
                + "        jp.getArguments()[0] = (int) jp.getArguments()[0] + 1;\n"
                + "        return jp.proceed();\n"
                + "   }"
                + "}\n";

        Object actual = CoasTestUtils.compileAndRun("pl.coas.compiler.MyAspect", "entry", code);

        assertThat(actual).isEqualTo(3);
    }

    @Test
    void testStaticMethod() throws Throwable {
        String code = "package pl.coas.compiler;\n"
                + "\n"
                + "import pl.coas.api.Aspect;\n"
                + "import pl.coas.api.Pointcut;\n"
                + "import pl.coas.api.AspectClass;\n"
                + "import pl.coas.api.AspectType;\n"
                + "import pl.coas.api.JoinPoint;\n"
                + "\n"
                + "@AspectClass\n"
                + "public class MyAspect {\n"
                + "\n"
                + "    public static int entry() {\n"
                + "        return testedMethod(0);\n"
                + "    }\n"
                + "\n"
                + "    private static int testedMethod(Integer arg) {\n"
                + "        return arg;\n"
                + "    }\n"
                + "\n"
                + "    public Object advice0(JoinPoint jp) {\n"
                + "        Aspect.on(Pointcut.method(\"* *.tested*(..)\"));\n"
                + "        Aspect.type(AspectType.PROTOTYPE);\n"
                + "        Aspect.order(0);\n"
                + "\n"
                + "        jp.getArguments()[0] = (int) jp.getArguments()[0] + 1;\n"
                + "        return jp.proceed()\n;"
                + "    }\n"
                + "\n"
                + "   public Object advice1(JoinPoint jp) {\n"
                + "        Aspect.on(Pointcut.method(\"* *.tested*(..)\"));\n"
                + "        Aspect.order(1);\n"
                + "\n"
                + "        jp.getArguments()[0] = (int) jp.getArguments()[0] + 1;\n"
                + "        return jp.proceed()\n;"
                + "   }"
                + "\n"
                + "   public Object advice2(JoinPoint jp) {\n"
                + "        Aspect.on(Pointcut.method(\"* *.tested*(..)\"));\n"
                + "        Aspect.order(2);\n"
                + "\n"
                + "        jp.getArguments()[0] = (int) jp.getArguments()[0] + 1;\n"
                + "        return jp.proceed();\n"
                + "   }"
                + "}\n";

        Object actual = CoasTestUtils.compileAndRun("pl.coas.compiler.MyAspect", "entry", code);

        assertThat(actual).isEqualTo(3);
    }

    @Test
    void testMethodContainingVarargs() throws Throwable {
        String code = "package pl.coas.compiler;\n"
                + "\n"
                + "import pl.coas.api.Aspect;\n"
                + "import pl.coas.api.Pointcut;\n"
                + "import pl.coas.api.AspectClass;\n"
                + "import pl.coas.api.AspectType;\n"
                + "import pl.coas.api.JoinPoint;\n"
                + "\n"
                + "@AspectClass\n"
                + "public class MyAspect {\n"
                + "\n"
                + "    public static int entry() {\n"
                + "        return testedMethod(0);\n"
                + "    }\n"
                + "\n"
                + "    private static int testedMethod(Integer... args) {\n"
                + "        return args[0];\n"
                + "    }\n"
                + "\n"
                + "    public Object advice0(JoinPoint jp) {\n"
                + "        Aspect.on(Pointcut.method(\"* *.tested*(..)\"));\n"
                + "        Aspect.type(AspectType.PROTOTYPE);\n"
                + "        Aspect.order(0);\n"
                + "\n"
                + "        ((Integer[]) jp.getArguments()[0])[0] = ((Integer[]) jp.getArguments()[0])[0] + 1;\n"
                + "        return jp.proceed()\n;"
                + "    }\n"
                + "\n"
                + "   public Object advice1(JoinPoint jp) {\n"
                + "        Aspect.on(Pointcut.method(\"* *.tested*(..)\"));\n"
                + "        Aspect.order(1);\n"
                + "\n"
                + "        ((Integer[]) jp.getArguments()[0])[0] = ((Integer[]) jp.getArguments()[0])[0] + 1;\n"
                + "        return jp.proceed()\n;"
                + "   }"
                + "\n"
                + "   public Object advice2(JoinPoint jp) {\n"
                + "        Aspect.on(Pointcut.method(\"* *.tested*(..)\"));\n"
                + "        Aspect.order(2);\n"
                + "\n"
                + "        ((Integer[]) jp.getArguments()[0])[0] = ((Integer[]) jp.getArguments()[0])[0] + 1;\n"
                + "        return jp.proceed();\n"
                + "   }"
                + "}\n";

        Object actual = CoasTestUtils.compileAndRun("pl.coas.compiler.MyAspect", "entry", code);

        assertThat(actual).isEqualTo(3);
    }
}
