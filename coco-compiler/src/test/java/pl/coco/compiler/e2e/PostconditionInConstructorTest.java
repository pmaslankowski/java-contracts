package pl.coco.compiler.e2e;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import pl.coco.api.ContractFailedException;
import pl.coco.util.CompiledClasses;
import pl.coco.util.JavacTestUtils;
import pl.coco.util.TestCompiler;
import pl.coco.util.TestCompilerInput;
import pl.coco.util.TestRunner;

class PostconditionInConstructorTest {

    private static final String ENTRY_CLASS_NAME = "pl.coco.compiler.Entry";
    private static final String ENTRY_METHOD_NAME = "entry";
    private static final String BASE_CLASS_NAME = "pl.coco.compiler.Base";
    private static final String SUBCLASS_CLASS_NAME = "pl.coco.compiler.Subclass";

    private static final int RESULT = 42;

    private TestCompiler compiler = new TestCompiler();
    private TestRunner runner = new TestRunner();

    @DisplayName("Postcondition in constructor passes")
    @Test
    void shouldReturnWhenContractInConstructorPasses() throws Throwable {
        String code = "package pl.coco.compiler;\n"
                + "\n"
                + "import pl.coco.api.code.Contract;\n"
                + "\n"
                + "public class Entry {\n"
                + "\n"
                + "    private int arg = -1;\n"
                + "\n"
                + "    public Entry(int arg) {\n"
                + "        Contract.ensures(this.arg >= 0);\n" +
                "          this.arg = arg;\n" +
                "      }\n"
                + "\n"
                + "    public static int entry() {\n"
                + "        Entry instance = new Entry(42);\n"
                + "        return instance.arg;\n"
                + "    }\n"
                + "\n"
                + "}\n";

        Object actual = JavacTestUtils.compileAndRun(ENTRY_CLASS_NAME, ENTRY_METHOD_NAME, code);

        assertThat(actual).isEqualTo(RESULT);
    }

    @DisplayName("Postcondition in constructor call fails")
    @Test
    void shouldThrowExceptionWhenContractInConstructorFails() {
        String code = "package pl.coco.compiler;\n"
                + "\n"
                + "import pl.coco.api.code.Contract;\n"
                + "\n"
                + "public class Entry {\n"
                + "\n"
                + "    private int arg = -1;\n"
                + "\n"
                + "    public Entry(int arg) {\n"
                + "        Contract.ensures(this.arg >= 0);\n" +
                "          this.arg = arg;\n" +
                "      }\n"
                + "\n"
                + "    public static int entry() {\n"
                + "        Entry instance = new Entry(-1);\n"
                + "        return instance.arg;\n"
                + "    }\n"
                + "\n"
                + "}\n";

        Throwable thrown = catchThrowable(
                () -> JavacTestUtils.compileAndRun(ENTRY_CLASS_NAME, ENTRY_METHOD_NAME, code));

        assertThat(thrown)
                .isInstanceOf(ContractFailedException.class);
    }

    @DisplayName("Postcondition in constructor call with non-trivial base constructor passes")
    @Test
    void shouldReturnWhenContractInConstructorWithNonTrivialSuperConstructorPasses()
            throws Throwable {

        String entry = "package pl.coco.compiler;\n"
                + "\n"
                + "public class Entry {\n"
                + "\n"
                + "    public static int entry() {\n"
                + "        Base instance = new Subclass(42);\n"
                + "        return instance.value;\n"
                + "    }\n"
                + "\n"
                + "}\n";

        String base = "package pl.coco.compiler;\n"
                + "\n"
                + "import pl.coco.api.code.Contract;\n"
                + "\n"
                + "public class Base {\n"
                + "\n"
                + "    public int value = -1;\n"
                + "\n"
                + "    public Base(int arg) {\n"
                + "        value = arg;\n"
                + "    }\n"
                + "\n"
                + "}\n";

        String subclass = "package pl.coco.compiler;\n"
                + "\n"
                + "import pl.coco.api.code.Contract;\n"
                + "\n"
                + "public class Subclass extends Base {\n"
                + "\n"
                + "    public Subclass(int arg) {\n"
                + "        super(arg);\n"
                + "        Contract.ensures(arg >= 0);\n"
                + "    }\n"
                + "\n"
                + "}\n";

        CompiledClasses compiled = compile(entry, base, subclass);

        Object actual = run(compiled);

        assertThat(actual).isEqualTo(RESULT);
    }

    @DisplayName("Postcondition in constructor call with non-trivial base constructor fails")
    @Test
    void shouldReturnWhenContractInConstructorWithNonTrivialSuperConstructorFails() {

        String entry = "package pl.coco.compiler;\n"
                + "\n"
                + "public class Entry {\n"
                + "\n"
                + "    public static int entry() {\n"
                + "        Base instance = new Subclass(-1);\n"
                + "        return instance.value;\n"
                + "    }\n"
                + "\n"
                + "}\n";

        String base = "package pl.coco.compiler;\n"
                + "\n"
                + "import pl.coco.api.code.Contract;\n"
                + "\n"
                + "public class Base {\n"
                + "\n"
                + "    public int value = -1;\n"
                + "\n"
                + "    public Base(int arg) {\n"
                + "        value = arg;\n"
                + "    }\n"
                + "\n"
                + "}\n";

        String subclass = "package pl.coco.compiler;\n"
                + "\n"
                + "import pl.coco.api.code.Contract;\n"
                + "\n"
                + "public class Subclass extends Base {\n"
                + "\n"
                + "    public Subclass(int arg) {\n"
                + "        super(arg);\n"
                + "        Contract.ensures(this.value >= 0);\n"
                + "    }\n"
                + "\n"
                + "}\n";

        CompiledClasses compiled = compile(entry, base, subclass);

        Throwable thrown = catchThrowable(() -> run(compiled));

        assertThat(thrown)
                .isInstanceOf(ContractFailedException.class);
    }

    private CompiledClasses compile(String entry, String base, String subclass) {
        TestCompilerInput compilerInput = new TestCompilerInput.Builder()
                .addCompilationUnit(ENTRY_CLASS_NAME, entry)
                .addCompilationUnit(BASE_CLASS_NAME, base)
                .addCompilationUnit(SUBCLASS_CLASS_NAME, subclass)
                .withPluginArg(JavacTestUtils.CONTRACTS_ENABLED)
                .build();
        return compiler.compile(compilerInput);
    }

    private Object run(CompiledClasses compiled) throws Throwable {
        return runner.run(compiled, ENTRY_CLASS_NAME, ENTRY_METHOD_NAME, new Class<?>[] {});
    }
}
