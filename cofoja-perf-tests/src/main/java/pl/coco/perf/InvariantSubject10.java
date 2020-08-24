package pl.coco.perf;

import com.google.java.contract.Invariant;

@Invariant({
        "field != 0",
        "field != 1",
        "field != 2",
        "field != 3",
        "field != 4",
        "field != 5",
        "field != 6",
        "field != 7",
        "field != 8",
        "field != 9"
})
public class InvariantSubject10 {

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
