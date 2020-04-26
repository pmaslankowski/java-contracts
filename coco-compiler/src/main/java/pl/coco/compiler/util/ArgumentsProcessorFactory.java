package pl.coco.compiler.util;

import javax.inject.Inject;

import pl.coco.compiler.ContractMethod;

public class ArgumentsProcessorFactory {

    private final EnsuresArgumentsProcessor ensuresArgsProcessor;
    private final RequiresArgumentsProcessor requiresArgsProcessor;

    @Inject
    public ArgumentsProcessorFactory(EnsuresArgumentsProcessor ensuresArgsProcessor,
            RequiresArgumentsProcessor requiresArgsProcessor) {

        this.ensuresArgsProcessor = ensuresArgsProcessor;
        this.requiresArgsProcessor = requiresArgsProcessor;
    }

    public ArgumentsProcessor newArgumentsProcessor(ContractMethod method) {
        switch (method) {
            case ENSURES:
                return ensuresArgsProcessor;
            case REQUIRES:
                return requiresArgsProcessor;
            default:
                throw new IllegalArgumentException(
                        "Contract method: " + method + " is not supported in this class.");
        }
    }
}
