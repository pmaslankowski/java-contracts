package pl.coco.compiler.e2e.code;

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

class PostconditionInheritanceTest {

    private static final String ENTRY_CLASS_NAME = "pl.coco.compiler.Entry";
    private static final String ENTRY_METHOD_NAME = "entry";
    private static final String BASE_CLASS_NAME = "pl.coco.compiler.Base";
    private static final String SUBCLASS_CLASS_NAME = "pl.coco.compiler.Subclass";

    private TestCompiler compiler = new TestCompiler();
    private TestRunner runner = new TestRunner();

    @DisplayName("Postcondition in base class only passes")
    @Test
    void methodShouldReturnResultWhenSingleBasePostconditionPasses() throws Throwable {

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
                + "import pl.coco.api.code.Contract;\n"
                + "\n"
                + "public class Base {\n"
                + "\n"
                + "    public int field = 1;\n"
                + "\n"
                + "    public int testedMethod(int arg) {\n"
                + "        Contract.ensures(field > 0);\n"
                + "        return 42;\n"
                + "    }\n"
                + "\n"
                + "}\n";

        String subclass = "package pl.coco.compiler;\n"
                + "\n"
                + "import pl.coco.api.code.Contract;\n"
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

    @DisplayName("Postcondition in base class only fails")
    @Test
    void methodShouldThrowExceptionWhenSingleBasePostconditionFails() {

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
                + "import pl.coco.api.code.Contract;\n"
                + "\n"
                + "public class Base {\n"
                + "\n"
                + "    public int testedMethod(int arg) {\n"
                + "        Contract.ensures(Contract.result(Integer.class) == 43);\n"
                + "        return 42;\n"
                + "    }\n"
                + "\n"
                + "}\n";

        String subclass = "package pl.coco.compiler;\n"
                + "\n"
                + "import pl.coco.api.code.Contract;\n"
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
                .isInstanceOf(ContractFailedException.class);
    }

    @DisplayName("2 postconditions, both pass")
    @Test
    void methodShouldReturnResultWhenBothPostconditionPass() throws Throwable {

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
                + "import pl.coco.api.code.Contract;\n"
                + "\n"
                + "public class Base {\n"
                + "\n"
                + "    public int testedMethod(int arg) {\n"
                + "        Contract.ensures(arg > 0);\n"
                + "        return 42;\n"
                + "    }\n"
                + "\n"
                + "}\n";

        String subclass = "package pl.coco.compiler;\n"
                + "\n"
                + "import pl.coco.api.code.Contract;\n"
                + "\n"
                + "public class Subclass extends Base {\n"
                + "\n"
                + "    @Override\n"
                + "    public int testedMethod(int arg) {\n"
                + "        Contract.ensures(Contract.result(Integer.class) == 42);\n"
                + "        return 42;\n"
                + "    }\n"
                + "\n"
                + "}\n";

        CompiledClasses compiled = compile(entry, base, subclass);

        Object actual = run(compiled);

        assertThat(actual).isEqualTo(42);
    }

    @DisplayName("2 postconditions, subclass postcondition fails")
    @Test
    void methodShouldThrowExceptionWhenSubclassPostconditionFails() {

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
                + "import pl.coco.api.code.Contract;\n"
                + "\n"
                + "public class Base {\n"
                + "\n"
                + "    public int testedMethod(int arg) {\n"
                + "        Contract.ensures(Contract.result(Integer.class) == 42);\n"
                + "        return 42;\n"
                + "    }\n"
                + "\n"
                + "}\n";

        String subclass = "package pl.coco.compiler;\n"
                + "\n"
                + "import pl.coco.api.code.Contract;\n"
                + "\n"
                + "public class Subclass extends Base {\n"
                + "\n"
                + "    @Override\n"
                + "    public int testedMethod(int arg) {\n"
                + "        Contract.ensures(arg == 0);\n"
                + "        return 42;\n"
                + "    }\n"
                + "\n"
                + "}\n";

        CompiledClasses compiled = compile(entry, base, subclass);

        Throwable thrown = catchThrowable(() -> run(compiled));

        assertThat(thrown)
                .isInstanceOf(ContractFailedException.class);
    }

    @DisplayName("2 postconditions, both fail")
    @Test
    void methodShouldThrowExceptionWhenBothPostconditionsFail() {

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
                + "import pl.coco.api.code.Contract;\n"
                + "\n"
                + "public class Base {\n"
                + "\n"
                + "    public static int field = 0;\n"
                + "\n"
                + "    public int testedMethod(int arg) {\n"
                + "        Contract.ensures(Contract.result(Integer.class) == 42);\n"
                + "        return 42;\n"
                + "    }\n"
                + "\n"
                + "}\n";

        String subclass = "package pl.coco.compiler;\n"
                + "\n"
                + "import pl.coco.api.code.Contract;\n"
                + "\n"
                + "public class Subclass extends Base {\n"
                + "\n"
                + "    @Override\n"
                + "    public int testedMethod(int arg) {\n"
                + "        Contract.ensures(field > 0);\n"
                + "        return 42;\n"
                + "    }\n"
                + "\n"
                + "}\n";

        CompiledClasses compiled = compile(entry, base, subclass);

        Throwable thrown = catchThrowable(() -> run(compiled));

        assertThat(thrown)
                .isInstanceOf(ContractFailedException.class);
    }

    @DisplayName("Postcondition on subclass method with different arg names fails")
    @Test
    void methodShouldThrowWhenSubclassMethodHasDifferentArgumentNamesAndPostconditionFails() {

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
                + "import pl.coco.api.code.Contract;\n"
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
                + "import pl.coco.api.code.Contract;\n"
                + "\n"
                + "public class Subclass extends Base {\n"
                + "\n"
                + "    @Override\n"
                + "    public int testedMethod(int arg2) {\n"
                + "        Contract.ensures(arg2 == 0);\n"
                + "        return 42;\n"
                + "    }\n"
                + "\n"
                + "}\n";

        CompiledClasses compiled = compile(entry, base, subclass);

        Throwable thrown = catchThrowable(() -> run(compiled));

        assertThat(thrown)
                .isInstanceOf(ContractFailedException.class);
    }

    @DisplayName("Base postcondition using private field fails")
    @Test
    void methodShouldThrowWhenBasePostconditionUsesPrivateField() {

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
                + "import pl.coco.api.code.Contract;\n"
                + "\n"
                + "public class Base {\n"
                + "\n"
                + "    private int field = 0;\n"
                + "\n"
                + "    public int testedMethod(int arg) {\n"
                + "        Contract.ensures(this.field > 0);\n"
                + "        return 42;\n"
                + "    }\n"
                + "\n"
                + "}\n";

        String subclass = "package pl.coco.compiler;\n"
                + "\n"
                + "import pl.coco.api.code.Contract;\n"
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
                .isInstanceOf(ContractFailedException.class);
    }

    @DisplayName("Subclass postcondition using base protected field fails")
    @Test
    void methodShouldThrowWhenSubclassPostconditionUsesProtectedField() {

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
                + "import pl.coco.api.code.Contract;\n"
                + "\n"
                + "public class Base {\n"
                + "\n"
                + "    protected int field = 0;\n"
                + "\n"
                + "    public int testedMethod(int arg) {\n"
                + "        return 42;\n"
                + "    }\n"
                + "\n"
                + "}\n";

        String subclass = "package pl.coco.compiler;\n"
                + "\n"
                + "import pl.coco.api.code.Contract;\n"
                + "\n"
                + "public class Subclass extends Base {\n"
                + "\n"
                + "    @Override\n"
                + "    public int testedMethod(int arg) {\n"
                + "        Contract.ensures(field > 0);\n"
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
