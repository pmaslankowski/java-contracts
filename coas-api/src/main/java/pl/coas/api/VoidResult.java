package pl.coas.api;

/**
 * Class used as a return value for {@link JoinPoint#proceed} when adviced method is void.
 */
public class VoidResult {

    public static final VoidResult INSTANCE = new VoidResult();

    private VoidResult() {

    }
}
