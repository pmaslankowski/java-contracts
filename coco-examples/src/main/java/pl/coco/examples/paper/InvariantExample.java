package pl.coco.examples.paper;

import pl.coco.api.code.Contract;
import pl.coco.api.code.InvariantMethod;

public class InvariantExample {

    private int x = 1;

    public void foo() {
        x += 1;
    }

    @InvariantMethod
    private void invariants() {
        Contract.classInvariant(x > 1);
    }
}
