package pl.coco.compiler.validation.invariant;

import com.sun.tools.javac.tree.JCTree.JCMethodDecl;
import com.sun.tools.javac.tree.TreeScanner;

import pl.coco.compiler.util.ContractAstUtil;
import pl.coco.compiler.validation.ClassValidationInput;
import pl.coco.compiler.validation.ContractError;
import pl.coco.compiler.validation.ContractValidationException;
import pl.coco.compiler.validation.ErrorProducer;

public class UniqueInvariantMethodValidator extends TreeScanner {

    private final ErrorProducer errorProducer;
    private final ClassValidationInput input;

    private boolean invariantAlreadyMethodOccured = false;

    public UniqueInvariantMethodValidator(ErrorProducer errorProducer, ClassValidationInput input) {
        this.errorProducer = errorProducer;
        this.input = input;
    }

    // TODO: zrobić tak, żeby z walidatorów rzucać tylko wyjątek, który zostanie u góry zmieniony na
    // wiadomość
    @Override
    public void visitMethodDef(JCMethodDecl method) {
        if (ContractAstUtil.isInvariantMethod(method)) {
            if (invariantAlreadyMethodOccured) {
                errorProducer.raiseError(ContractError.MULTIPLE_INVARIANT_METHODS_IN_THE_SAME_CLASS,
                        method, input.getCompilationUnit());
                throw new ContractValidationException(
                        ContractError.MULTIPLE_INVARIANT_METHODS_IN_THE_SAME_CLASS);
            }
            invariantAlreadyMethodOccured = true;
        }

        super.visitMethodDef(method);
    }
}
