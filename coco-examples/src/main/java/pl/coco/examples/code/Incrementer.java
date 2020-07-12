package pl.coco.examples.code;

import java.util.concurrent.atomic.AtomicInteger;

import pl.coco.api.code.Contract;

public class Incrementer {

    public void increment(AtomicInteger x) {
        Contract.ensures(x.get() == Contract.old(x).incrementAndGet());
        x.addAndGet(1);
    }
}
