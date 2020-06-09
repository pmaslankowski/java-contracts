package pl.coco.compiler.e2e.annotation;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.catchThrowable;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import pl.coco.api.ContractFailedException;
import pl.coco.util.CocoTestUtils;

class AnnotationPostconditionsTest {

    private static final String QUALIFIED_CLASS_NAME = "pl.coco.compiler.Test";
    private static final String ENTRY_POINT = "entry";
    private static final int RESULT = 42;

    @DisplayName("Simple postcondition on static method passes")
    @Test
    void shouldReturnResultWhenPostconditionOnStaticMethodPasses() throws Throwable {

        String code = "package pl.coco.compiler;\n"
                + "\n"
                + "import pl.coco.api.annotation.Ensures;\n"
                + "\n"
                + "public class Test {\n"
                + "\n"
                + "    private static int val = 0;\n"
                + "\n"
                + "    public static int entry() {\n"
                + "        return testedMethod();\n"
                + "    }\n"
                + "\n"
                + "    @Ensures(\"val == 1\")"
                + "    public static int testedMethod() {\n"
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
                + "import pl.coco.api.annotation.Ensures;\n"
                + "\n"
                + "public class Test {\n"
                + "\n"
                + "    private static int val = 0;\n"
                + "\n"
                + "    public static int entry() {\n"
                + "        return testedMethod();\n"
                + "    }\n"
                + "\n"
                + "    @Ensures(\"val == 1\")"
                + "    public static int testedMethod() {\n"
                + "        return 42;\n"
                + "    }\n"
                + "}\n";

        Throwable thrown = catchThrowable(
                () -> CocoTestUtils.compileAndRun(QUALIFIED_CLASS_NAME, ENTRY_POINT, code));

        assertThat(thrown)
                .isInstanceOf(ContractFailedException.class)
                .hasMessage("Postcondition \"val == 1\" is not satisfied.");
    }

    @DisplayName("Simple postcondition on instance method passes")
    @Test
    void shouldReturnResultWhenPostconditionOnInstanceMethodPasses() throws Throwable {

        String code = "package pl.coco.compiler;\n"
                + "\n"
                + "import pl.coco.api.annotation.Ensures;\n"
                + "\n"
                + "public class Test {\n"
                + "\n"
                + "    private int val = 0;\n"
                + "\n"
                + "    public static int entry() {\n"
                + "        Test test = new Test();\n"
                + "        return test.testedMethod();"
                + "    }\n"
                + "\n"
                + "    @Ensures(\"this.val == 1\")"
                + "    private int testedMethod() {\n"
                + "        this.val = 1;\n"
                + "        return 42;\n"
                + "    }\n"
                + "\n"
                + "}\n";

        Object actual = CocoTestUtils.compileAndRun(QUALIFIED_CLASS_NAME, ENTRY_POINT, code);

        assertThat(actual).isEqualTo(RESULT);
    }

    @DisplayName("Simple postcondition on instance method without this passes")
    @Test
    void shouldReturnResultWhenPostconditionOnInstanceMethodWithoutThisPasses() throws Throwable {

        String code = "package pl.coco.compiler;\n"
                + "\n"
                + "import pl.coco.api.annotation.Ensures;\n"
                + "\n"
                + "public class Test {\n"
                + "\n"
                + "    private int val = 0;\n"
                + "\n"
                + "    public static int entry() {\n"
                + "        Test test = new Test();\n"
                + "        return test.testedMethod();"
                + "    }\n"
                + "\n"
                + "    @Ensures(\"this.val == 1\")"
                + "    private int testedMethod() {\n"
                + "        val = 1;\n"
                + "        return 42;\n"
                + "    }\n"
                + "\n"
                + "}\n";

        Object actual = CocoTestUtils.compileAndRun(QUALIFIED_CLASS_NAME, ENTRY_POINT, code);

        assertThat(actual).isEqualTo(RESULT);
    }

    @DisplayName("Simple postcondition on instance method without this passes")
    @Test
    void shouldThrowExceptionWhenPostconditionOnInstanceMethodWithoutThisFails() {

        String code = "package pl.coco.compiler;\n"
                + "\n"
                + "import pl.coco.api.annotation.Ensures;\n"
                + "\n"
                + "public class Test {\n"
                + "\n"
                + "    private int val = 0;\n"
                + "\n"
                + "    public static int entry() {\n"
                + "        Test test = new Test();\n"
                + "        return test.testedMethod();"
                + "    }\n"
                + "\n"
                + "    @Ensures(\"val == 1\")"
                + "    private int testedMethod() {\n"
                + "        return 42;\n"
                + "    }\n"
                + "\n"
                + "}\n";

        Throwable thrown = catchThrowable(
                () -> CocoTestUtils.compileAndRun(QUALIFIED_CLASS_NAME, ENTRY_POINT, code));

        assertThat(thrown)
                .isInstanceOf(ContractFailedException.class)
                .hasMessage("Postcondition \"val == 1\" is not satisfied.");
    }

    @DisplayName("Simple postcondition on instance method fails")
    @Test
    void shouldThrowExceptionWhenPreconditionOnInstanceMethodFails() {
        String code = "package pl.coco.compiler;\n"
                + "\n"
                + "import pl.coco.api.annotation.Ensures;\n"
                + "\n"
                + "public class Test {\n"
                + "\n"
                + "    private int val = 0;\n"
                + "\n"
                + "    public static int entry() {\n"
                + "        Test test = new Test();\n"
                + "        return test.testedMethod();"
                + "    }\n"
                + "\n"
                + "    @Ensures(\"this.val == 1\")"
                + "    private int testedMethod() {\n"
                + "        return 42;\n"
                + "    }\n"
                + "\n"
                + "}\n";

        Throwable thrown = catchThrowable(
                () -> CocoTestUtils.compileAndRun(QUALIFIED_CLASS_NAME, ENTRY_POINT, code));

        assertThat(thrown)
                .isInstanceOf(ContractFailedException.class)
                .hasMessage("Postcondition \"this.val == 1\" is not satisfied.");
    }

    @DisplayName("Simple postcondition on void method passes")
    @Test
    void shouldPassWhenPreconditionOnVoidMethodPasses() {
        String code = "package pl.coco.compiler;\n"
                + "\n"
                + "import pl.coco.api.annotation.Ensures;\n"
                + "\n"
                + "public class Test {\n"
                + "\n"
                + "    private int val = 0;\n"
                + "\n"
                + "    public static void entry() {\n"
                + "        Test instance = new Test();\n"
                + "        instance.testedMethod();\n"
                + "    }\n"
                + "\n"
                + "    @Ensures(\"this.val == 1\")"
                + "    public void testedMethod() {\n"
                + "        this.val = 1;\n"
                + "    }\n"
                + "}\n";

        assertThatCode(() -> CocoTestUtils.compileAndRun(QUALIFIED_CLASS_NAME, ENTRY_POINT, code))
                .doesNotThrowAnyException();
    }

    @DisplayName("Simple postcondition on void method fails")
    @Test
    void shouldFailWhenPreconditionOnVoidMethodFails() {
        String code = "package pl.coco.compiler;\n"
                + "\n"
                + "import pl.coco.api.annotation.Ensures;\n"
                + "\n"
                + "public class Test {\n"
                + "\n"
                + "    private int val = 0;\n"
                + "\n"
                + "    public static void entry() {\n"
                + "        Test instance = new Test();\n"
                + "        instance.testedMethod();\n"
                + "    }\n"
                + "\n"
                + "    @Ensures(\"this.val == 1\")"
                + "    public void testedMethod() {\n"
                + "    }\n"
                + "}\n";

        Throwable thrown = catchThrowable(
                () -> CocoTestUtils.compileAndRun(QUALIFIED_CLASS_NAME, ENTRY_POINT, code));

        assertThat(thrown)
                .isInstanceOf(ContractFailedException.class)
                .hasMessage("Postcondition \"this.val == 1\" is not satisfied.");
    }

    @DisplayName("Postcondition with result call inside unary operator passes")
    @Test
    void shouldReturnResultWhenPostconditionWithResultInsideUnaryOpPasses() throws Throwable {
        String code = "package pl.coco.compiler;\n"
                + "\n"
                + "import pl.coco.api.annotation.Ensures;\n"
                + "\n"
                + "public class Test {\n"
                + "\n"
                + "    public static boolean entry() {\n"
                + "        Test test = new Test();\n"
                + "        return test.testedMethod();"
                + "    }\n"
                + "\n"
                + "    @Ensures(\"!Contract.result(boolean.class)\")"
                + "    private boolean testedMethod() {\n"
                + "        return false;\n"
                + "    }\n"
                + "\n"
                + "}\n";

        Object actual = CocoTestUtils.compileAndRun(QUALIFIED_CLASS_NAME, ENTRY_POINT, code);

        assertThat(actual).isEqualTo(false);
    }

    @DisplayName("Postcondition with result call inside unary operator fails")
    @Test
    void shouldThrowExceptionWhenPostconditionWithResultInsideUnaryOpFails() {
        String code = "package pl.coco.compiler;\n"
                + "\n"
                + "import pl.coco.api.annotation.Ensures;\n"
                + "\n"
                + "public class Test {\n"
                + "\n"
                + "    public static boolean entry() {\n"
                + "        Test test = new Test();\n"
                + "        return test.testedMethod();"
                + "    }\n"
                + "\n"
                + "    @Ensures(\"!Contract.result(boolean.class)\")"
                + "    private boolean testedMethod() {\n"
                + "        return true;\n"
                + "    }\n"
                + "\n"
                + "}\n";

        Throwable thrown = catchThrowable(
                () -> CocoTestUtils.compileAndRun(QUALIFIED_CLASS_NAME, ENTRY_POINT, code));

        assertThat(thrown)
                .isInstanceOf(ContractFailedException.class)
                .hasMessage("Postcondition \"!Contract.result(boolean.class)\" is not satisfied.");
    }

    @DisplayName("Postcondition with result call inside binary operator passes")
    @Test
    void shouldReturnResultWhenPostconditionWithResultInsideBinaryOpPasses() throws Throwable {
        String code = "package pl.coco.compiler;\n"
                + "\n"
                + "import pl.coco.api.annotation.Ensures;\n"
                + "\n"
                + "public class Test {\n"
                + "\n"
                + "    public static int entry() {\n"
                + "        Test test = new Test();\n"
                + "        return test.testedMethod();"
                + "    }\n"
                + "\n"
                + "    @Ensures(\"Contract.result(int.class) == 42\")"
                + "    private int testedMethod() {\n"
                + "        return 42;\n"
                + "    }\n"
                + "\n"
                + "}\n";

        Object actual = CocoTestUtils.compileAndRun(QUALIFIED_CLASS_NAME, ENTRY_POINT, code);

        assertThat(actual).isEqualTo(42);
    }

    @DisplayName("Postcondition with result call inside binary operator fails")
    @Test
    void shouldThrowExceptionWhenPostconditionWithResultInsideBinaryOpFails() {
        String code = "package pl.coco.compiler;\n"
                + "\n"
                + "import pl.coco.api.annotation.Ensures;\n"
                + "\n"
                + "public class Test {\n"
                + "\n"
                + "    public static int entry() {\n"
                + "        Test test = new Test();\n"
                + "        return test.testedMethod();"
                + "    }\n"
                + "\n"
                + "    @Ensures(\"Contract.result(int.class) == 42\")"
                + "    private int testedMethod() {\n"
                + "        return 43;\n"
                + "    }\n"
                + "\n"
                + "}\n";

        Throwable thrown = catchThrowable(
                () -> CocoTestUtils.compileAndRun(QUALIFIED_CLASS_NAME, ENTRY_POINT, code));

        assertThat(thrown)
                .isInstanceOf(ContractFailedException.class)
                .hasMessage("Postcondition \"Contract.result(int.class) == 42\" is not satisfied.");
    }

    @DisplayName("Postcondition with result call inside ternary operator passes")
    @Test
    void shouldReturnResultWhenPostconditionWithResultInsideTernaryOpPasses() throws Throwable {
        String code = "package pl.coco.compiler;\n"
                + "\n"
                + "import pl.coco.api.annotation.Ensures;\n"
                + "\n"
                + "public class Test {\n"
                + "\n"
                + "    public static boolean entry() {\n"
                + "        Test test = new Test();\n"
                + "        return test.testedMethod();"
                + "    }\n"
                + "\n"
                + "    @Ensures(\"Contract.result(boolean.class) ? Contract.result(boolean.class) : Contract.result(boolean.class)\")"
                + "    private boolean testedMethod() {\n"
                + "        return true;\n"
                + "    }\n"
                + "\n"
                + "}\n";

        Object actual = CocoTestUtils.compileAndRun(QUALIFIED_CLASS_NAME, ENTRY_POINT, code);

        assertThat(actual).isEqualTo(true);
    }

    @DisplayName("Postcondition with result call inside ternary operator fails")
    @Test
    void shouldReturnResultWhenPostconditionWithResultInsideTernaryOpFails() {
        String code = "package pl.coco.compiler;\n"
                + "\n"
                + "import pl.coco.api.annotation.Ensures;\n"
                + "\n"
                + "public class Test {\n"
                + "\n"
                + "    public static boolean entry() {\n"
                + "        Test test = new Test();\n"
                + "        return test.testedMethod();"
                + "    }\n"
                + "\n"
                + "    @Ensures(\"Contract.result(boolean.class) ? Contract.result(boolean.class) : Contract.result(boolean.class)\")"
                + "    private boolean testedMethod() {\n"
                + "        return false;\n"
                + "    }\n"
                + "\n"
                + "}\n";

        Throwable thrown = catchThrowable(
                () -> CocoTestUtils.compileAndRun(QUALIFIED_CLASS_NAME, ENTRY_POINT, code));

        assertThat(thrown)
                .isInstanceOf(ContractFailedException.class);
    }

    @DisplayName("Exception thrown during postcondition evaluation fails")
    @Test
    void shouldThrowContractFailedExceptionWhenExceptionIsThrownDuringContractEvaluation() {
        String code = "package pl.coco.compiler;\n"
                + "\n"
                + "import pl.coco.api.annotation.Ensures;\n"
                + "\n"
                + "public class Test {\n"
                + "\n"
                + "    public static void entry() {\n"
                + "        testedMethod();\n"
                + "    }\n"
                + "\n"
                + "    @Ensures(\"exception() >= 0\")"
                + "    public static void testedMethod() {\n"
                + "    }\n"
                + "\n"
                + "   public static int exception() {\n"
                + "       throw new RuntimeException(\"Test exception\");\n"
                + "   }\n"
                + "}\n";

        Throwable thrown = catchThrowable(
                () -> CocoTestUtils.compileAndRun(QUALIFIED_CLASS_NAME, ENTRY_POINT, code));

        assertThat(thrown)
                .isInstanceOf(ContractFailedException.class)
                .hasMessage("An exception has been thrown during contract evaluation:")
                .hasCause(new RuntimeException("Test exception"));
    }
}
