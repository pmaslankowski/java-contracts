package pl.coas.compiler.e2e;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import pl.coas.compiler.validation.AspectError;
import pl.coas.util.CoasTestUtils;
import pl.test.compiler.commons.CompilationFailedException;

class ValidationTests {

    public static final String CLASS_NAME = "pl.coas.compiler.MyAspect";
    public static final String ENTRY = "entry";

    @DisplayName("Compilation error when aspect is not the first statement")
    @Test
    void shouldProduceErrorWhenAspectIsNotTheFirstStatementInAMethod() {
        String code = "package pl.coas.compiler;\n"
                + "\n"
                + "import pl.coas.api.Aspect;\n"
                + "import pl.coas.api.Pointcut;\n"
                + "import pl.coas.api.AspectClass;\n"
                + "import pl.coas.api.AspectType;\n"
                + "import pl.coas.api.JoinPoint;\n"
                + "\n"
                + "@AspectClass\n"
                + "public class MyAspect {\n"
                + "\n"
                + "    private static int result = 0;\n"
                + "\n"
                + "    public static int entry() {\n"
                + "        MyAspect instance = new MyAspect();\n"
                + "        instance.testedMethod(0);\n"
                + "        return result;\n"
                + "    }\n"
                + "\n"
                + "    public void testedMethod(int arg) {\n"
                + "        result = arg;\n"
                + "    }\n"
                + "\n"
                + "    public Object advice0(JoinPoint jp) {\n"
                + "        jp.getArguments()[0] = (int) jp.getArguments()[0] + 1;\n"
                + "        Aspect.on(Pointcut.method(\"* *.tested*(..)\"));\n"
                + "        Aspect.type(AspectType.TRANSIENT);\n"
                + "        Aspect.order(0);\n"
                + "        return jp.proceed();\n"
                + "    }\n"
                + "\n"
                + "}\n";

        assertThatThrownBy(() -> CoasTestUtils.compileAndRun(CLASS_NAME, ENTRY, code))
                .isInstanceOf(CompilationFailedException.class)
                .hasMessageContaining(
                        AspectError.ASPECT_CAN_OCCUR_IN_BLOCK_AT_THE_BEGINNING_OF_THE_METHOD
                                .getMessage());
    }

    @DisplayName("Compilation error when aspect is interspersed with another statements")
    @Test
    void shouldProduceErrorWhenAspectBlockIsInterspersedWithOtherInstruction() {
        String code = "package pl.coas.compiler;\n"
                + "\n"
                + "import pl.coas.api.Aspect;\n"
                + "import pl.coas.api.Pointcut;\n"
                + "import pl.coas.api.AspectClass;\n"
                + "import pl.coas.api.AspectType;\n"
                + "import pl.coas.api.JoinPoint;\n"
                + "\n"
                + "@AspectClass\n"
                + "public class MyAspect {\n"
                + "\n"
                + "    private static int result = 0;\n"
                + "\n"
                + "    public static int entry() {\n"
                + "        MyAspect instance = new MyAspect();\n"
                + "        instance.testedMethod(0);\n"
                + "        return result;\n"
                + "    }\n"
                + "\n"
                + "    public void testedMethod(int arg) {\n"
                + "        result = arg;\n"
                + "    }\n"
                + "\n"
                + "    public Object advice0(JoinPoint jp) {\n"
                + "        Aspect.on(Pointcut.method(\"* *.tested*(..)\"));\n"
                + "        jp.getArguments()[0] = (int) jp.getArguments()[0] + 1;\n"
                + "        Aspect.type(AspectType.TRANSIENT);\n"
                + "        Aspect.order(0);\n"
                + "        return jp.proceed();\n"
                + "    }\n"
                + "\n"
                + "}\n";

        assertThatThrownBy(() -> CoasTestUtils.compileAndRun(CLASS_NAME, ENTRY, code))
                .isInstanceOf(CompilationFailedException.class)
                .hasMessageContaining(
                        AspectError.ASPECT_BLOCK_CAN_CONTAIN_ASPECTS_ONLY
                                .getMessage());
    }

    @DisplayName("Compilation error when advice does not return Object")
    @Test
    void shouldProduceErrorWhenAdviceMethodDoesNotReturnObject() {
        String code = "package pl.coas.compiler;\n"
                + "\n"
                + "import pl.coas.api.Aspect;\n"
                + "import pl.coas.api.Pointcut;\n"
                + "import pl.coas.api.AspectClass;\n"
                + "import pl.coas.api.AspectType;\n"
                + "import pl.coas.api.JoinPoint;\n"
                + "\n"
                + "@AspectClass\n"
                + "public class MyAspect {\n"
                + "\n"
                + "    private static int result = 0;\n"
                + "\n"
                + "    public static int entry() {\n"
                + "        MyAspect instance = new MyAspect();\n"
                + "        instance.testedMethod(0);\n"
                + "        return result;\n"
                + "    }\n"
                + "\n"
                + "    public void testedMethod(int arg) {\n"
                + "        result = arg;\n"
                + "    }\n"
                + "\n"
                + "    public void advice0(JoinPoint jp) {\n"
                + "        Aspect.on(Pointcut.method(\"* *.tested*(..)\"));\n"
                + "        Aspect.type(AspectType.TRANSIENT);\n"
                + "        Aspect.order(0);\n"
                + "    }\n"
                + "\n"
                + "}\n";

        assertThatThrownBy(() -> CoasTestUtils.compileAndRun(CLASS_NAME, ENTRY, code))
                .isInstanceOf(CompilationFailedException.class)
                .hasMessageContaining(
                        AspectError.INVALID_ADVICE_SINGATURE.getMessage());
    }


    @DisplayName("Compilation error when advice does takes zero arguments")
    @Test
    void shouldProduceErrorWhenAdviceMethodTakesZeroArguments() {
        String code = "package pl.coas.compiler;\n"
                + "\n"
                + "import pl.coas.api.Aspect;\n"
                + "import pl.coas.api.Pointcut;\n"
                + "import pl.coas.api.AspectClass;\n"
                + "import pl.coas.api.AspectType;\n"
                + "import pl.coas.api.JoinPoint;\n"
                + "\n"
                + "@AspectClass\n"
                + "public class MyAspect {\n"
                + "\n"
                + "    private static int result = 0;\n"
                + "\n"
                + "    public static int entry() {\n"
                + "        MyAspect instance = new MyAspect();\n"
                + "        instance.testedMethod(0);\n"
                + "        return result;\n"
                + "    }\n"
                + "\n"
                + "    public void testedMethod(int arg) {\n"
                + "        result = arg;\n"
                + "    }\n"
                + "\n"
                + "    public Object advice0() {\n"
                + "        Aspect.on(Pointcut.method(\"* *.tested*(..)\"));\n"
                + "        Aspect.type(AspectType.TRANSIENT);\n"
                + "        Aspect.order(0);\n"
                + "    }\n"
                + "\n"
                + "}\n";

        assertThatThrownBy(() -> CoasTestUtils.compileAndRun(CLASS_NAME, ENTRY, code))
                .isInstanceOf(CompilationFailedException.class)
                .hasMessageContaining(
                        AspectError.INVALID_ADVICE_SINGATURE.getMessage());
    }

    @DisplayName("Compilation error when advice does takes one argument other than JoinPoint")
    @Test
    void shouldProduceErrorWhenAdviceMethodTakesOneArgumentOtherThanJoinPoint() {
        String code = "package pl.coas.compiler;\n"
                + "\n"
                + "import pl.coas.api.Aspect;\n"
                + "import pl.coas.api.Pointcut;\n"
                + "import pl.coas.api.AspectClass;\n"
                + "import pl.coas.api.AspectType;\n"
                + "import pl.coas.api.JoinPoint;\n"
                + "\n"
                + "@AspectClass\n"
                + "public class MyAspect {\n"
                + "\n"
                + "    private static int result = 0;\n"
                + "\n"
                + "    public static int entry() {\n"
                + "        MyAspect instance = new MyAspect();\n"
                + "        instance.testedMethod(0);\n"
                + "        return result;\n"
                + "    }\n"
                + "\n"
                + "    public void testedMethod(int arg) {\n"
                + "        result = arg;\n"
                + "    }\n"
                + "\n"
                + "    public Object advice0(int arg) {\n"
                + "        Aspect.on(Pointcut.method(\"* *.tested*(..)\"));\n"
                + "        Aspect.type(AspectType.TRANSIENT);\n"
                + "        Aspect.order(0);\n"
                + "    }\n"
                + "\n"
                + "}\n";

        assertThatThrownBy(() -> CoasTestUtils.compileAndRun(CLASS_NAME, ENTRY, code))
                .isInstanceOf(CompilationFailedException.class)
                .hasMessageContaining(
                        AspectError.INVALID_ADVICE_SINGATURE.getMessage());
    }

    @DisplayName("Compilation error when advice does takes more than one argument")
    @Test
    void shouldProduceErrorWhenAdviceMethodTakesMoreThanOneArgument() {
        String code = "package pl.coas.compiler;\n"
                + "\n"
                + "import pl.coas.api.Aspect;\n"
                + "import pl.coas.api.Pointcut;\n"
                + "import pl.coas.api.AspectClass;\n"
                + "import pl.coas.api.AspectType;\n"
                + "import pl.coas.api.JoinPoint;\n"
                + "\n"
                + "@AspectClass\n"
                + "public class MyAspect {\n"
                + "\n"
                + "    private static int result = 0;\n"
                + "\n"
                + "    public static int entry() {\n"
                + "        MyAspect instance = new MyAspect();\n"
                + "        instance.testedMethod(0);\n"
                + "        return result;\n"
                + "    }\n"
                + "\n"
                + "    public void testedMethod(int arg) {\n"
                + "        result = arg;\n"
                + "    }\n"
                + "\n"
                + "    public Object advice0(JoinPoint jp, int arg) {\n"
                + "        Aspect.on(Pointcut.method(\"* *.tested*(..)\"));\n"
                + "        Aspect.type(AspectType.TRANSIENT);\n"
                + "        Aspect.order(0);\n"
                + "    }\n"
                + "\n"
                + "}\n";

        assertThatThrownBy(() -> CoasTestUtils.compileAndRun(CLASS_NAME, ENTRY, code))
                .isInstanceOf(CompilationFailedException.class)
                .hasMessageContaining(
                        AspectError.INVALID_ADVICE_SINGATURE.getMessage());
    }
}
