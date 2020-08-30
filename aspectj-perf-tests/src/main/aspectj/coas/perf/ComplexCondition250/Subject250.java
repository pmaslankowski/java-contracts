
package coas.perf.ComplexCondition250;

public class Subject250 {

    public int target(int x) {
        int[] fib = new int[x + 2];
        fib[0] = 0;
        fib[1] = 1;
        for (int i = 2; i <= x; i++) {
            fib[i] = fib[i - 1] + fib[i - 2];
        }
        return fib[x] % 10;
    }
}
