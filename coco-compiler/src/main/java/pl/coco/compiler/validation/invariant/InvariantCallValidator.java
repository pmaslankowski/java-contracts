package pl.coco.compiler.validation.invariant;

import javax.inject.Inject;

import com.sun.tools.javac.tree.JCTree.JCMethodDecl;
import com.sun.tools.javac.tree.JCTree.JCMethodInvocation;
import com.sun.tools.javac.tree.TreeScanner;

import pl.coco.compiler.instrumentation.ContractMethod;
import pl.coco.compiler.model.ContractInvocation;
import pl.coco.compiler.util.ContractAstUtil;
import pl.coco.compiler.util.InvariantUtil;
import pl.coco.compiler.validation.ContractError;
import pl.coco.compiler.validation.ContractValidationException;
import pl.coco.compiler.validation.MethodValidationInput;

public class InvariantCallValidator extends TreeScanner {

    private final MethodValidationInput input;

    @Inject
    public InvariantCallValidator(MethodValidationInput input) {
        this.input = input;
    }

    @Override
    public void visitApply(JCMethodInvocation invocation) {
        if (ContractAstUtil.isContractInvocation(invocation)) {
            ContractInvocation contract = ContractAstUtil.getContractInvocation(invocation);
            ContractMethod contractMethod = contract.getContractMethod();
            if (contractMethod == ContractMethod.INVARIANT) {
                JCMethodDecl enclosingMethod = input.getMethod();
                if (!InvariantUtil.isInvariantMethod(enclosingMethod)) {
                    throw new ContractValidationException(
                            ContractError.INVARIANT_CAN_OCCUR_IN_INVARIANT_METHODS_ONLY, invocation,
                            input.getCompilationUnit());
                }
            }
        }
        super.visitApply(invocation);
    }
}
