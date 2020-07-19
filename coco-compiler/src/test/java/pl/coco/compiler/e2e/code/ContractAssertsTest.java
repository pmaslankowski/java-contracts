package pl.coco.compiler.e2e.code;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import pl.coco.api.ContractFailedException;
import pl.coco.util.CocoTestUtils;

class ContractAssertsTest {

    private static final String QUALIFIED_CLASS_NAME = "pl.coco.compiler.Test";
    private static final String ENTRY_POINT = "entry";

    @DisplayName("Should throw exception when assertion fails")
    @Test
    void shouldThrowExceptionWhenAssertionFails() {
        String code = "package pl.coco.compiler;\n"
                + "\n"
                + "import pl.coco.api.code.Contract;\n"
                + "\n"
                + "public class Test {\n"
                + "\n"
                + "    public static int entry() {\n"
                + "        return testedMethod(0);\n"
                + "    }\n"
                + "\n"
                + "    public static int testedMethod(int arg) {\n"
                + "        Contract.invariant(arg > 0);\n"
                + "        return 42;\n"
                + "    }\n"
                + "}\n";

        Throwable thrown = catchThrowable(
                () -> CocoTestUtils.compileAndRun(QUALIFIED_CLASS_NAME, ENTRY_POINT, code));

        assertThat(thrown)
                .isInstanceOf(ContractFailedException.class)
                .hasMessage("Assertion \"arg > 0\" is not satisfied.");
    }

    @DisplayName("Should return result when assertion holds")
    @Test
    void shouldReturnResultWhenPreconditionOnStaticMethodPasses() throws Throwable {
        String code = "package pl.coco.compiler;\n"
                + "\n"
                + "import pl.coco.api.code.Contract;\n"
                + "\n"
                + "public class Test {\n"
                + "\n"
                + "    public static int entry() {\n"
                + "        return testedMethod(0);\n"
                + "    }\n"
                + "\n"
                + "    public static int testedMethod(int arg) {\n"
                + "        Contract.invariant(arg >= 0);\n"
                + "        return 42;\n"
                + "    }\n"
                + "}\n";


        Object actual = CocoTestUtils.compileAndRun(QUALIFIED_CLASS_NAME, ENTRY_POINT, code);

        assertThat(actual).isEqualTo(42);
    }
}
