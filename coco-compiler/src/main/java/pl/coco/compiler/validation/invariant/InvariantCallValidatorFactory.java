package pl.coco.compiler.validation.invariant;

import javax.inject.Inject;
import javax.inject.Singleton;

import pl.coco.compiler.validation.ErrorProducer;
import pl.coco.compiler.validation.MethodValidationInput;

@Singleton
public class InvariantCallValidatorFactory {

    private final ErrorProducer errorProducer;

    @Inject
    public InvariantCallValidatorFactory(ErrorProducer errorProducer) {
        this.errorProducer = errorProducer;
    }

    public InvariantCallValidator create(MethodValidationInput input) {
        return new InvariantCallValidator(errorProducer, input);
    }
}
