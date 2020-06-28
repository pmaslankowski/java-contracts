package pl.coas.compiler.instrumentation;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import javax.inject.Singleton;

import pl.coas.compiler.instrumentation.model.Aspect;
import pl.coas.compiler.instrumentation.model.JoinPoint;

@Singleton
public class AspectRegistry {

    private final List<Aspect> aspects = new ArrayList<>();

    public void registerAspect(Aspect aspect) {
        aspects.add(aspect);
    }

    public List<Aspect> getMatchingAspects(JoinPoint joinPoint) {
        return aspects.stream()
                .filter(aspect -> aspect.matches(joinPoint))
                .sorted(Comparator.comparingInt(Aspect::getOrder))
                .collect(Collectors.toList());
    }
}
