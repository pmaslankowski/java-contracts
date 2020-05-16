package pl.coco.compiler.validation.forallexists;

import javax.inject.Inject;

import pl.coco.compiler.validation.ErrorProducer;
import pl.coco.compiler.validation.ValidationInput;

public class ForAllExistsValidatorFactory {

    private final ErrorProducer errorProducer;

    @Inject
    public ForAllExistsValidatorFactory(ErrorProducer errorProducer) {
        this.errorProducer = errorProducer;
    }

    public ForAllExistsValidator create(ValidationInput input) {
        return new ForAllExistsValidator(errorProducer, input);
    }
}
