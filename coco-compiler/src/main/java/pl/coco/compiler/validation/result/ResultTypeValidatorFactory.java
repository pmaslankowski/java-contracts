package pl.coco.compiler.validation.result;

import javax.inject.Inject;
import javax.inject.Singleton;

import com.sun.tools.javac.code.Types;

import pl.coco.compiler.validation.MethodValidationInput;

@Singleton
public class ResultTypeValidatorFactory {

    private final Types types;

    @Inject
    public ResultTypeValidatorFactory(Types types) {
        this.types = types;
    }

    public ResultTypeValidator create(MethodValidationInput input) {
        return new ResultTypeValidator(input, types);
    }
}
