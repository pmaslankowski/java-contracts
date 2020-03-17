package pl.coco.example;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import pl.coco.util.JavacTestUtils;

class ExampleJavacTest {

    private static final String QUALIFIED_CLASS_NAME = "pl.coco.compiler.Test";
    private static final String METHOD_NAME = "method";
    private static final String CODE
            = "package pl.coco.compiler;\n"
            + "public class Test {\n"
            + "    public static int method(int i) {\n"
            + "        return i;\n"
            + "    }\n"
            + "}\n";

    @DisplayName("Example test using embedded javac and coco plugin")
    @Test
    void exampleTest() throws Throwable {
        JavacTestUtils.compileAndRun(QUALIFIED_CLASS_NAME, METHOD_NAME, CODE, int.class, -1);
    }
}
