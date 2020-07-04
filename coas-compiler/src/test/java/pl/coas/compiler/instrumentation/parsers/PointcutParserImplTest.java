package pl.coas.compiler.instrumentation.parsers;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Collections;

import org.junit.jupiter.api.Test;

import com.google.common.collect.Lists;

import pl.coas.compiler.instrumentation.model.pointcut.AndPointcut;
import pl.coas.compiler.instrumentation.model.pointcut.AnnotatedArgsPointcut;
import pl.coas.compiler.instrumentation.model.pointcut.AnnotatedMethodPointcut;
import pl.coas.compiler.instrumentation.model.pointcut.AnnotatedTypePointcut;
import pl.coas.compiler.instrumentation.model.pointcut.ArgsPointcut;
import pl.coas.compiler.instrumentation.model.pointcut.MethodArguments;
import pl.coas.compiler.instrumentation.model.pointcut.MethodPointcut;
import pl.coas.compiler.instrumentation.model.pointcut.OrPointcut;
import pl.coas.compiler.instrumentation.model.pointcut.Pointcut;
import pl.coas.compiler.instrumentation.model.pointcut.RegularMethodArguments;
import pl.coas.compiler.instrumentation.model.pointcut.TargetPointcut;
import pl.coas.compiler.instrumentation.model.pointcut.WildcardString;

class PointcutParserImplTest {

    private PointcutParserImpl parser = new PointcutParserImpl();

    @Test
    void shouldParseSimpleTarget() {
        Pointcut actual = parser.parse("target(com.example.Example)");

        Pointcut expected = new TargetPointcut("com.example.Example");

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void shouldParseMethod() {
        Pointcut actual =
                parser.parse("method(void MyClass.methodName(int, Integer*) throwing Exc*)");

        MethodArguments args = new RegularMethodArguments(
                Lists.newArrayList(new WildcardString("int"),
                        new WildcardString("Integer*")));
        Pointcut expected = new MethodPointcut.Builder()
                .withKind("")
                .withModifiers(Collections.emptyList())
                .withReturnType("void")
                .withClassName("MyClass")
                .withMethodName("methodName")
                .withArgumentTypes(args)
                .withExceptionsThrown(Lists.newArrayList("Exc*"))
                .build();

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void shouldParseArgs() {
        Pointcut actual = parser.parse("args(int, String)");

        ArgsPointcut expected = new ArgsPointcut(new RegularMethodArguments(Lists.newArrayList(
                new WildcardString("int"), new WildcardString("String"))));

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void shouldParseAnnotation() {
        Pointcut actual = parser.parse("annotation(MyPackage.MyAnnotation)");

        Pointcut expected = new AnnotatedMethodPointcut("MyPackage.MyAnnotation");

        assertThat(actual).isEqualTo(expected);

    }

    @Test
    void shouldParseAnnotatedType() {
        Pointcut actual = parser.parse("annotatedType(MyPackage.MyClass)");

        AnnotatedTypePointcut expected = new AnnotatedTypePointcut("MyPackage.MyClass");

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void shouldParseAnnotatedArgs() {
        Pointcut actual = parser.parse("annotatedArgs(MyPackage.Annotation1, My*.Annotation2)");

        AnnotatedArgsPointcut expected = new AnnotatedArgsPointcut(
                Lists.newArrayList("MyPackage.Annotation1", "My*.Annotation2"));

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void shouldParseSimpleLogicExpr() {
        Pointcut actual = parser.parse("target(MyClass1) || target(MyClass2) && target(MyClass3)");

        Pointcut expected = new OrPointcut(new TargetPointcut("MyClass1"),
                new AndPointcut(new TargetPointcut("MyClass2"), new TargetPointcut("MyClass3")));

        assertThat(actual).isEqualTo(expected);
    }
}
