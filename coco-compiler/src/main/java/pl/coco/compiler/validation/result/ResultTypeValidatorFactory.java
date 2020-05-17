package pl.coco.compiler.validation.result;

import javax.inject.Inject;
import javax.inject.Singleton;

import com.sun.tools.javac.code.Types;

import pl.coco.compiler.validation.ErrorProducer;
import pl.coco.compiler.validation.MethodValidationInput;

@Singleton
public class ResultTypeValidatorFactory {

    private final ErrorProducer errorProducer;
    private final Types types;

    @Inject
    public ResultTypeValidatorFactory(ErrorProducer errorProducer, Types types) {
        this.errorProducer = errorProducer;
        this.types = types;
    }

    public ResultTypeValidator create(MethodValidationInput input) {
        return new ResultTypeValidator(errorProducer, input, types);
    }
}
