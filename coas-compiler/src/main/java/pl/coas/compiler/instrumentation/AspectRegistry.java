package pl.coas.compiler.instrumentation;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Singleton;

import pl.coas.compiler.instrumentation.model.Aspect;

@Singleton
public class AspectRegistry {

    private final List<Aspect> aspects = new ArrayList<>();

    public void registerAspect(Aspect aspect) {
        aspects.add(aspect);
    }
}
