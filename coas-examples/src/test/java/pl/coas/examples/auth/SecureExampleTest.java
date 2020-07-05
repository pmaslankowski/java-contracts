package pl.coas.examples.auth;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;

class SecureExampleTest {

    private SecureExample example = new SecureExample();

    @Test
    void testSensitiveMethod() {
        assertThatThrownBy(() -> example.methodWithSensitiveData(10))
                .isInstanceOf(SecurityException.class);
    }
}
