package pl.coco.compiler.e2e;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import pl.coco.compiler.validation.ContractError;
import pl.coco.util.CompilationFailedException;
import pl.coco.util.JavacTestUtils;

class ContractValidationTest {

    private static final String QUALIFIED_CLASS_NAME = "pl.coco.compiler.Test";
    private static final String ENTRY_POINT = "entry";

    @DisplayName("Compilation error when contract is not the first statement")
    @Test
    void shouldProduceErrorWhenContractIsNotTheFirstStatementInAMethod() {

        String code = "package pl.coco.compiler;\n"
                + "\n"
                + "import pl.coco.api.Contract;\n"
                + "\n"
                + "public class Test {\n"
                + "\n"
                + "    public static int entry() {\n"
                + "        return testedMethod(1, true);\n"
                + "    }\n"
                + "\n"
                + "    public static int testedMethod(int arg, boolean flag) {\n"
                + "        int tmp = 42;\n"
                + "        Contract.requires(arg >= 0 && flag);\n"
                + "        return tmp;\n"
                + "    }\n"
                + "}\n";

        Throwable thrown = catchThrowable(
                () -> JavacTestUtils.compileAndRun(QUALIFIED_CLASS_NAME, ENTRY_POINT, code));

        assertThat(thrown)
                .isInstanceOf(CompilationFailedException.class)
                .hasMessageStartingWith("/coco/compiler/Test.java:13: error")
                .hasMessageContaining(
                        ContractError.CONTRACT_CAN_OCCUR_IN_BLOCK_AT_THE_BEGINNING_OF_THE_METHOD
                                .getMessage());

    }

    @DisplayName("Compilation error when contract block is interspersed with other instruction")
    @Test
    void shouldProduceErrorWhenContractBlockIsInterspersedWithOtherInstruction() {

        String code = "package pl.coco.compiler;\n"
                + "\n"
                + "import pl.coco.api.Contract;\n"
                + "\n"
                + "public class Test {\n"
                + "\n"
                + "    public static int entry() {\n"
                + "        return testedMethod(1, true);\n"
                + "    }\n"
                + "\n"
                + "    public static int testedMethod(int arg, boolean flag) {\n"
                + "        Contract.requires(arg >= 0 && flag);\n"
                + "        int tmp = 42;\n" +
                "          Contract.ensures(Contract.result(Integer.class) == 42);\n"
                + "        return tmp;\n"
                + "    }\n"
                + "}\n";

        Throwable thrown = catchThrowable(
                () -> JavacTestUtils.compileAndRun(QUALIFIED_CLASS_NAME, ENTRY_POINT, code));

        assertThat(thrown)
                .isInstanceOf(CompilationFailedException.class)
                .hasMessageStartingWith("/coco/compiler/Test.java:13: error")
                .hasMessageContaining(
                        ContractError.CONTRACT_BLOCK_CAN_CONTAIN_ONLY_CONTRACTS.getMessage());
    }

    @DisplayName("Compilation error when Contract.result occurs outside contracts")
    @Test
    void shouldProduceErrorWhenResultCallOccursOutsideContracts() {

        String code = "package pl.coco.compiler;\n"
                + "\n"
                + "import pl.coco.api.Contract;\n"
                + "\n"
                + "public class Test {\n"
                + "\n"
                + "    public static int entry() {\n"
                + "        return testedMethod(1, true);\n"
                + "    }\n"
                + "\n"
                + "    public static int testedMethod(int arg, boolean flag) {\n"
                + "        Contract.requires(arg >= 0 && flag);\n"
                + "        Contract.result(Integer.class);\n"
                + "        return 42;\n"
                + "    }\n"
                + "}\n";

        Throwable thrown = catchThrowable(
                () -> JavacTestUtils.compileAndRun(QUALIFIED_CLASS_NAME, ENTRY_POINT, code));

        assertThat(thrown)
                .isInstanceOf(CompilationFailedException.class)
                .hasMessageStartingWith("/coco/compiler/Test.java:13: error")
                .hasMessageContaining(
                        ContractError.RESULT_CAN_BE_PLACED_INSIDE_ENSURES_IN_NON_VOID_METHODS_ONLY
                                .getMessage());
    }

    @DisplayName("Compilation error when Contract.result occurs in precondition declaration")
    @Test
    void shouldProduceErrorWhenResultCallOccursInPreconditionDeclaration() {

        String code = "package pl.coco.compiler;\n"
                + "\n"
                + "import pl.coco.api.Contract;\n"
                + "\n"
                + "public class Test {\n"
                + "\n"
                + "    public static int entry() {\n"
                + "        return testedMethod(1, true);\n"
                + "    }\n"
                + "\n"
                + "    public static int testedMethod(int arg, boolean flag) {\n"
                + "        Contract.requires(Contract.result(Integer.class) >= 0);\n"
                + "        return 42;\n"
                + "    }\n"
                + "}\n";

        Throwable thrown = catchThrowable(
                () -> JavacTestUtils.compileAndRun(QUALIFIED_CLASS_NAME, ENTRY_POINT, code));

        assertThat(thrown)
                .isInstanceOf(CompilationFailedException.class)
                .hasMessageStartingWith("/coco/compiler/Test.java:12: error")
                .hasMessageContaining(
                        ContractError.RESULT_CAN_BE_PLACED_INSIDE_ENSURES_IN_NON_VOID_METHODS_ONLY
                                .getMessage());

    }

    @DisplayName("Compilation error when Contract.result occurs in void method")
    @Test
    void shouldProduceErrorWhenResultCallOccursInVoidMethodDeclaration() {

        String code = "package pl.coco.compiler;\n"
                + "\n"
                + "import pl.coco.api.Contract;\n"
                + "\n"
                + "public class Test {\n"
                + "\n"
                + "    public static void entry() {\n"
                + "        testedMethod(1, true);\n"
                + "    }\n"
                + "\n"
                + "    public static void testedMethod(int arg, boolean flag) {\n"
                + "        Contract.ensures(Contract.result(Integer.class) >= 0);\n"
                + "    }\n"
                + "}\n";

        Throwable thrown = catchThrowable(
                () -> JavacTestUtils.compileAndRun(QUALIFIED_CLASS_NAME, ENTRY_POINT, code));

        assertThat(thrown)
                .isInstanceOf(CompilationFailedException.class)
                .hasMessageStartingWith("/coco/compiler/Test.java:12: error")
                .hasMessageContaining(
                        ContractError.RESULT_CAN_BE_PLACED_INSIDE_ENSURES_IN_NON_VOID_METHODS_ONLY
                                .getMessage());
    }

    @DisplayName("Compilation error when Contract.result occurs in constructor")
    @Test
    void shouldProduceErrorWhenResultCallOccursInConstructor() {

        String code = "package pl.coco.compiler;\n"
                + "\n"
                + "import pl.coco.api.Contract;\n"
                + "\n"
                + "public class Test {\n"
                + "\n"
                + "    private int val = 0;\n"
                + "\n"
                + "    public static void entry() {\n"
                + "        Test instance = new Test();\n"
                + "    }\n"
                + "\n"
                + "    public Test() {\n"
                + "        Contract.ensures(Contract.result(Integer.class) >= 0);\n"
                + "    }\n"
                + "}\n";

        Throwable thrown = catchThrowable(
                () -> JavacTestUtils.compileAndRun(QUALIFIED_CLASS_NAME, ENTRY_POINT, code));

        assertThat(thrown)
                .isInstanceOf(CompilationFailedException.class)
                .hasMessageStartingWith("/coco/compiler/Test.java:14: error")
                .hasMessageContaining(
                        ContractError.RESULT_CAN_BE_PLACED_INSIDE_ENSURES_IN_NON_VOID_METHODS_ONLY
                                .getMessage());
    }

    @DisplayName("Compilation error when Contract.result type does not match method type")
    @Test
    void shouldProduceErrorWhenResultTypeDoesNotMatchMethodType() {

        String code = "package pl.coco.compiler;\n"
                + "\n"
                + "import pl.coco.api.Contract;\n"
                + "\n"
                + "public class Test {\n"
                + "\n"
                + "    public static int entry() {\n"
                + "        return testedMethod(1, true);\n"
                + "    }\n"
                + "\n"
                + "    public static int testedMethod(int arg, boolean flag) {\n"
                + "        Contract.ensures(Contract.result(Character.class) == 'a');\n"
                + "        return 42;\n"
                + "    }\n"
                + "}\n";

        Throwable thrown = catchThrowable(
                () -> JavacTestUtils.compileAndRun(QUALIFIED_CLASS_NAME, ENTRY_POINT, code));

        assertThat(thrown)
                .isInstanceOf(CompilationFailedException.class)
                .hasMessageStartingWith("/coco/compiler/Test.java:12: error")
                .hasMessageContaining(
                        ContractError.RESULT_TYPE_MUST_MATCH_METHOD_TYPE.getMessage());
    }

    @DisplayName("No compilation errors when Contract.result matches boxed method type")
    @Test
    void shouldNotProduceErrorWhenResultTypeMatchesBoxedType() throws Throwable {

        String code = "package pl.coco.compiler;\n"
                + "\n"
                + "import pl.coco.api.Contract;\n"
                + "\n"
                + "public class Test {\n"
                + "\n"
                + "    public static int entry() {\n"
                + "        return testedMethod(1, true);\n"
                + "    }\n"
                + "\n"
                + "    public static int testedMethod(int arg, boolean flag) {\n"
                + "        Contract.ensures(Contract.result(Integer.class) == 42);\n"
                + "        return 42;\n"
                + "    }\n"
                + "}\n";

        Object actual = JavacTestUtils.compileAndRun(QUALIFIED_CLASS_NAME, ENTRY_POINT, code);

        assertThat(actual).isEqualTo(42);
    }

    @DisplayName("Compilation error when Contract.invariant is used outside of invariant method")
    @Test
    void shouldProduceErrorWhenInvariantIsUsedOutsideInvariantMethod() {

        String code = "package pl.coco.compiler;\n"
                + "\n"
                + "import pl.coco.api.Contract;\n"
                + "\n"
                + "public class Test {\n"
                + "\n"
                + "    public static int entry() {\n"
                + "        return testedMethod(1, true);\n"
                + "    }\n"
                + "\n"
                + "    public static int testedMethod(int arg, boolean flag) {\n"
                + "        Contract.invariant(arg == 1);\n"
                + "        return 42;\n"
                + "    }\n"
                + "}\n";

        Throwable thrown = catchThrowable(
                () -> JavacTestUtils.compileAndRun(QUALIFIED_CLASS_NAME, ENTRY_POINT, code));

        assertThat(thrown)
                .isInstanceOf(CompilationFailedException.class)
                .hasMessageContaining(
                        ContractError.INVARIANT_CAN_OCCUR_IN_INVARIANT_METHODS_ONLY.getMessage());
    }

    @DisplayName("Compilation error when there are multiple invariant methods in the class")
    @Test
    void shouldProduceErrorWhenThereIsMoreThanOneInvariantMethodInClass() {

        String code = "package pl.coco.compiler;\n"
                + "\n"
                + "import pl.coco.api.Invariant;\n"
                + "\n"
                + "public class Test {\n"
                + "\n"
                + "    public static void entry() {\n"
                + "    }\n"
                + "\n"
                + "    @Invariant\n"
                + "    private void invariant1() {\n"
                + "    }\n"
                + "\n"
                + "    @Invariant\n"
                + "    private void invariant2() {\n"
                + "    }\n"
                + "}\n";

        Throwable thrown = catchThrowable(
                () -> JavacTestUtils.compileAndRun(QUALIFIED_CLASS_NAME, ENTRY_POINT, code));

        assertThat(thrown)
                .isInstanceOf(CompilationFailedException.class)
                .hasMessageContaining(
                        ContractError.MULTIPLE_INVARIANT_METHODS_IN_THE_SAME_CLASS.getMessage());
    }

    @Disabled
    @DisplayName("Compilation error when invariant method has wrong signature")
    @Test
    void shouldProduceErrorWhenInvariantMethodHasWrongSignature() {

        String code = "package pl.coco.compiler;\n"
                + "\n"
                + "import pl.coco.api.Invariant;\n"
                + "\n"
                + "public class Test {\n"
                + "\n"
                + "    public static void entry() {\n"
                + "    }\n"
                + "\n"
                + "    @Invariant\n"
                + "    private void invariant1(String arg) {\n"
                + "    }\n"
                + "\n"

                + "}\n";

        Throwable thrown = catchThrowable(
                () -> JavacTestUtils.compileAndRun(QUALIFIED_CLASS_NAME, ENTRY_POINT, code));

        assertThat(thrown)
                .isInstanceOf(CompilationFailedException.class)
                .hasMessageContaining(
                        ContractError.BAD_INVARIANT_METHOD_TYPE.getMessage());
    }

    @DisplayName("Compilation error when Contract.forAll(array, pred) is used outside of contracts")
    @Test
    void shouldProduceErrorWhenForAllArrayIsUsedOutsideOfContract() {

        String code = "package pl.coco.compiler;\n"
                + "\n"
                + "import pl.coco.api.Contract;\n"
                + "\n"
                + "public class Test {\n"
                + "\n"
                + "    public static void entry() {\n"
                + "        Contract.forAll(new Object[] {}, x -> true);\n"
                + "    }\n"
                + "\n"
                + "}\n";

        Throwable thrown = catchThrowable(
                () -> JavacTestUtils.compileAndRun(QUALIFIED_CLASS_NAME, ENTRY_POINT, code));

        assertThat(thrown)
                .isInstanceOf(CompilationFailedException.class)
                .hasMessageContaining(
                        ContractError.CONTRACT_STATEMENT_OUTSIDE_OF_CONTRACTS.getMessage());
    }

    @DisplayName("Compilation error when Contract.forAll(iter, pred) is used outside of contracts")
    @Test
    void shouldProduceErrorWhenForAllIterIsUsedOutsideOfContract() {

        String code = "package pl.coco.compiler;\n"
                + "\n"
                + "import java.util.Collections;\n"
                + "import pl.coco.api.Contract;\n"
                + "\n"
                + "public class Test {\n"
                + "\n"
                + "    public static void entry() {\n"
                + "        Contract.forAll(Collections.emptyList(), x -> true);\n"
                + "    }\n"
                + "\n"
                + "}\n";

        Throwable thrown = catchThrowable(
                () -> JavacTestUtils.compileAndRun(QUALIFIED_CLASS_NAME, ENTRY_POINT, code));

        assertThat(thrown)
                .isInstanceOf(CompilationFailedException.class)
                .hasMessageContaining(
                        ContractError.CONTRACT_STATEMENT_OUTSIDE_OF_CONTRACTS.getMessage());
    }

    @DisplayName("Compilation error when Contract.exists(arr, pred) is used outside of contracts")
    @Test
    void shouldProduceErrorWhenExistsArrayIsUsedOutsideOfContracts() {

        String code = "package pl.coco.compiler;\n"
                + "\n"
                + "import pl.coco.api.Contract;\n"
                + "\n"
                + "public class Test {\n"
                + "\n"
                + "    public static void entry() {\n"
                + "        Contract.exists(new Object[] {}, x -> true);\n"
                + "    }\n"
                + "\n"
                + "}\n";

        Throwable thrown = catchThrowable(
                () -> JavacTestUtils.compileAndRun(QUALIFIED_CLASS_NAME, ENTRY_POINT, code));

        assertThat(thrown)
                .isInstanceOf(CompilationFailedException.class)
                .hasMessageContaining(
                        ContractError.CONTRACT_STATEMENT_OUTSIDE_OF_CONTRACTS.getMessage());
    }

    @DisplayName("Compilation error when Contract.exists(iter, pred) is used outside of contracts")
    @Test
    void shouldProduceErrorWhenExistsIterIsUsedOutsideOfContracts() {

        String code = "package pl.coco.compiler;\n"
                + "\n"
                + "import java.util.Collections;\n"
                + "import pl.coco.api.Contract;\n"
                + "\n"
                + "public class Test {\n"
                + "\n"
                + "    public static void entry() {\n"
                + "        Contract.exists(Collections.emptyList(), x -> true);\n"
                + "    }\n"
                + "\n"
                + "}\n";

        Throwable thrown = catchThrowable(
                () -> JavacTestUtils.compileAndRun(QUALIFIED_CLASS_NAME, ENTRY_POINT, code));

        assertThat(thrown)
                .isInstanceOf(CompilationFailedException.class)
                .hasMessageContaining(
                        ContractError.CONTRACT_STATEMENT_OUTSIDE_OF_CONTRACTS.getMessage());
    }
}
