package pl.coco.compiler.instrumentation.invocation.arguments;

import javax.inject.Inject;

import pl.coco.compiler.instrumentation.ContractMethod;

public class ArgumentsProcessorFactory {

    private final EnsuresArgumentsProcessor ensuresArgsProcessor;
    private final RequiresArgumentsProcessor requiresArgsProcessor;
    private final InvariantArgumentsProcessor invariantArgsProcessor;

    @Inject
    public ArgumentsProcessorFactory(EnsuresArgumentsProcessor ensuresArgsProcessor,
            RequiresArgumentsProcessor requiresArgsProcessor,
            InvariantArgumentsProcessor invariantArgsProcessor) {

        this.ensuresArgsProcessor = ensuresArgsProcessor;
        this.requiresArgsProcessor = requiresArgsProcessor;
        this.invariantArgsProcessor = invariantArgsProcessor;
    }

    public ArgumentsProcessor newArgumentsProcessor(ContractMethod method) {
        switch (method) {
            case ENSURES:
                return ensuresArgsProcessor;
            case REQUIRES:
                return requiresArgsProcessor;
            case INVARIANT:
                return invariantArgsProcessor;
            default:
                throw new IllegalArgumentException(
                        "Contract method: " + method + " is not supported in this class.");
        }
    }
}
