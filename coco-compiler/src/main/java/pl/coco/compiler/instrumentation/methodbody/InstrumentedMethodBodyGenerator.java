package pl.coco.compiler.instrumentation.methodbody;

import static com.sun.tools.javac.tree.JCTree.JCStatement;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import javax.inject.Inject;
import javax.inject.Singleton;

import com.sun.tools.javac.code.Symbol.VarSymbol;
import com.sun.tools.javac.tree.JCTree.JCClassDecl;
import com.sun.tools.javac.tree.JCTree.JCExpression;
import com.sun.tools.javac.tree.JCTree.JCMethodDecl;
import com.sun.tools.javac.tree.JCTree.JCReturn;
import com.sun.tools.javac.tree.TreeMaker;

import pl.coco.compiler.instrumentation.synthetic.ContractSyntheticMethods;
import pl.coco.compiler.util.AstUtil;
import pl.coco.compiler.util.ContractAstUtil;
import pl.coco.compiler.util.InvariantUtil;

@Singleton
public class InstrumentedMethodBodyGenerator {

    private final TreeMaker treeMaker;
    private final InvocationStatementGenerator invocationStmtGenerator;

    @Inject
    public InstrumentedMethodBodyGenerator(TreeMaker treeMaker,
            InvocationStatementGenerator invocationStmtGenerator) {

        this.treeMaker = treeMaker;
        this.invocationStmtGenerator = invocationStmtGenerator;
    }

    public List<JCStatement> generateBody(MethodBodyGeneratorInput input) {
        List<JCStatement> processed = new ArrayList<>();

        JCMethodDecl originalMethod = input.getOriginalMethod();

        if (AstUtil.isConstructor(originalMethod)) {
            addSuperCallFromOriginalMethod(originalMethod, processed);
        }

        if (!AstUtil.isConstructor(originalMethod) && !AstUtil.isStatic(originalMethod)) {
            addInvariantInvocationIfNeeded(input.getClazz(), processed, InvariantPoint.BEFORE);
        }

        addPreconditionInvocation(input, processed);
        addTargetInvocation(input, processed);
        addPostconditionInvocation(input, processed);

        if (!AstUtil.isStatic(originalMethod)) {
            addInvariantInvocationIfNeeded(input.getClazz(), processed, InvariantPoint.AFTER);
        }

        if (shouldReturnResult(originalMethod)) {
            addReturnStatement(input, processed);
        }

        return processed;
    }

    private void addSuperCallFromOriginalMethod(JCMethodDecl originalMethod,
            List<JCStatement> processed) {
        JCStatement superStmt = originalMethod.getBody().getStatements().get(0);
        processed.add(superStmt);
    }

    private void addInvariantInvocationIfNeeded(JCClassDecl clazz, List<JCStatement> processed,
            InvariantPoint invocationPoint) {

        List<JCExpression> params = Collections.singletonList(
                treeMaker.Literal(invocationPoint.isBefore()));
        Optional<JCStatement> invariantInvocation = InvariantUtil.getInvariantMethod(clazz)
                .map(inv -> invocationStmtGenerator.generateWithParameters(inv, params));

        invariantInvocation.ifPresent(processed::add);
    }

    private void addPreconditionInvocation(MethodBodyGeneratorInput input,
            List<JCStatement> processed) {

        ContractSyntheticMethods syntheticMethods = input.getSyntheticMethods();
        JCMethodDecl preconditions = syntheticMethods.getPreconditions();
        JCStatement preconditionsInvocationStmt = invocationStmtGenerator.generate(preconditions);
        processed.add(preconditionsInvocationStmt);
    }

    private void addTargetInvocation(MethodBodyGeneratorInput input, List<JCStatement> processed) {
        ContractSyntheticMethods syntheticMethods = input.getSyntheticMethods();
        JCMethodDecl target = syntheticMethods.getTarget();
        VarSymbol result = input.getResultSymbol();
        JCStatement targetInvocationStmt = invocationStmtGenerator.generateTarget(target, result);
        processed.add(targetInvocationStmt);
    }

    private void addPostconditionInvocation(MethodBodyGeneratorInput input,
            List<JCStatement> processed) {
        ContractSyntheticMethods syntheticMethods = input.getSyntheticMethods();
        JCMethodDecl postconditions = syntheticMethods.getPostconditions();
        JCStatement postconditionsInvocationStmt = invocationStmtGenerator.generate(postconditions);
        processed.add(postconditionsInvocationStmt);
    }

    private boolean shouldReturnResult(JCMethodDecl originalMethod) {
        return !AstUtil.isConstructor(originalMethod) && !AstUtil.isVoid(originalMethod);
    }

    private void addReturnStatement(MethodBodyGeneratorInput input, List<JCStatement> processed) {
        VarSymbol result = input.getResultSymbol();
        JCReturn returnStatement = treeMaker.Return(treeMaker.Ident(result));
        processed.add(returnStatement);
    }
}
