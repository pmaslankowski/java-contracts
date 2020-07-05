package pl.coas.examples.code.monitoring;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

import pl.coas.examples.monitoring.MonitoringExample;

class MonitoringExampleTest {

    private MonitoringExample monitoringExample = new MonitoringExample();

    @Test
    void test() {
        int res = monitoringExample.monitoredMethod(5);
        int res2 = monitoringExample.nonMonitoredMethod();

        assertThat(true).isTrue();
    }
}
