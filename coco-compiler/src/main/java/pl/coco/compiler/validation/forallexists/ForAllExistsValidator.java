package pl.coco.compiler.validation.forallexists;

import com.sun.tools.javac.tree.JCTree.JCMethodInvocation;
import com.sun.tools.javac.tree.TreeScanner;

import pl.coco.compiler.instrumentation.ContractMethod;
import pl.coco.compiler.instrumentation.invocation.ContractInvocation;
import pl.coco.compiler.util.ContractAstUtil;
import pl.coco.compiler.validation.ContractError;
import pl.coco.compiler.validation.ContractValidationException;
import pl.coco.compiler.validation.ErrorProducer;
import pl.coco.compiler.validation.MethodValidationInput;

public class ForAllExistsValidator extends TreeScanner {

    private final ErrorProducer errorProducer;
    private final MethodValidationInput input;

    private boolean isInsideSpecification = false;

    public ForAllExistsValidator(ErrorProducer errorProducer, MethodValidationInput input) {
        this.errorProducer = errorProducer;
        this.input = input;
    }

    @Override
    public void visitApply(JCMethodInvocation invocation) {
        if (ContractAstUtil.isContractInvocation(invocation)) {
            ContractInvocation contract = ContractAstUtil.getContractInvocation(invocation);
            ContractMethod contractMethod = contract.getContractMethod();
            if (contractMethod.isContractSpecification()) {
                handleContractSpecification(invocation);
            } else {
                if (contractMethod.canOccurInsideSpecificationOnly()) {
                    checkIfContractIsInsideSpecification(invocation);
                }
                super.visitApply(invocation);
            }
        }
    }

    private void handleContractSpecification(JCMethodInvocation invocation) {
        isInsideSpecification = true;
        super.visitApply(invocation);
        isInsideSpecification = false;
    }

    private void checkIfContractIsInsideSpecification(JCMethodInvocation invocation) {
        if (!isInsideSpecification) {
            errorProducer.raiseError(
                    ContractError.CONTRACT_STATEMENT_OUTSIDE_OF_CONTRACTS, invocation,
                    input.getCompilationUnit());
            throw new ContractValidationException(
                    ContractError.CONTRACT_STATEMENT_OUTSIDE_OF_CONTRACTS);
        }
    }
}
