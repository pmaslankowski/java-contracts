package pl.coas.compiler.instrumentation.parsers;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class ClassNameParserImplTest {

    private ClassNameParserImpl parser = new ClassNameParserImpl();

    @Test
    void shouldParseSimpleClassName() {
        String actual = parser.parse("String");

        assertThat(actual).isEqualTo("String");
    }

    @Test
    void shouldParseSimpleClassNameWithWildcards() {
        String actual = parser.parse("Str*ng");

        assertThat(actual).isEqualTo("Str*ng");
    }

    @Test
    void shouldParseFullyQualifiedClassName() {
        String actual = parser.parse("java.lang.String");

        assertThat(actual).isEqualTo("java.lang.String");
    }

    @Test
    void shouldParseFullyQualifiedClassNameWithWildcards() {
        String actual = parser.parse("java.l*ng.Strin*g");

        assertThat(actual).isEqualTo("java.l*ng.Strin*g");
    }
}
