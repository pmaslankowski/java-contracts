package pl.coco.compiler.validation.result;

import javax.inject.Inject;
import javax.inject.Singleton;

import pl.coco.compiler.validation.ErrorProducer;
import pl.coco.compiler.validation.MethodValidationInput;

@Singleton
public class ContractResultValidatorFactory {

    private final ErrorProducer errorProducer;

    @Inject
    public ContractResultValidatorFactory(ErrorProducer errorProducer) {
        this.errorProducer = errorProducer;
    }

    public ContractResultValidator create(MethodValidationInput input) {
        return new ContractResultValidator(errorProducer, input);
    }
}
