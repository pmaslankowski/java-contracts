package pl.coco.api;

import java.util.Arrays;
import java.util.function.Predicate;
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

    // TODO: add validation that ensures that this method is used inside invariants only
    /**
     * Specifies class invariant. Can be used inside invariant methods only.
     * 
     * @param condition condition
     * @see Invariant
     */
    public static void invariant(boolean condition) {
        throw new IllegalStateException(MESSAGE);
    }

    // TODO: add validation that those methods are called only in contract context
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
}
