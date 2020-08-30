
public aspect Advice_50_44 {

    private int counter = 0;

    pointcut test(): execution(int coas.perf.TargetClass50.Subject50.target(..));
    int around(): test() {

        counter += 1;
        return proceed();
    }
}
