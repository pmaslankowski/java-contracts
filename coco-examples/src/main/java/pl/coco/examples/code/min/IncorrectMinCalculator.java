package pl.coco.examples.code.min;

import java.util.Collection;

public class IncorrectMinCalculator extends MinCalculator {

    @Override
    public int min(Collection<Integer> collection) {
        return 10;
    }
}
