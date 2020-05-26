package pl.coco.compiler.e2e.annotation;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.catchThrowable;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import pl.coco.api.ContractFailedException;
import pl.coco.util.JavacTestUtils;

class AnnotationPreconditionsTest {

    private static final String QUALIFIED_CLASS_NAME = "pl.coco.compiler.Test";
    private static final String ENTRY_POINT = "entry";
    private static final int RESULT = 42;

    @DisplayName("Precondition on static method passes")
    @Test
    void shouldReturnResultWhenPreconditionOnStaticMethodPasses() throws Throwable {
        String code = "package pl.coco.compiler;\n"
                + "\n"
                + "import pl.coco.api.annotation.Requires;\n"
                + "\n"
                + "public class Test {\n"
                + "\n"
                + "    public static int entry() {\n"
                + "        return testedMethod(1, true);\n"
                + "    }\n"
                + "\n"
                + "    @Requires(\"arg >= 0 && flag\")"
                + "    public static int testedMethod(int arg, boolean flag) {\n"
                + "        return 42;\n"
                + "    }\n"

                + "}\n";

        Object actual = JavacTestUtils.compileAndRun(QUALIFIED_CLASS_NAME, ENTRY_POINT, code);

        assertThat(actual).isEqualTo(RESULT);
    }

    @DisplayName("Precondition on static method fails")
    @Test
    void shouldThrowExceptionWhenPreconditionOnStaticMethodFails() {
        String code = "package pl.coco.compiler;\n"
                + "\n"
                + "import pl.coco.api.annotation.Requires;\n"
                + "\n"
                + "public class Test {\n"
                + "    public static int entry() {\n"
                + "        return testedMethod(-1);\n"
                + "    }\n"
                + "\n"
                + "    @Requires(\"arg >= 0\")"
                + "    private static int testedMethod(int arg) {\n"
                + "        return 42;\n"
                + "    }\n"
                + "\n"
                + "}\n";

        Throwable thrown = catchThrowable(
                () -> JavacTestUtils.compileAndRun(QUALIFIED_CLASS_NAME, ENTRY_POINT, code));

        assertThat(thrown)
                .isInstanceOf(ContractFailedException.class)
                .hasMessage("Precondition \"arg >= 0\" is not satisfied.");
    }

    @DisplayName("Precondition on instance method passes")
    @Test
    void shouldReturnResultWhenPreconditionOnInstanceMethodPasses() throws Throwable {
        String code = "package pl.coco.compiler;\n"
                + "\n"
                + "import pl.coco.api.annotation.Requires;\n"
                + "\n"
                + "public class Test {\n"
                + "\n"
                + "    public static int entry() {\n"
                + "        Test test = new Test();\n"
                + "        return test.testedMethod(1, true);"
                + "    }\n"
                + "\n"
                + "    @Requires(\"i >= 0 && flag\")"
                + "    private int testedMethod(int i, boolean flag) {\n"
                + "        return 42;\n"
                + "    }\n"
                + "\n"
                + "}\n";

        Object actual = JavacTestUtils.compileAndRun(QUALIFIED_CLASS_NAME, ENTRY_POINT, code);

        assertThat(actual).isEqualTo(RESULT);
    }

    @DisplayName("Precondition on instance method fails")
    @Test
    void shouldThrowExceptionWhenPreconditionOnInstanceMethodFails() {
        String code = "package pl.coco.compiler;\n"
                + "\n"
                + "import pl.coco.api.annotation.Requires;\n"
                + "\n"
                + "public class Test {\n"
                + "\n"
                + "    public static int entry() {\n"
                + "        Test test = new Test();\n"
                + "        return test.testedMethod(-1);"
                + "    }\n"
                + "\n"
                + "    @Requires(\"i >=0\")"
                + "    private int testedMethod(int i) {\n"
                + "        return 42;\n"
                + "    }\n"
                + "\n"
                + "}\n";

        Throwable thrown = catchThrowable(
                () -> JavacTestUtils.compileAndRun(QUALIFIED_CLASS_NAME, ENTRY_POINT, code));

        assertThat(thrown)
                .isInstanceOf(ContractFailedException.class)
                .hasMessage("Precondition \"i >= 0\" is not satisfied.");
    }

    @DisplayName("Precondition on void method passes")
    @Test
    void shouldPassWhenPreconditionOnVoidMethodPasses() {
        String code = "package pl.coco.compiler;\n"
                + "\n"
                + "import pl.coco.api.annotation.Requires;\n"
                + "\n"
                + "public class Test {\n"
                + "\n"
                + "    public static void entry() {\n"
                + "        testedMethod();\n"
                + "    }\n"
                + "\n"
                + "    @Requires(\"1 >= 0\")"
                + "    public static void testedMethod() {\n"
                + "    }\n"
                + "}\n";

        assertThatCode(() -> JavacTestUtils.compileAndRun(QUALIFIED_CLASS_NAME, ENTRY_POINT, code))
                .doesNotThrowAnyException();
    }

    @DisplayName("Precondition on void method fails")
    @Test
    void shouldFailWhenPreconditionOnVoidMethodFails() {
        String code = "package pl.coco.compiler;\n"
                + "\n"
                + "import pl.coco.api.annotation.Requires;\n"
                + "\n"
                + "public class Test {\n"
                + "\n"
                + "    public static void entry() {\n"
                + "        testedMethod();\n"
                + "    }\n"
                + "\n"
                + "    @Requires(\"-1 >= 0\")"
                + "    public static void testedMethod() {\n"
                + "    }\n"
                + "}\n";

        Throwable thrown = catchThrowable(
                () -> JavacTestUtils.compileAndRun(QUALIFIED_CLASS_NAME, ENTRY_POINT, code));

        assertThat(thrown)
                .isInstanceOf(ContractFailedException.class)
                .hasMessage("Precondition \"-1 >= 0\" is not satisfied.");
    }

    @DisplayName("Precondition on instance method with generics passes")
    @Test
    void shouldReturnResultWhenPreconditionOnInstanceMethodWithGenericPasses() throws Throwable {
        String code = "package pl.coco.compiler;\n"
                + "\n"
                + "import pl.coco.api.annotation.Requires;\n"
                + "\n"
                + "public class Test {\n"
                + "\n"
                + "    public static int entry() {\n"
                + "        Test test = new Test();\n"
                + "        return test.testedMethod(10);"
                + "    }\n"
                + "\n"
                + "    @Requires(\"i != null\")"
                + "    private <T> int testedMethod(T i) {\n"
                + "        return 42;\n"
                + "    }\n"
                + "\n"
                + "}\n";

        Object actual = JavacTestUtils.compileAndRun(QUALIFIED_CLASS_NAME, ENTRY_POINT, code);

        assertThat(actual).isEqualTo(RESULT);
    }

    @DisplayName("Precondition on instance method with generics fails")
    @Test
    void shouldThrowExceptionWhenPreconditionOnInstanceMethodWithGenericsFails() {
        String code = "package pl.coco.compiler;\n"
                + "\n"
                + "import pl.coco.api.annotation.Requires;\n"
                + "\n"
                + "public class Test {\n"
                + "\n"
                + "    public static int entry() {\n"
                + "        Test test = new Test();\n"
                + "        return test.testedMethod(null);"
                + "    }\n"
                + "\n"
                + "    @Requires(\"i != null\")"
                + "    private <T> int testedMethod(T i) {\n"
                + "        return 42;\n"
                + "    }\n"
                + "\n"
                + "}\n";

        Throwable thrown = catchThrowable(
                () -> JavacTestUtils.compileAndRun(QUALIFIED_CLASS_NAME, ENTRY_POINT, code));

        assertThat(thrown)
                .isInstanceOf(ContractFailedException.class)
                .hasMessage("Precondition \"i != null\" is not satisfied.");
    }

    @DisplayName("Exception thrown during precondition evaluation fails")
    @Test
    void shouldThrowContractFailedExceptionWhenExceptionIsThrownDuringContractEvaluation() {
        String code = "package pl.coco.compiler;\n"
                + "\n"
                + "import pl.coco.api.annotation.Requires;\n"
                + "\n"
                + "public class Test {\n"
                + "\n"
                + "    public static void entry() {\n"
                + "        testedMethod();\n"
                + "    }\n"
                + "\n"
                + "    @Requires(\"exception() >= 0\")"
                + "    public static void testedMethod() {\n"
                + "    }\n"
                + "\n"
                + "   public static int exception() {\n"
                + "       throw new RuntimeException(\"Test exception\");\n"
                + "   }\n"
                + "}\n";

        Throwable thrown = catchThrowable(
                () -> JavacTestUtils.compileAndRun(QUALIFIED_CLASS_NAME, ENTRY_POINT, code));

        assertThat(thrown)
                .isInstanceOf(ContractFailedException.class)
                .hasMessage("An exception has been thrown during contract evaluation:")
                .hasCause(new RuntimeException("Test exception"));
    }
}
