package pl.coas.api;

/**
 * Class wrapping checked exceptions thrown from aspect advices.
 * 
 * @author pmaslankowski
 */
public class WrappedCheckedException extends RuntimeException {

    public WrappedCheckedException(Exception cause) {
        super("A checked exception has been thrown from an aspect advice.", cause);
    }
}
