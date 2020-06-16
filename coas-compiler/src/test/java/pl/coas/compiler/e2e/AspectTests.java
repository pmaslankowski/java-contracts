package pl.coas.compiler.e2e;

import org.junit.jupiter.api.Test;

import pl.coas.util.CoasTestUtils;

class AspectTests {

    @Test
    void test() throws Throwable {
        String code = "package pl.coas.compiler;\n"
                + "\n"
                + "import pl.coas.api.Aspect;\n"
                + "import pl.coas.api.Pointcut;\n"
                + "import pl.coas.api.AspectClass;\n"
                + "import pl.coas.api.AspectType;\n"
                + "\n"
                + "@AspectClass\n"
                + "public class MyAspect {\n"
                + "\n"
                + "    public static void entry() {\n"
                + "    }\n"
                + "\n"
                + "    public int testedMethod(int arg, boolean flag) {\n"
                + "        Aspect.on(!Pointcut.args(MyAspect.class));\n"
                + "        Aspect.type(AspectType.PROTOTYPE);\n"
                + "        Aspect.order(42);\n"
                + "        return 42;\n"
                + "    }\n"
                + "}\n";

        Object actual = CoasTestUtils.compileAndRun("pl.coas.compiler.MyAspect", "entry", code);

        System.out.println("halo");
    }
}
