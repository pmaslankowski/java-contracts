package pl.coco.examples.paper;

import pl.coco.api.code.Contract;

public class OldExample {

    public int foo(int x, Bar y) {
        Contract.ensures(Contract.old(y).bar != y.bar);
        y.bar = "new value";
        return x;
    }
}
