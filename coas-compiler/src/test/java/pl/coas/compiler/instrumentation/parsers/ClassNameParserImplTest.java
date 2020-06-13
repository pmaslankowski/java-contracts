package pl.coas.compiler.instrumentation.parsers;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

import pl.coas.compiler.instrumentation.model.pointcut.WildcardString;

class ClassNameParserImplTest {

    private ClassNameParserImpl parser = new ClassNameParserImpl();

    @Test
    void shouldParseSimpleClassName() {
        WildcardString actual = parser.parse("String");

        WildcardString expected = new WildcardString("String");

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void shouldParseSimpleClassNameWithWildcards() {
        WildcardString actual = parser.parse("Str*ng");

        WildcardString expected = new WildcardString("Str*ng");

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void shouldParseFullyQualifiedClassName() {
        WildcardString actual = parser.parse("java.lang.String");

        WildcardString expected = new WildcardString("java.lang.String");

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void shouldParseFullyQualifiedClassNameWithWildcards() {
        WildcardString actual = parser.parse("java.l*ng.Strin*g");

        WildcardString expected = new WildcardString("java.l*ng.Strin*g");

        assertThat(actual).isEqualTo(expected);
    }
}
