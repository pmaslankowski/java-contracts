package pl.coco.compiler.e2e.annotation;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import pl.coco.api.ContractFailedException;
import pl.coco.util.CocoTestUtils;

class AnnotationInvariantsTest {

    private static final String QUALIFIED_CLASS_NAME = "pl.coco.compiler.Test";
    private static final String ENTRY_POINT = "entry";
    private static final int RESULT = 42;

    @DisplayName("Invariant on static method is ignored")
    @Test
    void shouldReturnResultFromStaticMethodWhenClassHasInvariantEvenThoughItsNotSatisfied()
            throws Throwable {

        String code = "package pl.coco.compiler;\n"
                + "\n"
                + "import pl.coco.api.annotation.Invariant;\n"
                + "\n"
                + "@Invariant(\"val >= 0\")"
                + "public class Test {\n"
                + "\n"
                + "    private static int val = -1;\n"
                + "\n"
                + "    public static int entry() {\n"
                + "        return 42;\n"
                + "    }\n"
                + "\n"
                + "}\n";

        Object actual = CocoTestUtils.compileAndRun(QUALIFIED_CLASS_NAME, ENTRY_POINT, code);

        assertThat(actual).isEqualTo(RESULT);
    }

    @DisplayName("Invariant on instance method passes")
    @Test
    void shouldReturnResultFromInstanceMethodWhenInvariantPasses() throws Throwable {
        String code = "package pl.coco.compiler;\n"
                + "\n"
                + "import pl.coco.api.annotation.Invariant;\n"
                + "\n"
                + "@Invariant(\"val >= 0\")"
                + "public class Test {\n"
                + "\n"
                + "    private int val = 0;\n"
                + "\n"
                + "    public static int entry() {\n"
                + "        Test instance = new Test();\n"
                + "        return instance.testedMethod();\n"
                + "    }\n"
                + "\n"
                + "    private int testedMethod() {\n"
                + "        return 42;\n"
                + "    }\n"
                + "\n"
                + "}\n";

        Object actual = CocoTestUtils.compileAndRun(QUALIFIED_CLASS_NAME, ENTRY_POINT, code);

        assertThat(actual).isEqualTo(RESULT);
    }

    @DisplayName("Invariant on instance method fails before")
    @Test
    void shouldThrowExceptionFromInstanceMethodWhenInvariantFailsBefore() {
        String code = "package pl.coco.compiler;\n"
                + "\n"
                + "import pl.coco.api.annotation.Invariant;\n"
                + "\n"
                + "@Invariant(\"val >= 0\")"
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
                + "}\n";

        Throwable thrown = catchThrowable(
                () -> CocoTestUtils.compileAndRun(QUALIFIED_CLASS_NAME, ENTRY_POINT, code));

        assertThat(thrown)
                .isInstanceOf(ContractFailedException.class)
                .hasMessage("Invariant \"val >= 0\" is not satisfied before the method call");
    }

    @DisplayName("Invariant on instance method fails after")
    @Test
    void shouldThrowExceptionFromInstanceMethodWhenInvariantFailsAfter() {
        String code = "package pl.coco.compiler;\n"
                + "\n"
                + "import pl.coco.api.annotation.Invariant;\n"
                + "\n"
                + "@Invariant(\"val >= 0\")"
                + "public class Test {\n"
                + "\n"
                + "    private static int val = 0;\n"
                + "\n"
                + "    public static int entry() {\n"
                + "        Test instance = new Test();\n"
                + "        return instance.testedMethod();\n"
                + "    }\n"
                + "\n"
                + "    private int testedMethod() {\n"
                + "        val = -1;\n"
                + "        return 42;\n"
                + "    }\n"
                + "\n"
                + "}\n";

        Throwable thrown = catchThrowable(
                () -> CocoTestUtils.compileAndRun(QUALIFIED_CLASS_NAME, ENTRY_POINT, code));

        assertThat(thrown)
                .isInstanceOf(ContractFailedException.class)
                .hasMessage("Invariant \"val >= 0\" is not satisfied after the method call");
    }
}
