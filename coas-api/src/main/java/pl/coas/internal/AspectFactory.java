package pl.coas.internal;

import java.util.function.Supplier;

public class AspectFactory {

    // TODO: dodać cachowanie
    public static <T> T getAspect(Class<T> clazz, Supplier<T> supplier) {
        return supplier.get();
    }
}
