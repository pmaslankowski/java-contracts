package pl.coco.coco.invariant;

import pl.coco.api.code.Contract;
import pl.coco.api.code.InvariantMethod;

public class InvariantSubject10 {

    private int field = 10;

    @InvariantMethod
    public void invariants() {
        Contract.classInvariant(field != 0);
        Contract.classInvariant(field != 1);
        Contract.classInvariant(field != 2);
        Contract.classInvariant(field != 3);
        Contract.classInvariant(field != 4);
        Contract.classInvariant(field != 5);
        Contract.classInvariant(field != 6);
        Contract.classInvariant(field != 7);
        Contract.classInvariant(field != 8);
        Contract.classInvariant(field != 9);
    }

    public int target(int x) {
        if (field != Integer.MIN_VALUE) {
            field--;
        } else {
            field = 0;
        }
        return x;
    }
}
