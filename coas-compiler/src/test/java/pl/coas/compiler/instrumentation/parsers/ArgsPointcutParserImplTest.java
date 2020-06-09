package pl.coas.compiler.instrumentation.parsers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.Set;

import org.junit.jupiter.api.Test;

import com.google.common.collect.Sets;

import pl.coas.compiler.instrumentation.model.pointcut.ArgsPointcut;
import pl.coas.compiler.instrumentation.model.pointcut.WildcardString;

class ArgsPointcutParserImplTest {

    private final ArgsPointcutParserImpl parser = new ArgsPointcutParserImpl();

    @Test
    void shouldParseSingleSimpleType() {
        ArgsPointcut pointcut = parser.parse("String");

        WildcardString expected = new WildcardString("String");

        assertThat(pointcut.getArgumentTypes()).containsExactlyInAnyOrder(expected);
    }

    @Test
    void shouldParseSingleSimpleTypeWithWildcards() {
        ArgsPointcut pointcut = parser.parse("St*in*");

        WildcardString expected = new WildcardString("St*in*");

        assertThat(pointcut.getArgumentTypes()).containsExactlyInAnyOrder(expected);
    }

    @Test
    void shouldParseSingleType() {
        ArgsPointcut pointcut = parser.parse("com.mypackage.MyClass");

        WildcardString expected = new WildcardString("com.mypackage.MyClass");

        assertThat(pointcut.getArgumentTypes()).containsExactlyInAnyOrder(expected);
    }

    @Test
    void shouldParseSingleTypeWithWildcards() {
        ArgsPointcut pointcut = parser.parse("com.my*.*Class");

        WildcardString expected = new WildcardString("com.my*.*Class");

        assertThat(pointcut.getArgumentTypes()).containsExactlyInAnyOrder(expected);
    }

    @Test
    void shouldParseMultipleTypesWithWildcards() {
        ArgsPointcut pointcut = parser.parse("com.my*.*Class, java.lang.*, String");

        Set<WildcardString> expected = Sets.newHashSet(
                new WildcardString("com.my*.*Class"),
                new WildcardString("java.lang.*"),
                new WildcardString("String"));

        assertThat(pointcut.getArgumentTypes()).containsExactlyInAnyOrderElementsOf(expected);
    }

    @Test
    void shouldThrowExceptionWhenExpressionIsMalformed() {
        assertThatThrownBy(() -> parser.parse("com.my*.*Class, throwing java.lang.*, String"))
                .isInstanceOf(ParsingException.class);
    }
}
