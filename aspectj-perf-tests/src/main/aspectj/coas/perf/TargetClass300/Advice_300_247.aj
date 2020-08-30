
public aspect Advice_300_247 {

    private int counter = 0;

    pointcut test(): execution(int coas.perf.TargetClass300.Subject300.target(..));
    int around(): test() {

        counter += 1;
        return proceed();
    }
}
