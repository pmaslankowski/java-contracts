package pl.coco.api;

/**
 * Class providing the imperative API for specifying contracts.
 *
 * @author pmaslankowski
 */
public class Contract {

    /**
     * Specifies a precondition for a method.
     *
     * @param precondition precondition
     */
    public static void requires(boolean precondition) {

    }

    /**
     * Specifies a postcondition from a method.
     *
     * @param postcondition postcondition
     */
    public static void ensures(boolean postcondition) {

    }

    /**
     * Represents a value returned from a method. Can be used inside contract specification.
     *
     * @param type value type
     * @param <T> value type
     * @return value returned from a method containing <i>result()</i> call
     */
    public static <T> T result(Class<T> type) {
        return null;
    }
}
