
public aspect Advice_150_95 {


    pointcut test(): execution(int coas.perf.TargetClass150.Subject150.target(..));
    int around(): test() {

        int res = proceed();
        
        for (int i=0; i < 1000; i++) {
            if (res % 2 == 0) {
                res /= 2;
            } else {
                res = 2 * res + 1;
            }
        }

        return res;

    }
}
