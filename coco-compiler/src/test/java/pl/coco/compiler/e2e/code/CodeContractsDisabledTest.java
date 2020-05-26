package pl.coco.compiler.e2e.code;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import pl.coco.util.JavacTestUtils;

class CodeContractsDisabledTest {

    public static final String DISABLED = "-Xplugin:coco --disabled";

    private static final String CLASS_NAME = "pl.coco.compiler.Test";
    private static final String ENTRY_POINT = "entry";
    private static final int RESULT = 42;

    @DisplayName("Method return result even though precondition fails")
    @Test
    void shouldReturnResultEvenThoughPreconditionFails() throws Throwable {
        String code = "package pl.coco.compiler;\n"
                + "\n"
                + "import pl.coco.api.code.Contract;\n"
                + "\n"
                + "public class Test {\n"
                + "    public static int entry() {\n"
                + "        return testedMethod(-1);\n"
                + "    }\n"
                + "\n"
                + "    private static int testedMethod(int arg) {\n"
                + "        Contract.requires(arg >= 0);\n"
                + "        return 42;\n"
                + "    }\n"
                + "\n"
                + "}\n";

        Object result = JavacTestUtils.compileAndRun(CLASS_NAME, ENTRY_POINT, code, DISABLED);

        assertThat(result).isEqualTo(RESULT);

    }

    @DisplayName("Method return result even though postcondition fails")
    @Test
    void shouldReturnResultEvenThoughPostconditionFails() throws Throwable {
        String code = "package pl.coco.compiler;\n"
                + "\n"
                + "import pl.coco.api.code.Contract;\n"
                + "\n"
                + "public class Test {\n"
                + "\n"
                + "    private static int val = 0;\n"
                + "\n"
                + "    public static int entry() {\n"
                + "        return testedMethod();\n"
                + "    }\n"
                + "\n"
                + "    public static int testedMethod() {\n"
                + "        Contract.ensures(val == 1);\n"
                + "        return 42;\n"
                + "    }\n"
                + "}\n";

        Object result = JavacTestUtils.compileAndRun(CLASS_NAME, ENTRY_POINT, code, DISABLED);

        assertThat(result).isEqualTo(RESULT);
    }

    @DisplayName("Method returns result even though invariant fails")
    @Test
    void shouldReturnResultEvenThoughInvariantFails() throws Throwable {
        String code = "package pl.coco.compiler;\n"
                + "\n"
                + "import pl.coco.api.code.Contract;\n"
                + "import pl.coco.api.code.InvariantMethod;\n"
                + "\n"
                + "public class Test {\n"
                + "\n"
                + "    private static int val = 0;\n"
                + "\n"
                + "    public static int entry() {\n"
                + "        Test instance = new Test();\n"
                + "        val = -1;\n"
                + "        return instance.testedMethod();\n"
                + "    }\n"
                + "\n"
                + "    private int testedMethod() {\n"
                + "        return 42;\n"
                + "    }\n"
                + "\n"
                + "    @InvariantMethod"
                + "    void invariantMethod() {\n"
                + "        Contract.invariant(val >= 0);\n"
                + "    }\n"
                + "\n"
                + "}\n";

        Object result = JavacTestUtils.compileAndRun(CLASS_NAME, ENTRY_POINT, code, DISABLED);

        assertThat(result).isEqualTo(RESULT);
    }
}
