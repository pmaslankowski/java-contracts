package pl.coco.compiler.validation;

import javax.inject.Inject;
import javax.inject.Singleton;

import com.sun.tools.javac.tree.JCTree.JCClassDecl;

import pl.coco.compiler.validation.invariant.InvariantMethodValidator;
import pl.coco.compiler.validation.invariant.InvariantMethodValidatorFactory;

@Singleton
public class ContractClassValidator {

    private final InvariantMethodValidatorFactory invariantMethodValidatorFactory;

    @Inject
    public ContractClassValidator(
            InvariantMethodValidatorFactory invariantMethodValidatorFactory) {
        this.invariantMethodValidatorFactory = invariantMethodValidatorFactory;
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
        InvariantMethodValidator validator =
                invariantMethodValidatorFactory.create(input);
        clazz.accept(validator);
    }
}
