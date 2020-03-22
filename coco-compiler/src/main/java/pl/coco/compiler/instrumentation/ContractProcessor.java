package pl.coco.compiler.instrumentation;

import static com.sun.tools.javac.tree.JCTree.JCBlock;
import static com.sun.tools.javac.tree.JCTree.JCExpression;
import static com.sun.tools.javac.tree.JCTree.JCLiteral;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;

import com.sun.source.tree.BlockTree;
import com.sun.source.tree.ClassTree;
import com.sun.source.tree.ExpressionTree;
import com.sun.source.tree.MethodTree;
import com.sun.source.tree.StatementTree;
import com.sun.source.util.JavacTask;
import com.sun.tools.javac.tree.JCTree.JCStatement;
import com.sun.tools.javac.tree.TreeMaker;
import com.sun.tools.javac.util.List;

import pl.coco.compiler.ContractType;
import pl.coco.compiler.util.ContractAstUtil;
import pl.coco.compiler.util.MethodInvocationBuilder;

public class ContractProcessor {

    private final JavacTask task;
    private final TreeMaker treeMaker;
    private final ClassTree clazz;
    private final MethodTree method;

    public ContractProcessor(JavacTask task, TreeMaker treeMaker,
            ClassTree clazz, MethodTree method) {
        this.task = task;
        this.treeMaker = treeMaker;
        this.clazz = clazz;
        this.method = method;
    }

    public void process() {
        ArrayList<JCStatement> processedStatements = new ArrayList<>();
        BlockTree methodBody = method.getBody();
        for (StatementTree statement : methodBody.getStatements()) {
            Optional<ContractMethodInvocation> contractInvocation =
                    ContractAstUtil.getContractInvocation(statement);

            JCStatement processedStatement = contractInvocation
                    .map(this::getProcessedPrecondition)
                    .orElse((JCStatement) statement);

            processedStatements.add(processedStatement);
        }

        ((JCBlock) methodBody).stats = List.from(processedStatements);
    }

    private JCStatement getProcessedPrecondition(
            ContractMethodInvocation contractInvocation) {

        if (contractInvocation.getContractType() == ContractType.REQUIRES) {
            return buildRequiresCall(contractInvocation);
        } else {
            throw new UnsupportedOperationException();
        }
    }

    private JCStatement buildRequiresCall(ContractMethodInvocation methodInvocation) {
        return new MethodInvocationBuilder(task)
                .withClassName(ContractType.REQUIRES.getInternalClassName())
                .withMethodName(ContractType.REQUIRES.getMethodName())
                .withArguments(getArgumentsForContractEngine(methodInvocation.getArguments()))
                .withPosition(methodInvocation.getPos())
                .build();
    }

    private List<JCExpression> getArgumentsForContractEngine(
            java.util.List<? extends ExpressionTree> originalArguments) {

        JCExpression precondition = (JCExpression) originalArguments.get(0);
        JCLiteral preconditionAsStringLiteral = treeMaker.Literal(precondition.toString());
        return List.from(Arrays.asList(precondition, preconditionAsStringLiteral));
    }
}
