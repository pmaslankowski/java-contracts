
public aspect Advice_100_28 {

    private int counter = 0;

    pointcut test(): execution(int coas.perf.TargetClass100.Subject100.target(..));
    int around(): test() {

        counter += 1;
        return proceed();
    }
}
