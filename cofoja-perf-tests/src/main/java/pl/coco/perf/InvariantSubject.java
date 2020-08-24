package pl.coco.perf;

public class InvariantSubject {

    private int field = 0;

    public int target(int x) {
        if (field != Integer.MIN_VALUE) {
            field--;
        } else {
            field = 0;
        }
        return x;
    }
}
