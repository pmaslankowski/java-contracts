package pl.coco.experiments;

import java.time.ZonedDateTime;

import pl.coco.api.code.Contract;

public class TestMath {

    private final int someNumber;
    private final ZonedDateTime date;

    public TestMath(int someNumber, ZonedDateTime date) {
        this.someNumber = someNumber;
        this.date = date;
    }

    public double sqrt(double x) {
        Contract.requires(x > 0 && date.isBefore(ZonedDateTime.now()));
        Contract.ensures(Contract.result(Double.class) > 0 && someNumber < 0);

        return Math.sqrt(x);
    }
}
