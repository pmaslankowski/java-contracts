package pl.coco.examples.annotation.min;

import java.util.Collection;

public class CorrectMinCalculatorWithAnnotations extends MinCalculatorWithAnnotations {

    @Override
    public int min(Collection<Integer> collection) {
        return collection.stream().mapToInt(Integer::intValue).min().getAsInt();
    }
}
