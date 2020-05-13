package pl.coco.compiler.validation;

import com.sun.tools.javac.tree.JCTree.JCMethodDecl;
import com.sun.tools.javac.tree.JCTree.JCMethodInvocation;
import com.sun.tools.javac.tree.TreeScanner;

import pl.coco.compiler.instrumentation.ContractMethod;
import pl.coco.compiler.instrumentation.invocation.ContractInvocation;
import pl.coco.compiler.util.AstUtil;
import pl.coco.compiler.util.ContractAstUtil;

public class ContractResultValidator extends TreeScanner {

    private final ErrorProducer errorProducer;

    private boolean isInsideEnsuresCall = false;
    private boolean isNonVoidMethod;

    public ContractResultValidator(ErrorProducer errorProducer, JCMethodDecl method) {
        this.errorProducer = errorProducer;
        this.isNonVoidMethod = !AstUtil.isVoid(method);
    }

    @Override
    public void visitApply(JCMethodInvocation invocation) {
        if (ContractAstUtil.isContractInvocation(invocation)) {
            ContractInvocation contract = ContractAstUtil.getContractInvocation(invocation);
            if (contract.getContractMethod() == ContractMethod.RESULT) {
                handleResultCall(invocation);
            } else if (contract.getContractMethod() == ContractMethod.ENSURES) {
                handleEnsuresCall(invocation);
            } else {
                super.visitApply(invocation);
            }
        }
    }

    private void handleResultCall(JCMethodInvocation invocation) {
        if (!isResultCallAllowed()) {
            errorProducer.raiseError(
                    ContractError.RESULT_CAN_BE_PLACED_INSIDE_ENSURES_IN_NON_VOID_METHODS_ONLY,
                    invocation);
        }
        super.visitApply(invocation);
    }

    private void handleEnsuresCall(JCMethodInvocation invocation) {
        isInsideEnsuresCall = true;
        super.visitApply(invocation);
        isInsideEnsuresCall = false;
    }

    private boolean isResultCallAllowed() {
        return isNonVoidMethod && isInsideEnsuresCall;
    }
}
