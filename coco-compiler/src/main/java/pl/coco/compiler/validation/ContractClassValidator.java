package pl.coco.compiler.validation;

import javax.inject.Inject;
import javax.inject.Singleton;

import com.sun.tools.javac.tree.JCTree.JCClassDecl;

import pl.coco.compiler.validation.invariant.InvariantMethodValidator;

@Singleton
public class ContractClassValidator {

    private final ErrorProducer errorProducer;

    @Inject
    public ContractClassValidator(ErrorProducer errorProducer) {
        this.errorProducer = errorProducer;
    }

    public boolean isValid(ClassValidationInput input) {
        try {
            checkIfInvariantMethodsAreUnique(input);
            return true;
        } catch (ContractValidationException ex) {
            errorProducer.produceErrorMessage(ex);
            return false;
        }
    }

    private void checkIfInvariantMethodsAreUnique(ClassValidationInput input) {
        JCClassDecl clazz = input.getClazz();
        clazz.accept(new InvariantMethodValidator(input));
    }
}
