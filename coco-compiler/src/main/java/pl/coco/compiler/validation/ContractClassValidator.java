package pl.coco.compiler.validation;

import javax.inject.Inject;
import javax.inject.Singleton;

import com.sun.tools.javac.tree.JCTree.JCClassDecl;

import pl.coco.compiler.validation.invariant.UniqueInvariantMethodValidator;
import pl.coco.compiler.validation.invariant.UniqueInvariantMethodValidatorFactory;

@Singleton
public class ContractClassValidator {

    private final UniqueInvariantMethodValidatorFactory uniqueInvariantMethodValidatorFactory;

    @Inject
    public ContractClassValidator(
            UniqueInvariantMethodValidatorFactory uniqueInvariantMethodValidatorFactory) {
        this.uniqueInvariantMethodValidatorFactory = uniqueInvariantMethodValidatorFactory;
    }

    public boolean isValid(ClassValidationInput input) {
        try {
            checkIfInvariantMethodsAreUnique(input);
            return true;
        } catch (ContractValidationException ex) {
            return false;
        }
    }

    private void checkIfInvariantMethodsAreUnique(ClassValidationInput input) {

        JCClassDecl clazz = input.getClazz();
        UniqueInvariantMethodValidator validator =
                uniqueInvariantMethodValidatorFactory.create(input);
        clazz.accept(validator);
    }
}
