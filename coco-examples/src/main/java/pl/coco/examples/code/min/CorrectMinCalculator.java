package pl.coco.examples.code.min;

import java.util.Collection;

public class CorrectMinCalculator extends MinCalculator {

    @Override
    public int min(Collection<Integer> collection) {
        return collection.stream().mapToInt(Integer::intValue).min().getAsInt();
    }
}
