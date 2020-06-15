package pl.coas.util;

import java.util.Optional;
import java.util.stream.Stream;

public class StreamUtil {

    public static <T> Stream<T> optionalStream(Optional<T> optional) {
        return optional.map(Stream::of)
                .orElseGet(Stream::empty);
    }
}
