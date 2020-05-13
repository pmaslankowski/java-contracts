package pl.coco.compiler.validation;

import java.util.List;

import com.sun.source.tree.ExpressionTree;
import com.sun.tools.javac.code.Type;
import com.sun.tools.javac.tree.JCTree.JCExpression;
import com.sun.tools.javac.tree.JCTree.JCMethodDecl;
import com.sun.tools.javac.tree.JCTree.JCMethodInvocation;
import com.sun.tools.javac.tree.TreeScanner;

import pl.coco.compiler.instrumentation.ContractMethod;
import pl.coco.compiler.instrumentation.invocation.ContractInvocation;
import pl.coco.compiler.util.ContractAstUtil;

public class ResultTypeValidator extends TreeScanner {

    private final ErrorProducer errorProducer;
    private final JCMethodDecl containingMethod;

    public ResultTypeValidator(ErrorProducer errorProducer, JCMethodDecl containingMethod) {
        this.errorProducer = errorProducer;
        this.containingMethod = containingMethod;
    }

    @Override
    public void visitApply(JCMethodInvocation invocation) {
        if (ContractAstUtil.isContractInvocation(invocation)) {
            ContractInvocation contract = ContractAstUtil.getContractInvocation(invocation);
            if (contract.getContractMethod() == ContractMethod.RESULT) {

                List<? extends ExpressionTree> arguments = contract.getArguments();
                JCExpression resultType = (JCExpression) arguments.get(0);
                if (doesResultTypeMatchMethodType(resultType)) {

                    errorProducer.raiseError(ContractError.RESULT_TYPE_MUST_MATCH_METHOD_TYPE,
                            resultType);
                }
            }
        }
        super.visitApply(invocation);
    }

    private boolean doesResultTypeMatchMethodType(JCExpression typeMarker) {
        Type.ClassType classType = (Type.ClassType) typeMarker.type;
        Type actualType = classType.getTypeArguments().get(0);
        return !actualType.equals(containingMethod.getReturnType().type);
    }
}
