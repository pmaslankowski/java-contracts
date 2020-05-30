package pl.coco.api.code;

import java.util.Arrays;
import java.util.function.BiPredicate;
import java.util.function.Predicate;
import java.util.stream.IntStream;
import java.util.stream.StreamSupport;

/**
 * Class providing the imperative API for specifying contracts.
 *
 * @author pmaslankowski
 */
public class Contract {

    private static final String MESSAGE =
            "This method call should not be present in compiled code. "
                    + "Make sure that coco javac plugin is correctly used "
                    + "(add flag -Xplugin:coco to javac arguments)";

    /**
     * Specifies a precondition for a method.
     *
     * @param precondition precondition
     */
    public static void requires(boolean precondition) {
        throw new IllegalStateException(MESSAGE);
    }

    /**
     * Specifies a postcondition from a method.
     *
     * @param postcondition postcondition
     */
    public static void ensures(boolean postcondition) {
        throw new IllegalStateException(MESSAGE);
    }

    /**
     * Represents a value returned from a method. Can be used inside contract specification.
     *
     * @param type value type
     * @param <T> value type
     * @return value returned from a method containing <i>result()</i> call
     */
    public static <T> T result(Class<T> type) {
        throw new IllegalStateException(MESSAGE);
    }

    /**
     * Specifies class invariant. Can be used inside invariant methods only.
     * 
     * @param condition condition
     * @see InvariantMethod
     */
    public static void invariant(boolean condition) {
        throw new IllegalStateException(MESSAGE);
    }

    /**
     * Specifies that given predicate must be true for all objects in given array.
     * 
     * @param objects array of objects that the predicate will be tested on
     * @param predicate predicate
     * @param <T> type of values in array
     * @return <i>true</i> if predicate is true for all objects in given array, <i>false</i>
     *         otherwise
     */
    public static <T> boolean forAll(T[] objects, Predicate<? super T> predicate) {
        return Arrays.stream(objects).allMatch(predicate);
    }

    /**
     * Specifies that given predicate must be true for all pair of objects in given array.
     *
     * @param objects iterable of objects that the predicate will be tested on
     * @param predicate bi-predicate
     * @param <T> type of values in iterable
     * @return <i>true</i> if predicate is true for all pairs of objects in given array,
     *         <i>false</i> otherwise
     */
    public static <T> boolean forAll(T[] objects, BiPredicate<T, T> predicate) {
        for (T obj1 : objects) {
            for (T obj2 : objects) {
                if (!predicate.test(obj1, obj2)) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Specifies that given predicate must be true for all objects in given iterable.
     *
     * @param objects iterable of objects that the predicate will be tested on
     * @param predicate predicate
     * @param <T> type of values in iterable
     * @return <i>true</i> if predicate is true for all objects in given array, <i>false</i>
     *         otherwise
     */
    public static <T> boolean forAll(Iterable<T> objects, Predicate<? super T> predicate) {
        return StreamSupport.stream(objects.spliterator(), false).allMatch(predicate);
    }

    /**
     * Specifies that given predicate must be true for all pair of objects in given iterable.
     *
     * @param objects iterable of objects that the predicate will be tested on
     * @param predicate bi-predicate
     * @param <T> type of values in iterable
     * @return <i>true</i> if predicate is true for all pairs of objects in given array,
     *         <i>false</i> otherwise
     */
    public static <T> boolean forAll(Iterable<T> objects, BiPredicate<T, T> predicate) {
        for (T obj1 : objects) {
            for (T obj2 : objects) {
                if (!predicate.test(obj1, obj2)) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Specifies that given predicate must be true for at least one of the objects in given array.
     *
     * @param objects array of objects that the predicate will be tested on
     * @param predicate predicate
     * @param <T> type of values in array
     * @return <i>true</i> if predicate is true for at least one of the objects in given array,
     *         <i>false</i> otherwise
     */
    public static <T> boolean exists(T[] objects, Predicate<? super T> predicate) {
        return Arrays.stream(objects).anyMatch(predicate);
    }

    /**
     * Specifies that given predicate must be true for at least one pair of objects in given array.
     *
     * @param objects iterable of objects that the predicate will be tested on
     * @param predicate bi-predicate
     * @param <T> type of values in iterable
     * @return <i>true</i> if predicate is true for at least one pair of objects in given array,
     *         <i>false</i> otherwise
     */
    public static <T> boolean exists(T[] objects, BiPredicate<T, T> predicate) {
        for (T obj1 : objects) {
            for (T obj2 : objects) {
                if (predicate.test(obj1, obj2)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Specifies that given predicate must be true for at least one of the objects in given
     * iterable.
     *
     * @param objects iterable of objects that the predicate will be tested on
     * @param predicate predicate
     * @param <T> type of values in iterable
     * @return <i>true</i> if predicate is true for at least one of the objects in given iterable,
     *         <i>false</i> otherwise
     */
    public static <T> boolean exists(Iterable<T> objects, Predicate<? super T> predicate) {
        return StreamSupport.stream(objects.spliterator(), false).anyMatch(predicate);
    }

    /**
     * Specifies that given predicate must be true for at least one pair of objects in given array.
     *
     * @param objects iterable of objects that the predicate will be tested on
     * @param predicate bi-predicate
     * @param <T> type of values in iterable
     * @return <i>true</i> if predicate is true for at least one pair of objects in given array,
     *         <i>false</i> otherwise
     */
    public static <T> boolean exists(Iterable<T> objects, BiPredicate<T, T> predicate) {
        for (T obj1 : objects) {
            for (T obj2 : objects) {
                if (predicate.test(obj1, obj2)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Returns array representing exclusive range [0, ..., high - 1].
     * 
     * @param high higher boundary
     * @return array representing range [0, ..., high - 1]
     */
    public static Integer[] range(int high) {
        return range(0, high);
    }

    /**
     * Returns array representing exclusive range [low, ..., high - 1].
     * 
     * @param low lower boundary
     * @param high higher boundary
     * @return array representing range [0, ..., high -1]
     */
    public static Integer[] range(int low, int high) {
        return IntStream.range(low, high).boxed().toArray(Integer[]::new);
    }

    /**
     * Represents logical impliation.
     * 
     * @param premise premise
     * @param conclusion conclusion
     * @return logical value of expression: premise => conclusion
     */
    public static boolean implies(boolean premise, boolean conclusion) {
        return !premise || conclusion;
    }

}
