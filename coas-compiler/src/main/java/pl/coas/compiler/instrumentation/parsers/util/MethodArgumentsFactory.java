package pl.coas.compiler.instrumentation.parsers.util;

import java.util.List;
import java.util.stream.Collectors;

import javax.inject.Singleton;

import pl.coas.compiler.instrumentation.model.pointcut.MethodArguments;
import pl.coas.compiler.instrumentation.model.pointcut.MethodArgumentsWildcard;
import pl.coas.compiler.instrumentation.model.pointcut.RegularMethodArguments;
import pl.coas.compiler.instrumentation.model.pointcut.WildcardString;

@Singleton
public class MethodArgumentsFactory {

    private static final String ARGS_WILDCARD = "..";
    private static final MethodArguments ARGS_WILDCARD_INSTANCE = new MethodArgumentsWildcard();

    public MethodArguments create(List<String> args) {
        if (args.size() == 1 && args.get(0).equals(ARGS_WILDCARD)) {
            return ARGS_WILDCARD_INSTANCE;
        } else {
            List<WildcardString> argsAsWildcardStrings = args.stream()
                    .map(WildcardString::new)
                    .collect(Collectors.toList());
            return new RegularMethodArguments(argsAsWildcardStrings);
        }
    }
}
