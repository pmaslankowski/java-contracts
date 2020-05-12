package pl.coco.compiler.e2e;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import pl.coco.compiler.validation.ContractValidator;
import pl.coco.util.CompilationFailedException;
import pl.coco.util.JavacTestUtils;

class ContractValidationTest {

    private static final String QUALIFIED_CLASS_NAME = "pl.coco.compiler.Test";
    private static final String ENTRY_POINT = "entry";
    private static final int RESULT = 42;

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
                        ContractValidator.CONTRACT_CALL_CAN_OCCUR_IN_BLOCK_AT_THE_BEGINNING_OF_THE_METHOD_MSG);

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
                .hasMessageContaining(ContractValidator.CONTRACT_BLOCK_CAN_CONTAIN_ONLY_CONTRACTS);
    }
}
