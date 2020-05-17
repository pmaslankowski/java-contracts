package pl.coco.compiler.validation;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class ContractValidator {

    private final ContractClassValidator classValidator;
    private final ContractMethodValidator methodValidator;

    @Inject
    public ContractValidator(ContractClassValidator classValidator,
            ContractMethodValidator methodValidator) {
        this.classValidator = classValidator;
        this.methodValidator = methodValidator;
    }

    public boolean isMethodValid(MethodValidationInput input) {
        return methodValidator.isValid(input);
    }

    public boolean isClassValid(ClassValidationInput input) {
        return classValidator.isValid(input);
    }
}
