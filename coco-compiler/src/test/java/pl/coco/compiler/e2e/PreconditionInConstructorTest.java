package pl.coco.compiler.e2e;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import pl.coco.util.JavacTestUtils;

class PreconditionInConstructorTest {

    private static final String QUALIFIED_CLASS_NAME = "pl.coco.compiler.Test";
    private static final String ENTRY_POINT = "entry";
    private static final int RESULT = 42;

    @DisplayName("Precondition in constructor passes")
    @Test
    void shouldReturnWhenContractInConstructorPasses() throws Throwable {
        String code = "package pl.coco.compiler;\n"
                + "\n"
                + "import pl.coco.api.Contract;\n"
                + "\n"
                + "public class Test {\n"
                + "\n"
                + "    private int arg = -1;\n"
                + "\n"
                + "    public Test(int arg) {\n"
                + "        Contract.requires(arg >= 0);\n" +
                "          this.arg = arg;\n" +
                "      }\n"
                + "\n"
                + "    public static int entry() {\n"
                + "        Test instance = new Test(42);\n"
                + "        return instance.arg;\n"
                + "    }\n"
                + "\n"
                + "}\n";

        Object actual = JavacTestUtils.compileAndRun(QUALIFIED_CLASS_NAME, ENTRY_POINT, code);

        assertThat(actual).isEqualTo(RESULT);
    }
}
