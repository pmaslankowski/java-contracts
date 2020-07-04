package pl.coas.compiler.e2e;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

import pl.coas.util.CoasTestUtils;

class AnnotationTests {

    @Test
    void testAnnotations() throws Throwable {
        String code = "package pl.coas.compiler;\n"
                + "\n"
                + "import pl.coas.api.annotation.Advice;\n"
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
                + "    @Advice(on = \"method(* *.tested*(..))\")\n"
                + "    public Object advice0(JoinPoint jp) {\n"
                + "        jp.getArguments()[0] = (int) jp.getArguments()[0] + 1;\n"
                + "        return jp.proceed()\n;"
                + "    }\n"
                + "\n"
                + "   @Advice(on = \"method(* *.tested*(..))\", type=AspectType.PROTOTYPE, order=1)\n"
                + "   public Object advice1(JoinPoint jp) {\n"
                + "        jp.getArguments()[0] = (int) jp.getArguments()[0] + 1;\n"
                + "        return jp.proceed()\n;"
                + "   }"
                + "\n"
                + "   @Advice(on = \"method(* *.tested*(..))\", order=2)\n"
                + "   public Object advice2(JoinPoint jp) {\n"
                + "        jp.getArguments()[0] = (int) jp.getArguments()[0] + 1;\n"
                + "        return jp.proceed();\n"
                + "   }"
                + "}\n";

        Object actual = CoasTestUtils.compileAndRun("pl.coas.compiler.MyAspect", "entry", code);

        assertThat(actual).isEqualTo(3);
    }
}
