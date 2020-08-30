
public aspect Advice_200_40 {

    private int counter = 0;

    pointcut test(): execution(int coas.perf.TargetClass200.Subject200.target(..));
    int around(): test() {

        counter += 1;
        return proceed();
    }
}
