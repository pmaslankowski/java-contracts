package pl.coco.compiler.e2e.code;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import pl.coco.api.ContractFailedException;
import pl.coco.util.CocoTestUtils;

public class ContractEnsuresSelfTest {

    private static final String QUALIFIED_CLASS_NAME = "pl.coco.compiler.Test";
    private static final String ENTRY_POINT = "entry";
    private static final int RESULT = 42;

    @DisplayName("Simple postcondition on static method passes")
    @Test
    void shouldReturnResultWhenPostconditionOnStaticMethodPasses() throws Throwable {

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
                + "        Contract.ensuresSelf(val == 1);\n"
                + "        val = 1;\n"
                + "        return 42;\n"
                + "    }\n"
                + "}\n";

        Object actual = CocoTestUtils.compileAndRun(QUALIFIED_CLASS_NAME, ENTRY_POINT, code);

        assertThat(actual).isEqualTo(RESULT);
    }

    @DisplayName("Simple postcondition on static method fails")
    @Test
    void shouldThrowExceptionWhenPostconditionOnStaticMethodFails() {
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
                + "        Contract.ensuresSelf(val == 1);\n"
                + "        return 42;\n"
                + "    }\n"
                + "}\n";

        Throwable thrown = catchThrowable(
                () -> CocoTestUtils.compileAndRun(QUALIFIED_CLASS_NAME, ENTRY_POINT, code));

        assertThat(thrown)
                .isInstanceOf(ContractFailedException.class)
                .hasMessage("Postcondition \"val == 1\" is not satisfied.");
    }
}
