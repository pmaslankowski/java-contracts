
public aspect Advice_500_215 {

    private int counter = 0;

    pointcut test(): execution(int coas.perf.TargetClass500.Subject500.target(..));
    int around(): test() {

        counter += 1;
        return proceed();
    }
}
