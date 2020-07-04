package pl.coas.api.annotation;

import pl.coas.api.AspectType;

public @interface Aspect {

    String on();

    int order();

    AspectType type();
}
