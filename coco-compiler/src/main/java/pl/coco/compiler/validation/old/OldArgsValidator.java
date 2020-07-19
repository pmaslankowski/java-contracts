package pl.coco.compiler.validation.old;

import com.sun.source.tree.ExpressionTree;
import com.sun.source.tree.MethodInvocationTree;
import com.sun.tools.javac.tree.JCTree.JCIdent;
import com.sun.tools.javac.tree.JCTree.JCMethodDecl;
import com.sun.tools.javac.tree.JCTree.JCMethodInvocation;
import com.sun.tools.javac.tree.JCTree.JCVariableDecl;
import com.sun.tools.javac.tree.TreeScanner;
import com.sun.tools.javac.util.List;
import com.sun.tools.javac.util.Name;

import pl.coco.compiler.instrumentation.ContractMethod;
import pl.coco.compiler.model.ContractInvocation;
import pl.coco.compiler.util.ContractAstUtil;
import pl.coco.compiler.validation.ContractError;
import pl.coco.compiler.validation.ContractValidationException;
import pl.coco.compiler.validation.MethodValidationInput;

public class OldArgsValidator extends TreeScanner {

    private final MethodValidationInput input;
    private JCMethodDecl currentMethod;

    public OldArgsValidator(MethodValidationInput input) {
        this.input = input;
    }

    @Override
    public void visitMethodDef(JCMethodDecl method) {
        this.currentMethod = method;
        super.visitMethodDef(method);
    }

    @Override
    public void visitApply(JCMethodInvocation methodInvocation) {
        if (ContractAstUtil.isContractInvocation(methodInvocation)) {
            ContractInvocation invocation = ContractAstUtil.getContractInvocation(methodInvocation);
            if (invocation.getContractMethod() == ContractMethod.OLD) {
                ExpressionTree arg = invocation.getArguments().get(0);
                if (arg instanceof JCIdent) {
                    Name argumentName = ((JCIdent) arg).name;
                    if (!doesCurrentMethodContainArg(argumentName)) {
                        throwContractError(methodInvocation);
                    }
                } else {
                    throwContractError(methodInvocation);
                }
            }
        }
        super.visitApply(methodInvocation);
    }

    private boolean doesCurrentMethodContainArg(Name name) {
        List<JCVariableDecl> parameters = currentMethod.getParameters();
        return parameters.stream().anyMatch(param -> param.name.contentEquals(name));
    }

    private void throwContractError(MethodInvocationTree invocation) {
        throw new ContractValidationException(ContractError.OLD_CAN_BE_USED_ON_THE_METHOD_ARGS_ONLY,
                invocation, input.getCompilationUnit());
    }
}
