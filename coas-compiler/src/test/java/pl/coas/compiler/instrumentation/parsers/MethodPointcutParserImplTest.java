package pl.coas.compiler.instrumentation.parsers;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import java.util.Collections;

import org.junit.jupiter.api.Test;

import pl.coas.compiler.instrumentation.model.pointcut.MethodPointcut;
import pl.coas.compiler.instrumentation.model.pointcut.RegularMethodArguments;
import pl.coas.compiler.instrumentation.model.pointcut.WildcardString;
import pl.coas.compiler.instrumentation.parsers.util.MethodArgumentsFactory;

// TODO: więcej testów
class MethodPointcutParserImplTest {

    private MethodArgumentsFactory methodArgumentsFactory = new MethodArgumentsFactory();
    private MethodPointcutParserImpl parser = new MethodPointcutParserImpl(methodArgumentsFactory);

    @Test
    void shouldParseMinimalMethodPointcut() {
        MethodPointcut actual = parser.parse("* mypackage.*.*()");

        MethodPointcut expected = new MethodPointcut.Builder()
                .withKind("")
                .withModifiers(Collections.emptyList())
                .withReturnType("*")
                .withClassName("mypackage.*")
                .withMethodName("*")
                .withArgumentTypes(new RegularMethodArguments(Collections.emptyList()))
                .withExceptionsThrown(Collections.emptyList())
                .build();

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void shouldParseFullMethodPointcut() {

        MethodPointcut actual = parser.parse(
                "non-static package-private private void " +
                        "mypackage.*class.my*(String, int, mypackage.*) " +
                        "throwing mypackage.*, IllegalArgumentException");

        MethodPointcut expected = new MethodPointcut.Builder()
                .withKind("non-static")
                .withModifiers(Arrays.asList("package-private", "private"))
                .withReturnType("void")
                .withClassName("mypackage.*class")
                .withMethodName("my*")
                .withArgumentTypes(new RegularMethodArguments(Arrays.asList(
                        new WildcardString("String"), new WildcardString("int"),
                        new WildcardString("mypackage.*"))))
                .withExceptionsThrown(Arrays.asList("mypackage.*", "IllegalArgumentException"))
                .build();

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void shouldParseMethodPointcutWithArgumentsWildcard() {
        MethodPointcut actual = parser.parse("* mypackage.*.*(..)");

        MethodPointcut expected = new MethodPointcut.Builder()
                .withKind("")
                .withModifiers(Collections.emptyList())
                .withReturnType("*")
                .withClassName("mypackage.*")
                .withMethodName("*")
                .withArgumentTypes(methodArgumentsFactory.create(Collections.singletonList("..")))
                .withExceptionsThrown(Collections.emptyList())
                .build();

        assertThat(actual).isEqualTo(expected);
    }
}
