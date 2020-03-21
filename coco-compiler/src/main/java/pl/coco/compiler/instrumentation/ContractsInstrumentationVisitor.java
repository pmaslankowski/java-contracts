package pl.coco.compiler.instrumentation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import com.sun.source.tree.ExpressionStatementTree;
import com.sun.source.tree.ExpressionTree;
import com.sun.source.tree.MethodInvocationTree;
import com.sun.source.tree.MethodTree;
import com.sun.source.tree.StatementTree;
import com.sun.source.util.JavacTask;
import com.sun.source.util.TreeScanner;
import com.sun.tools.javac.api.BasicJavacTask;
import com.sun.tools.javac.code.Type;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.tree.TreeMaker;
import com.sun.tools.javac.util.Context;
import com.sun.tools.javac.util.Names;

import pl.coco.compiler.arguments.CocoArgs;
import pl.coco.compiler.util.TreePasser;

public class ContractsInstrumentationVisitor extends TreeScanner<Void, Void> {

    private final JavacTask task;
    private final CocoArgs cocoArgs;

    public ContractsInstrumentationVisitor(JavacTask task,
            CocoArgs cocoArgs) {
        this.task = task;
        this.cocoArgs = cocoArgs;
    }

    @Override
    public Void visitMethod(MethodTree method, Void context) {
        List<? extends StatementTree> statements = method.getBody().getStatements();
        List<JCTree.JCStatement> modifiedStatements = new ArrayList<>();
        for (StatementTree statement : statements) {
            Optional<ContractMethodInvocation> contractInvocation = getMethodInvocation(statement)
                    .flatMap(SimpleMethodInvocation::of)
                    .flatMap(ContractMethodInvocation::of);

            if (contractInvocation.isPresent()) {
                if (cocoArgs.isContractCheckingEnabled()) {
                    if (contractInvocation.get().getMethodName().contentEquals("requires")) {
                        JCTree.JCStatement processedStatement = buildRequiresCall(
                                contractInvocation.get());
                        modifiedStatements.add(processedStatement);
                    }

                }
            } else {
                modifiedStatements.add((JCTree.JCStatement) statement);
            }
        }

        JCTree.JCBlock body = (JCTree.JCBlock) method.getBody();
        body.stats = com.sun.tools.javac.util.List.from(modifiedStatements);
        return super.visitMethod(method, context);
    }

    private Optional<MethodInvocationTree> getMethodInvocation(StatementTree statement) {
        return TreePasser.of(statement)
                .as(ExpressionStatementTree.class)
                .map(ExpressionStatementTree::getExpression)
                .as(MethodInvocationTree.class)
                .get();
    }

    private JCTree.JCStatement buildRequiresCall(ContractMethodInvocation methodInvocation) {
        Context context = ((BasicJavacTask) task).getContext();
        TreeMaker factory = TreeMaker.instance(context);
        Names names = Names.instance(context);

        com.sun.tools.javac.util.List<JCTree.JCExpression> typeParameters =
                com.sun.tools.javac.util.List.nil();

        int pos = methodInvocation.getPosition();
        JCTree.JCFieldAccess method = factory.at(pos).Select(
                factory.at(pos).Select(
                        factory.at(pos).Select(
                                factory.at(pos).Select(
                                        factory.Ident(names.fromString("pl")),
                                        names.fromString("coco")),
                                names.fromString("internal")),
                        names.fromString("ContractEngine")),
                names.fromString("requires"));

        List<? extends ExpressionTree> originalArguments = methodInvocation.getArguments();
        JCTree.JCExpression precondition = (JCTree.JCExpression) originalArguments.get(0);
        JCTree.JCLiteral preconditionAsStringLiteral = factory.Literal(precondition.toString());
        List<JCTree.JCExpression> targetArguments = Arrays.asList(
                precondition, preconditionAsStringLiteral);

        com.sun.tools.javac.util.List<JCTree.JCExpression> invocationArgs =
                com.sun.tools.javac.util.List.from(targetArguments);

        JCTree.JCMethodInvocation processedInvocation = factory.Apply(
                typeParameters, method, invocationArgs);
        processedInvocation.setType(new Type.JCVoidType());
        return factory.at(methodInvocation.getPosition())
                .Call(processedInvocation);
    }
}
