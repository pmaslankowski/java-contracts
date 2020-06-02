package pl.coco.examples.annotation.min;

import java.util.Collection;

public class IncorrectMinCalculatorWithAnnotations extends MinCalculatorWithAnnotations {

    @Override
    public int min(Collection<Integer> collection) {
        return 10;
    }
}
