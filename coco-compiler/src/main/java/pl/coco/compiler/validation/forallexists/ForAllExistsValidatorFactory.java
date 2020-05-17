package pl.coco.compiler.validation.forallexists;

import javax.inject.Inject;

import pl.coco.compiler.validation.ErrorProducer;
import pl.coco.compiler.validation.MethodValidationInput;

public class ForAllExistsValidatorFactory {

    private final ErrorProducer errorProducer;

    @Inject
    public ForAllExistsValidatorFactory(ErrorProducer errorProducer) {
        this.errorProducer = errorProducer;
    }

    public ForAllExistsValidator create(MethodValidationInput input) {
        return new ForAllExistsValidator(errorProducer, input);
    }
}
