package pl.coco.api;

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
}
