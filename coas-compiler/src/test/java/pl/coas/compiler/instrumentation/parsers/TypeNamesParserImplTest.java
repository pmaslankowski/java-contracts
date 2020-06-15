package pl.coas.compiler.instrumentation.parsers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;

import org.junit.jupiter.api.Test;

class TypeNamesParserImplTest {

    private final TypeNamesParserImpl parser = new TypeNamesParserImpl();

    @Test
    void shouldParseEmptyTypes() {
        List<String> actual = parser.parse("");

        assertThat(actual).isEmpty();
    }

    @Test
    void shouldParseSingleSimpleType() {
        List<String> actual = parser.parse("String");

        assertThat(actual).containsExactly("String");
    }

    @Test
    void shouldParseSingleSimpleTypeWithWildcards() {
        List<String> actual = parser.parse("St*in*");

        assertThat(actual).containsExactly("St*in*");
    }

    @Test
    void shouldParseSingleType() {
        List<String> actual = parser.parse("com.mypackage.MyClass");

        assertThat(actual).containsExactly("com.mypackage.MyClass");

    }

    @Test
    void shouldParseSingleTypeWithWildcards() {
        List<String> actual = parser.parse("com.my*.*Class");

        assertThat(actual).containsExactly("com.my*.*Class");
    }

    @Test
    void shouldParseMultipleTypesWithWildcards() {
        List<String> actual = parser.parse("com.my*.*Class, java.lang.*, String");

        assertThat(actual).containsExactly("com.my*.*Class", "java.lang.*", "String");
    }

    @Test
    void shouldThrowExceptionWhenExpressionIsMalformed() {
        assertThatThrownBy(() -> parser.parse("com.my*.*Class, throwing java.lang.*, String"))
                .isInstanceOf(ParsingException.class);
    }
}
