package pl.coco.compiler.validation.old;

import com.sun.tools.javac.tree.JCTree.JCMethodInvocation;
import com.sun.tools.javac.tree.TreeScanner;

import pl.coco.compiler.instrumentation.ContractMethod;
import pl.coco.compiler.model.ContractInvocation;
import pl.coco.compiler.util.ContractAstUtil;
import pl.coco.compiler.validation.ContractError;
import pl.coco.compiler.validation.ContractValidationException;
import pl.coco.compiler.validation.MethodValidationInput;

public class OldValidator extends TreeScanner {

    private final MethodValidationInput input;

    private boolean isInsideEnsures = false;

    public OldValidator(MethodValidationInput input) {
        this.input = input;
    }

    @Override
    public void visitApply(JCMethodInvocation invocation) {
        if (ContractAstUtil.isContractInvocation(invocation)) {
            ContractInvocation contract = ContractAstUtil.getContractInvocation(invocation);
            ContractMethod contractMethod = contract.getContractMethod();
            if (contractMethod.isPostcondition()) {
                handlePostcondition(invocation);
            } else {
                if (contractMethod == ContractMethod.OLD) {
                    checkIfContractIsInsidePostcondition(invocation);
                }
                super.visitApply(invocation);
            }
        }
    }

    private void handlePostcondition(JCMethodInvocation invocation) {
        isInsideEnsures = true;
        super.visitApply(invocation);
        isInsideEnsures = false;
    }

    private void checkIfContractIsInsidePostcondition(JCMethodInvocation invocation) {
        if (!isInsideEnsures) {
            throw new ContractValidationException(
                    ContractError.OLD_CAN_OCCUR_IN_POSTCONDITIONS_ONLY, invocation,
                    input.getCompilationUnit());
        }
    }
}
