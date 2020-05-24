package pl.coco.compiler.e2e.code;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.catchThrowable;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import pl.coco.api.ContractFailedException;
import pl.coco.util.JavacTestUtils;

class ContractRequiresTest {

    private static final String QUALIFIED_CLASS_NAME = "pl.coco.compiler.Test";
    private static final String ENTRY_POINT = "entry";
    private static final int RESULT = 42;

    @DisplayName("Precondition on static method passes")
    @Test
    void shouldReturnResultWhenPreconditionOnStaticMethodPasses() throws Throwable {
        String code = "package pl.coco.compiler;\n"
                + "\n"
                + "import pl.coco.api.code.Contract;\n"
                + "\n"
                + "public class Test {\n"
                + "\n"
                + "    public static int entry() {\n"
                + "        return testedMethod(1, true);\n"
                + "    }\n"
                + "\n"
                + "    public static int testedMethod(int arg, boolean flag) {\n"
                + "        Contract.requires(arg >= 0 && flag);\n"
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
                + "import pl.coco.api.code.Contract;\n"
                + "\n"
                + "public class Test {\n"
                + "\n"
                + "    public static int entry() {\n"
                + "        Test test = new Test();\n"
                + "        return test.testedMethod(1, true);"
                + "    }\n"
                + "\n"
                + "    private int testedMethod(int i, boolean flag) {\n"
                + "        Contract.requires(i >= 0 && flag);\n"
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
                + "import pl.coco.api.code.Contract;\n"
                + "\n"
                + "public class Test {\n"
                + "\n"
                + "    public static int entry() {\n"
                + "        Test test = new Test();\n"
                + "        return test.testedMethod(-1);"
                + "    }\n"
                + "\n"
                + "    private int testedMethod(int i) {\n"
                + "        Contract.requires(i >= 0);\n"
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
                + "import pl.coco.api.code.Contract;\n"
                + "\n"
                + "public class Test {\n"
                + "\n"
                + "    public static void entry() {\n"
                + "        testedMethod();\n"
                + "    }\n"
                + "\n"
                + "    public static void testedMethod() {\n"
                + "        Contract.requires(1 >= 0);\n"
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
                + "import pl.coco.api.code.Contract;\n"
                + "\n"
                + "public class Test {\n"
                + "\n"
                + "    public static void entry() {\n"
                + "        testedMethod();\n"
                + "    }\n"
                + "\n"
                + "    public static void testedMethod() {\n"
                + "        Contract.requires(-1 >= 0);\n"
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
                + "import pl.coco.api.code.Contract;\n"
                + "\n"
                + "public class Test {\n"
                + "\n"
                + "    public static int entry() {\n"
                + "        Test test = new Test();\n"
                + "        return test.testedMethod(10);"
                + "    }\n"
                + "\n"
                + "    private <T> int testedMethod(T i) {\n"
                + "        Contract.requires(i != null);\n"
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
                + "import pl.coco.api.code.Contract;\n"
                + "\n"
                + "public class Test {\n"
                + "\n"
                + "    public static int entry() {\n"
                + "        Test test = new Test();\n"
                + "        return test.testedMethod(null);"
                + "    }\n"
                + "\n"
                + "    private <T> int testedMethod(T i) {\n"
                + "        Contract.requires(i != null);\n"
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

    @DisplayName("Precondition as fully qualified Contract.requires passes")
    @Test
    void shouldReturnResultWhenFullyQualifiedPreconditionPasses() throws Throwable {
        String code = "package pl.coco.compiler;\n"
                + "\n"
                + "public class Test {\n"
                + "\n"
                + "    public static int entry() {\n"
                + "        return testedMethod(1, true);\n"
                + "    }\n"
                + "\n"
                + "    public static int testedMethod(int arg, boolean flag) {\n"
                + "        pl.coco.api.code.Contract.requires(arg >= 0 && flag);\n"
                + "        return 42;\n"
                + "    }\n"
                + "}\n";

        Object actual = JavacTestUtils.compileAndRun(QUALIFIED_CLASS_NAME, ENTRY_POINT, code);

        assertThat(actual).isEqualTo(RESULT);
    }

    @DisplayName("Precondition as fully qualified Contract.requires fails")
    @Test
    void shouldThrowExceptionWhenFullyQualifiedPreconditionFails() {
        String code = "package pl.coco.compiler;\n"
                + "\n"
                + "public class Test {\n"
                + "\n"
                + "    public static int entry() {\n"
                + "        return testedMethod(-1);\n"
                + "    }\n"
                + "\n"
                + "    public static int testedMethod(int arg) {\n"
                + "        pl.coco.api.code.Contract.requires(arg >= 0);\n"
                + "        return 42;\n"
                + "    }\n"
                + "}\n";

        Throwable thrown = catchThrowable(
                () -> JavacTestUtils.compileAndRun(QUALIFIED_CLASS_NAME, ENTRY_POINT, code));

        assertThat(thrown)
                .isInstanceOf(ContractFailedException.class)
                .hasMessage("Precondition \"arg >= 0\" is not satisfied.");
    }

    @DisplayName("Exception thrown during precondition evaluation fails")
    @Test
    void shouldThrowContractFailedExceptionWhenExceptionIsThrownDuringContractEvaluation() {
        String code = "package pl.coco.compiler;\n"
                + "\n"
                + "import pl.coco.api.code.Contract;\n"
                + "\n"
                + "public class Test {\n"
                + "\n"
                + "    public static void entry() {\n"
                + "        testedMethod();\n"
                + "    }\n"
                + "\n"
                + "    public static void testedMethod() {\n"
                + "        Contract.requires(exception() >= 0);\n"
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
