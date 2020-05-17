package pl.coco.compiler.validation.result;

import java.util.List;

import com.sun.source.tree.ExpressionTree;
import com.sun.tools.javac.code.Type;
import com.sun.tools.javac.code.Type.ClassType;
import com.sun.tools.javac.code.Types;
import com.sun.tools.javac.tree.JCTree.JCExpression;
import com.sun.tools.javac.tree.JCTree.JCMethodInvocation;
import com.sun.tools.javac.tree.TreeScanner;

import pl.coco.compiler.instrumentation.ContractMethod;
import pl.coco.compiler.instrumentation.invocation.ContractInvocation;
import pl.coco.compiler.util.ContractAstUtil;
import pl.coco.compiler.validation.ContractError;
import pl.coco.compiler.validation.ContractValidationException;
import pl.coco.compiler.validation.MethodValidationInput;

public class ResultTypeValidator extends TreeScanner {

    private final MethodValidationInput input;
    private final Types types;

    public ResultTypeValidator(MethodValidationInput input, Types types) {
        this.input = input;
        this.types = types;
    }

    @Override
    public void visitApply(JCMethodInvocation invocation) {
        if (ContractAstUtil.isContractInvocation(invocation)) {
            ContractInvocation contract = ContractAstUtil.getContractInvocation(invocation);
            if (contract.getContractMethod() == ContractMethod.RESULT) {

                List<? extends ExpressionTree> arguments = contract.getArguments();
                JCExpression resultType = (JCExpression) arguments.get(0);
                if (doesResultTypeMatchMethodType(resultType)) {
                    throw new ContractValidationException(
                            ContractError.RESULT_TYPE_MUST_MATCH_METHOD_TYPE, resultType,
                            input.getCompilationUnit());
                }
            }
        }
        super.visitApply(invocation);
    }

    private boolean doesResultTypeMatchMethodType(JCExpression typeMarker) {
        ClassType classType = (ClassType) typeMarker.type;
        Type resultType = classType.getTypeArguments().get(0);
        Type methodType = input.getMethod().getReturnType().type;
        Type boxedMethodType = types.boxedTypeOrType(methodType);
        return !typesEqual(resultType, boxedMethodType);
    }

    private boolean typesEqual(Type type1, Type type2) {
        return type1.tsym.flatName().contentEquals(type2.tsym.flatName());
    }

}
