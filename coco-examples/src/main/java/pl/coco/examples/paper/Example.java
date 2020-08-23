package pl.coco.examples.paper;

import pl.coco.api.code.Contract;

public class Example {

    public int foo(int arg) {
        Contract.requires(arg > 0);
        Contract.ensures(Contract.result(Integer.class) > 0);

        return arg;
    }
}
