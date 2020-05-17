package pl.coco.compiler.validation.invariant;

import javax.inject.Inject;
import javax.inject.Singleton;

import pl.coco.compiler.validation.ClassValidationInput;
import pl.coco.compiler.validation.ErrorProducer;

@Singleton
public class InvariantMethodValidatorFactory {

    private final ErrorProducer errorProducer;

    @Inject
    public InvariantMethodValidatorFactory(ErrorProducer errorProducer) {
        this.errorProducer = errorProducer;
    }

    public InvariantMethodValidator create(ClassValidationInput input) {
        return new InvariantMethodValidator(errorProducer, input);
    }
}
