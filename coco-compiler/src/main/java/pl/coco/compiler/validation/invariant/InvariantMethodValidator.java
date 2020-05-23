package pl.coco.compiler.validation.invariant;

import com.sun.tools.javac.tree.JCTree.JCMethodDecl;
import com.sun.tools.javac.tree.JCTree.JCStatement;
import com.sun.tools.javac.tree.TreeScanner;

import pl.coco.compiler.instrumentation.ContractMethod;
import pl.coco.compiler.util.AstUtil;
import pl.coco.compiler.util.ContractAstUtil;
import pl.coco.compiler.util.InvariantUtil;
import pl.coco.compiler.validation.ClassValidationInput;
import pl.coco.compiler.validation.ContractError;
import pl.coco.compiler.validation.ContractValidationException;

public class InvariantMethodValidator extends TreeScanner {

    private final ClassValidationInput input;

    private boolean invariantAlreadyMethodOccurred = false;

    public InvariantMethodValidator(ClassValidationInput input) {
        this.input = input;
    }

    @Override
    public void visitMethodDef(JCMethodDecl method) {
        if (InvariantUtil.isInvariantMethod(method)) {
            checkInvariantMethodSignature(method);
            checkIfInvariantMethodIsUnique(method);
            checkIfInvariantMethodContainsInvariantsOnly(method);
            invariantAlreadyMethodOccurred = true;
        }

        super.visitMethodDef(method);
    }

    private void checkInvariantMethodSignature(JCMethodDecl method) {
        if (AstUtil.isConstructor(method) || !AstUtil.isVoid(method)
                || !method.type.getParameterTypes().isEmpty()) {

            throw new ContractValidationException(ContractError.BAD_INVARIANT_METHOD_SIGNATURE,
                    method, input.getCompilationUnit());
        }
    }

    private void checkIfInvariantMethodIsUnique(JCMethodDecl method) {
        if (invariantAlreadyMethodOccurred) {
            throw new ContractValidationException(
                    ContractError.MULTIPLE_INVARIANT_METHODS_IN_THE_SAME_CLASS, method,
                    input.getCompilationUnit());
        }
    }

    private void checkIfInvariantMethodContainsInvariantsOnly(JCMethodDecl method) {
        for (JCStatement statement : method.getBody().getStatements()) {
            if (!ContractAstUtil.isContractInvocation(statement)) {
                throwInvariantHasToContainInvariantMethodsOnlyException(statement);
            }

            ContractMethod contract = ContractAstUtil.getContractInvocation(statement)
                    .getContractMethod();
            if (contract != ContractMethod.INVARIANT) {
                throwInvariantHasToContainInvariantMethodsOnlyException(statement);
            }
        }
    }

    private void throwInvariantHasToContainInvariantMethodsOnlyException(JCStatement statement) {
        throw new ContractValidationException(
                ContractError.INVARIANT_METHOD_MUST_CONTAIN_INVARIANTS_ONLY, statement,
                input.getCompilationUnit());
    }
}
