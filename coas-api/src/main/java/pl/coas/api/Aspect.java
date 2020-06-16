package pl.coas.api;

/**
 * Class providing imperative API for aspect specification.
 * 
 * @author pmaslankowski
 * @see Pointcut
 */
public class Aspect {

    private static final String MESSAGE =
            "This method call should not be present in compiled code. " +
                    "Make sure that coas javac plugin is correctly used " +
                    "(add flag -Xplugin:coas to javac arguments)";

    /**
     * Declares pointcut expression for an aspect.
     * 
     * @param pointcutExpression pointcut expression filtering matching joinpoints
     */
    public static void on(boolean pointcutExpression) {
        throw new IllegalStateException(MESSAGE);
    }

    /**
     * Specifies aspect type {@link AspectType}.
     * 
     * @param type aspect type
     */
    public static void type(AspectType type) {
        throw new IllegalStateException(MESSAGE);
    }

    /**
     * Specifies aspect ordering.
     * 
     * @param val aspect ordering value
     */
    public static void order(int val) {
        throw new IllegalStateException();
    }
}
