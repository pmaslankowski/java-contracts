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

class PreconditionInheritanceTest {

    private static final String ENTRY_CLASS_NAME = "pl.coco.compiler.Entry";
    private static final String ENTRY_METHOD_NAME = "entry";
    private static final String BASE_CLASS_NAME = "pl.coco.compiler.Base";
    private static final String SUBCLASS_CLASS_NAME = "pl.coco.compiler.Subclass";

    private TestCompiler compiler = new TestCompiler();
    private TestRunner runner = new TestRunner();

    @DisplayName("Precondition on base method only passes")
    @Test
    void methodShouldReturnResultWhenBasePreconditionIsSatisfiedAndItIsTheOnlyPrecondition()
            throws Throwable {

        String entry = "package pl.coco.compiler;\n"
                + "\n"
                + "public class Entry {\n"
                + "\n"
                + "    public static int entry() {\n"
                + "        Base instance = new Subclass();\n"
                + "        return instance.testedMethod(1);\n"
                + "    }\n"
                + "\n"
                + "}\n";

        String base = "package pl.coco.compiler;\n"
                + "\n"
                + "import pl.coco.api.Contract;\n"
                + "\n"
                + "public class Base {\n"
                + "\n"
                + "    public int testedMethod(int arg) {\n"
                + "        Contract.requires(arg > 0);\n"
                + "        return 42;\n"
                + "    }\n"
                + "\n"
                + "}\n";

        String subclass = "package pl.coco.compiler;\n"
                + "\n"
                + "import pl.coco.api.Contract;\n"
                + "\n"
                + "public class Subclass extends Base {\n"
                + "\n"
                + "    @Override\n"
                + "    public int testedMethod(int arg) {\n"
                + "        return 42;\n"
                + "    }\n"
                + "\n"
                + "}\n";

        CompiledClasses compiled = compile(entry, base, subclass);

        Object actual = run(compiled);

        assertThat(actual).isEqualTo(42);
    }

    @DisplayName("Precondition on base method only fails")
    @Test
    void methodShouldThrowExceptionWhenBasePreconditionIsNotSatisfiedAndItIsTheOnlyPrecondition() {

        String entry = "package pl.coco.compiler;\n"
                + "\n"
                + "public class Entry {\n"
                + "\n"
                + "    public static int entry() {\n"
                + "        Base instance = new Subclass();\n"
                + "        return instance.testedMethod(0);\n"
                + "    }\n"
                + "\n"
                + "}\n";

        String base = "package pl.coco.compiler;\n"
                + "\n"
                + "import pl.coco.api.Contract;\n"
                + "\n"
                + "public class Base {\n"
                + "\n"
                + "    public int testedMethod(int arg) {\n"
                + "        Contract.requires(arg > 0);\n"
                + "        return 42;\n"
                + "    }\n"
                + "\n"
                + "}\n";

        String subclass = "package pl.coco.compiler;\n"
                + "\n"
                + "import pl.coco.api.Contract;\n"
                + "\n"
                + "public class Subclass extends Base {\n"
                + "\n"
                + "    @Override\n"
                + "    public int testedMethod(int arg) {\n"
                + "        return 42;\n"
                + "    }\n"
                + "\n"
                + "}\n";

        CompiledClasses compiled = compile(entry, base, subclass);

        Throwable thrown = catchThrowable(() -> run(compiled));
        assertThat(thrown)
                .isInstanceOf(ContractFailedException.class)
                .hasMessage("Precondition \"arg > 0\" is not satisfied.");
    }

    @DisplayName("Precondition on subclass method only passes")
    @Test
    void methodShouldReturnResultWhenSubclassPreconditionIsSatisfiedAndItIsTheOnlyPrecondition()
            throws Throwable {

        String entry = "package pl.coco.compiler;\n"
                + "\n"
                + "public class Entry {\n"
                + "\n"
                + "    public static int entry() {\n"
                + "        Base instance = new Subclass();\n"
                + "        return instance.testedMethod(0);\n"
                + "    }\n"
                + "\n"
                + "}\n";

        String base = "package pl.coco.compiler;\n"
                + "\n"
                + "import pl.coco.api.Contract;\n"
                + "\n"
                + "public class Base {\n"
                + "\n"
                + "    public int testedMethod(int arg) {\n"
                + "        return 42;\n"
                + "    }\n"
                + "\n"
                + "}\n";

        String subclass = "package pl.coco.compiler;\n"
                + "\n"
                + "import pl.coco.api.Contract;\n"
                + "\n"
                + "public class Subclass extends Base {\n"
                + "\n"
                + "    @Override\n"
                + "    public int testedMethod(int arg) {\n"
                + "        Contract.requires(arg >= 0);\n"
                + "        return 42;\n"
                + "    }\n"
                + "\n"
                + "}\n";

        CompiledClasses compiled = compile(entry, base, subclass);

        Object actual = run(compiled);

        assertThat(actual).isEqualTo(42);
    }

    @DisplayName("Precondition on subclass method only fails")
    @Test
    void methodShouldThrowExceptionWhenSubclassPreconditionFailsAndItsTheOnlyPrecondition() {

        String entry = "package pl.coco.compiler;\n"
                + "\n"
                + "public class Entry {\n"
                + "\n"
                + "    public static int entry() {\n"
                + "        Base instance = new Subclass();\n"
                + "        return instance.testedMethod(-1);\n"
                + "    }\n"
                + "\n"
                + "}\n";

        String base = "package pl.coco.compiler;\n"
                + "\n"
                + "import pl.coco.api.Contract;\n"
                + "\n"
                + "public class Base {\n"
                + "\n"
                + "    public int testedMethod(int arg) {\n"
                + "        return 42;\n"
                + "    }\n"
                + "\n"
                + "}\n";

        String subclass = "package pl.coco.compiler;\n"
                + "\n"
                + "import pl.coco.api.Contract;\n"
                + "\n"
                + "public class Subclass extends Base {\n"
                + "\n"
                + "    @Override\n"
                + "    public int testedMethod(int arg) {\n"
                + "        Contract.requires(arg >= 0);\n"
                + "        return 42;\n"
                + "    }\n"
                + "\n"
                + "}\n";

        CompiledClasses compiled = compile(entry, base, subclass);

        Throwable thrown = catchThrowable(() -> run(compiled));

        assertThat(thrown)
                .isInstanceOf(ContractFailedException.class)
                .hasMessage("Precondition \"arg >= 0\" is not satisfied.");
    }

    @DisplayName("2 preconditions, subclass precondition passes")
    @Test
    void methodShouldReturnResultWhenSubclassPreconditionIsSatisfied() throws Throwable {

        String entry = "package pl.coco.compiler;\n"
                + "\n"
                + "public class Entry {\n"
                + "\n"
                + "    public static int entry() {\n"
                + "        Base instance = new Subclass();\n"
                + "        return instance.testedMethod(1);\n"
                + "    }\n"
                + "\n"
                + "}\n";

        String base = "package pl.coco.compiler;\n"
                + "\n"
                + "import pl.coco.api.Contract;\n"
                + "\n"
                + "public class Base {\n"
                + "\n"
                + "    public int testedMethod(int arg) {\n"
                + "        Contract.requires(arg > 0);\n"
                + "        return 42;\n"
                + "    }\n"
                + "\n"
                + "}\n";

        String subclass = "package pl.coco.compiler;\n"
                + "\n"
                + "import pl.coco.api.Contract;\n"
                + "\n"
                + "public class Subclass extends Base {\n"
                + "\n"
                + "    @Override\n"
                + "    public int testedMethod(int arg) {\n"
                + "        Contract.requires(arg >= 0);\n"
                + "        return 42;\n"
                + "    }\n"
                + "\n"
                + "}\n";

        CompiledClasses compiled = compile(entry, base, subclass);

        Object actual = run(compiled);

        assertThat(actual).isEqualTo(42);
    }

    @DisplayName("2 preconditions, subclass precondition fails")
    @Test
    void methodShouldThrowExceptionWhenSubclassPreconditionFails() {

        String entry = "package pl.coco.compiler;\n"
                + "\n"
                + "public class Entry {\n"
                + "\n"
                + "    public static int entry() {\n"
                + "        Base instance = new Subclass();\n"
                + "        return instance.testedMethod(1);\n"
                + "    }\n"
                + "\n"
                + "}\n";

        String base = "package pl.coco.compiler;\n"
                + "\n"
                + "import pl.coco.api.Contract;\n"
                + "\n"
                + "public class Base {\n"
                + "\n"
                + "    public int testedMethod(int arg) {\n"
                + "        Contract.requires(arg > 0);\n"
                + "        return 42;\n"
                + "    }\n"
                + "\n"
                + "}\n";

        String subclass = "package pl.coco.compiler;\n"
                + "\n"
                + "import pl.coco.api.Contract;\n"
                + "\n"
                + "public class Subclass extends Base {\n"
                + "\n"
                + "    @Override\n"
                + "    public int testedMethod(int arg) {\n"
                + "        Contract.requires(arg > 5);\n"
                + "        return 42;\n"
                + "    }\n"
                + "\n"
                + "}\n";

        CompiledClasses compiled = compile(entry, base, subclass);

        Throwable thrown = catchThrowable(() -> run(compiled));

        // TODO: check message
        assertThat(thrown)
                .isInstanceOf(ContractFailedException.class);
    }

    @DisplayName("2 preconditions, base precondition fails")
    @Test
    void methodShouldReturnResultWhenBasePreconditionFailsButSubclassPreconditionDoesnt()
            throws Throwable {

        String entry = "package pl.coco.compiler;\n"
                + "\n"
                + "public class Entry {\n"
                + "\n"
                + "    public static int entry() {\n"
                + "        Base instance = new Subclass();\n"
                + "        return instance.testedMethod(0);\n"
                + "    }\n"
                + "\n"
                + "}\n";

        String base = "package pl.coco.compiler;\n"
                + "\n"
                + "import pl.coco.api.Contract;\n"
                + "\n"
                + "public class Base {\n"
                + "\n"
                + "    public int testedMethod(int arg) {\n"
                + "        Contract.requires(arg > 0);\n"
                + "        return 42;\n"
                + "    }\n"
                + "\n"
                + "}\n";

        String subclass = "package pl.coco.compiler;\n"
                + "\n"
                + "import pl.coco.api.Contract;\n"
                + "\n"
                + "public class Subclass extends Base {\n"
                + "\n"
                + "    @Override\n"
                + "    public int testedMethod(int arg) {\n"
                + "        Contract.requires(arg >= 0);\n"
                + "        return 42;\n"
                + "    }\n"
                + "\n"
                + "}\n";

        CompiledClasses compiled = compile(entry, base, subclass);

        Object actual = run(compiled);

        assertThat(actual).isEqualTo(42);
    }

    @DisplayName("Base precondition on static field passes")
    @Test
    void methodShouldReturnResultWhenBasePreconditionOnStaticFieldPasses() throws Throwable {

        String entry = "package pl.coco.compiler;\n"
                + "\n"
                + "public class Entry {\n"
                + "\n"
                + "    public static int entry() {\n"
                + "        Base instance = new Subclass();\n"
                + "        return instance.testedMethod();\n"
                + "    }\n"
                + "\n"
                + "}\n";

        String base = "package pl.coco.compiler;\n"
                + "\n"
                + "import pl.coco.api.Contract;\n"
                + "\n"
                + "public class Base {\n"
                + "\n"
                + "    protected static int field = 5;\n"
                + "\n"
                + "    public int testedMethod() {\n"
                + "        Contract.requires(field > 0);\n"
                + "        return 42;\n"
                + "    }\n"
                + "\n"
                + "}\n";

        String subclass = "package pl.coco.compiler;\n"
                + "\n"
                + "import pl.coco.api.Contract;\n"
                + "\n"
                + "public class Subclass extends Base {\n"
                + "\n"
                + "    @Override\n"
                + "    public int testedMethod() {\n"
                + "        return 42;\n"
                + "    }\n"
                + "\n"
                + "}\n";

        CompiledClasses compiled = compile(entry, base, subclass);

        Object actual = run(compiled);

        assertThat(actual).isEqualTo(42);
    }

    @DisplayName("Base precondition on static field fails")
    @Test
    void methodShouldThrowExceptionWhenBasePreconditionOnStaticFieldFails() {

        String entry = "package pl.coco.compiler;\n"
                + "\n"
                + "public class Entry {\n"
                + "\n"
                + "    public static int entry() {\n"
                + "        Base instance = new Subclass();\n"
                + "        return instance.testedMethod();\n"
                + "    }\n"
                + "\n"
                + "}\n";

        String base = "package pl.coco.compiler;\n"
                + "\n"
                + "import pl.coco.api.Contract;\n"
                + "\n"
                + "public class Base {\n"
                + "\n"
                + "    protected static int field = 0;\n"
                + "\n"
                + "    public int testedMethod() {\n"
                + "        Contract.requires(field > 0);\n"
                + "        return 42;\n"
                + "    }\n"
                + "\n"
                + "}\n";

        String subclass = "package pl.coco.compiler;\n"
                + "\n"
                + "import pl.coco.api.Contract;\n"
                + "\n"
                + "public class Subclass extends Base {\n"
                + "\n"
                + "    @Override\n"
                + "    public int testedMethod() {\n"
                + "        return 42;\n"
                + "    }\n"
                + "\n"
                + "}\n";

        CompiledClasses compiled = compile(entry, base, subclass);

        Throwable thrown = catchThrowable(() -> run(compiled));

        // TODO: check message
        assertThat(thrown)
                .isInstanceOf(ContractFailedException.class);
    }

    @DisplayName("Base precondition on instance field passes")
    @Test
    void methodShouldReturnResultWhenBasePreconditionOnInstanceFieldPasses() throws Throwable {
        String entry = "package pl.coco.compiler;\n"
                + "\n"
                + "public class Entry {\n"
                + "\n"
                + "    public static int entry() {\n"
                + "        Base instance = new Subclass();\n"
                + "        return instance.testedMethod();\n"
                + "    }\n"
                + "\n"
                + "}\n";

        String base = "package pl.coco.compiler;\n"
                + "\n"
                + "import pl.coco.api.Contract;\n"
                + "\n"
                + "public class Base {\n"
                + "\n"
                + "    protected static int field = 5;\n"
                + "\n"
                + "    public int testedMethod() {\n"
                + "        Contract.requires(field > 0);\n"
                + "        return 42;\n"
                + "    }\n"
                + "\n"
                + "}\n";

        String subclass = "package pl.coco.compiler;\n"
                + "\n"
                + "import pl.coco.api.Contract;\n"
                + "\n"
                + "public class Subclass extends Base {\n"
                + "\n"
                + "    @Override\n"
                + "    public int testedMethod() {\n"
                + "        return 42;\n"
                + "    }\n"
                + "\n"
                + "}\n";

        CompiledClasses compiled = compile(entry, base, subclass);

        Object actual = run(compiled);

        assertThat(actual).isEqualTo(42);
    }

    @DisplayName("Base precondition on instance field fails")
    @Test
    void methodShouldThrowExceptionWhenBasePreconditionOnInstanceFieldFails() {

        String entry = "package pl.coco.compiler;\n"
                + "\n"
                + "public class Entry {\n"
                + "\n"
                + "    public static int entry() {\n"
                + "        Base instance = new Subclass();\n"
                + "        return instance.testedMethod();\n"
                + "    }\n"
                + "\n"
                + "}\n";

        String base = "package pl.coco.compiler;\n"
                + "\n"
                + "import pl.coco.api.Contract;\n"
                + "\n"
                + "public class Base {\n"
                + "\n"
                + "    protected int field = 0;\n"
                + "\n"
                + "    public int testedMethod() {\n"
                + "        Contract.requires(field > 0);\n"
                + "        return 42;\n"
                + "    }\n"
                + "\n"
                + "}\n";

        String subclass = "package pl.coco.compiler;\n"
                + "\n"
                + "import pl.coco.api.Contract;\n"
                + "\n"
                + "public class Subclass extends Base {\n"
                + "\n"
                + "    @Override\n"
                + "    public int testedMethod() {\n"
                + "        return 42;\n"
                + "    }\n"
                + "\n"
                + "}\n";

        CompiledClasses compiled = compile(entry, base, subclass);

        Throwable thrown = catchThrowable(() -> run(compiled));

        // TODO: check message
        assertThat(thrown)
                .isInstanceOf(ContractFailedException.class);
    }

    @DisplayName("Base precondition on private instance field fails but subclass contract passes")
    @Test
    void methodShouldReturnResultWhenBasePreconditionOnPrivateFieldFails() throws Throwable {

        String entry = "package pl.coco.compiler;\n"
                + "\n"
                + "public class Entry {\n"
                + "\n"
                + "    public static int entry() {\n"
                + "        Base instance = new Subclass();\n"
                + "        return instance.testedMethod();\n"
                + "    }\n"
                + "\n"
                + "}\n";

        String base = "package pl.coco.compiler;\n"
                + "\n"
                + "import pl.coco.api.Contract;\n"
                + "\n"
                + "public class Base {\n"
                + "\n"
                + "    private int field = 1;\n"
                + "\n"
                + "    public int testedMethod() {\n"
                + "        Contract.requires(field > 0);\n"
                + "        return 42;\n"
                + "    }\n"
                + "\n"
                + "}\n";

        String subclass = "package pl.coco.compiler;\n"
                + "\n"
                + "import pl.coco.api.Contract;\n"
                + "\n"
                + "public class Subclass extends Base {\n"
                + "\n"
                + "    @Override\n"
                + "    public int testedMethod() {\n"
                + "        return 42;\n"
                + "    }\n"
                + "\n"
                + "}\n";

        CompiledClasses compiled = compile(entry, base, subclass);

        Object actual = run(compiled);

        assertThat(actual).isEqualTo(42);
    }

    @DisplayName("Base precondition with different argument names passes")
    @Test
    void methodShouldReturnResultWhenBasePreconditionWithDifferentArgumentNamesPasses()
            throws Throwable {

        String entry = "package pl.coco.compiler;\n"
                + "\n"
                + "public class Entry {\n"
                + "\n"
                + "    public static int entry() {\n"
                + "        Base instance = new Subclass();\n"
                + "        return instance.testedMethod(0);\n"
                + "    }\n"
                + "\n"
                + "}\n";

        String base = "package pl.coco.compiler;\n"
                + "\n"
                + "import pl.coco.api.Contract;\n"
                + "\n"
                + "public class Base {\n"
                + "\n"
                + "    public int testedMethod(int arg) {\n"
                + "        Contract.requires(arg >= 0);\n"
                + "        return 42;\n"
                + "    }\n"
                + "\n"
                + "}\n";

        String subclass = "package pl.coco.compiler;\n"
                + "\n"
                + "import pl.coco.api.Contract;\n"
                + "\n"
                + "public class Subclass extends Base {\n"
                + "\n"
                + "    @Override\n"
                + "    public int testedMethod(int different) {\n"
                + "        return 42;\n"
                + "    }\n"
                + "\n"
                + "}\n";

        CompiledClasses compiled = compile(entry, base, subclass);

        Object actual = run(compiled);

        assertThat(actual).isEqualTo(42);
    }

    @DisplayName("Base precondition with different argument names fails")
    @Test
    void methodShouldThrowExceptionWhenBasePreconditionWithDifferentArgumentNamesFails() {

        String entry = "package pl.coco.compiler;\n"
                + "\n"
                + "public class Entry {\n"
                + "\n"
                + "    public static int entry() {\n"
                + "        Base instance = new Subclass();\n"
                + "        return instance.testedMethod(0);\n"
                + "    }\n"
                + "\n"
                + "}\n";

        String base = "package pl.coco.compiler;\n"
                + "\n"
                + "import pl.coco.api.Contract;\n"
                + "\n"
                + "public class Base {\n"
                + "\n"
                + "    public int testedMethod(int arg) {\n"
                + "        Contract.requires(arg > 0);\n"
                + "        return 42;\n"
                + "    }\n"
                + "\n"
                + "}\n";

        String subclass = "package pl.coco.compiler;\n"
                + "\n"
                + "import pl.coco.api.Contract;\n"
                + "\n"
                + "public class Subclass extends Base {\n"
                + "\n"
                + "    @Override\n"
                + "    public int testedMethod(int different) {\n"
                + "        return 42;\n"
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
