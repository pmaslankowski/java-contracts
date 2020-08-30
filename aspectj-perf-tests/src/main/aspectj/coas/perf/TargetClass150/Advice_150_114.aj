
public aspect Advice_150_114 {

    private int counter = 0;

    pointcut test(): execution(int coas.perf.TargetClass150.Subject150.target(..));
    int around(): test() {

        counter += 1;
        return proceed();
    }
}
