package pl.coas.examples.monitoring;

public class MonitoringExample {

    @Monitored
    public int monitoredMethod(int arg) {
        return 0;
    }

    public int nonMonitoredMethod() {
        return 1;
    }
}
