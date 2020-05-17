package pl.coco.compiler.validation.invariant;

import javax.inject.Inject;

import com.sun.tools.javac.tree.JCTree.JCMethodDecl;
import com.sun.tools.javac.tree.JCTree.JCMethodInvocation;
import com.sun.tools.javac.tree.TreeScanner;

import pl.coco.compiler.instrumentation.ContractMethod;
import pl.coco.compiler.instrumentation.invocation.ContractInvocation;
import pl.coco.compiler.util.ContractAstUtil;
import pl.coco.compiler.validation.ContractError;
import pl.coco.compiler.validation.ContractValidationException;
import pl.coco.compiler.validation.ErrorProducer;
import pl.coco.compiler.validation.MethodValidationInput;

public class InvariantCallValidator extends TreeScanner {

    private final ErrorProducer errorProducer;
    private final MethodValidationInput input;

    @Inject
    public InvariantCallValidator(ErrorProducer errorProducer, MethodValidationInput input) {
        this.errorProducer = errorProducer;
        this.input = input;
    }

    @Override
    public void visitApply(JCMethodInvocation invocation) {
        if (ContractAstUtil.isContractInvocation(invocation)) {
            ContractInvocation contract = ContractAstUtil.getContractInvocation(invocation);
            ContractMethod contractMethod = contract.getContractMethod();
            if (contractMethod == ContractMethod.INVARIANT) {
                JCMethodDecl enclosingMethod = input.getMethod();
                if (!ContractAstUtil.isInvariantMethod(enclosingMethod)) {
                    errorProducer.raiseError(
                            ContractError.INVARIANT_CAN_OCCUR_IN_INVARIANT_METHODS_ONLY, invocation,
                            input.getCompilationUnit());
                    throw new ContractValidationException(
                            ContractError.INVARIANT_CAN_OCCUR_IN_INVARIANT_METHODS_ONLY);
                }
            }
        }
        super.visitApply(invocation);
    }
}