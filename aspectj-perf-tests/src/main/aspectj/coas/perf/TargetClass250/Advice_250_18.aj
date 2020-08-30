
public aspect Advice_250_18 {

    private int counter = 0;

    pointcut test(): execution(int coas.perf.TargetClass250.Subject250.target(..));
    int around(): test() {

        counter += 1;
        return proceed();
    }
}
