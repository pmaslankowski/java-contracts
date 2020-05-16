package pl.coco.compiler.util;

import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

public class CollectionUtils {

    public static <T> Optional<Integer> getIndexOfFirstElementMatchingPredicate(List<T> objects,
            Predicate<? super T> predicate) {

        for (int i = 0; i < objects.size(); i++) {
            T object = objects.get(i);
            if (predicate.test(object)) {
                return Optional.of(i);
            }
        }

        return Optional.empty();
    }

    public static <T> Optional<Integer> getIndexOfLastElementMatchingPredicate(List<T> objects,
            Predicate<? super T> predicate) {

        for (int i = objects.size() - 1; i >= 0; i--) {
            T object = objects.get(i);
            if (predicate.test(object)) {
                return Optional.of(i);
            }
        }

        return Optional.empty();
    }
}
