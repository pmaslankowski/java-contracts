package pl.coco.examples.paper;

import pl.coco.api.code.Contract;

public class Base {

    public int foo(int x) {
        Contract.requires(x > 0);

        return x;
    }
}
