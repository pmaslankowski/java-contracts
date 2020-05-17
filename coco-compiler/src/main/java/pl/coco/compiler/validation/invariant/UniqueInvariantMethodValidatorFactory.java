package pl.coco.compiler.validation.invariant;

import javax.inject.Inject;
import javax.inject.Singleton;

import pl.coco.compiler.validation.ClassValidationInput;
import pl.coco.compiler.validation.ErrorProducer;

@Singleton
public class UniqueInvariantMethodValidatorFactory {

    private final ErrorProducer errorProducer;

    @Inject
    public UniqueInvariantMethodValidatorFactory(ErrorProducer errorProducer) {
        this.errorProducer = errorProducer;
    }

    public UniqueInvariantMethodValidator create(ClassValidationInput input) {
        return new UniqueInvariantMethodValidator(errorProducer, input);
    }
}
