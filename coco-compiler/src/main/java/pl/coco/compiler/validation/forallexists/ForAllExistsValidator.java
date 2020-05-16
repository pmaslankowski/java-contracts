package pl.coco.compiler.validation.forallexists;

import com.sun.tools.javac.tree.JCTree.JCMethodInvocation;
import com.sun.tools.javac.tree.TreeScanner;

import pl.coco.compiler.instrumentation.invocation.ContractInvocation;
import pl.coco.compiler.util.ContractAstUtil;
import pl.coco.compiler.validation.ContractError;
import pl.coco.compiler.validation.ErrorProducer;
import pl.coco.compiler.validation.ValidationInput;

public class ForAllExistsValidator extends TreeScanner {

    private final ErrorProducer errorProducer;
    private final ValidationInput input;

    private boolean isInsideSpecification = false;

    public ForAllExistsValidator(ErrorProducer errorProducer, ValidationInput input) {
        this.errorProducer = errorProducer;
        this.input = input;
    }

    @Override
    public void visitApply(JCMethodInvocation invocation) {
        if (ContractAstUtil.isContractInvocation(invocation)) {
            ContractInvocation contract = ContractAstUtil.getContractInvocation(invocation);
            if (contract.isContractSpecification()) {
                handleContractSpecification(invocation);
            }
            if (contract.canOccurInsideContractSpecificationOnly()) {
                checkIfContractIsInsideSpecification(invocation);
            }
            super.visitApply(invocation);
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
        }
    }
}
