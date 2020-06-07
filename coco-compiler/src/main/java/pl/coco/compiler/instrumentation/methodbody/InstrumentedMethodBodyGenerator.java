package pl.coco.compiler.instrumentation.methodbody;

import static com.sun.tools.javac.tree.JCTree.JCStatement;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.inject.Inject;
import javax.inject.Singleton;

import com.sun.source.tree.VariableTree;
import com.sun.tools.javac.code.Symbol.VarSymbol;
import com.sun.tools.javac.tree.JCTree.JCClassDecl;
import com.sun.tools.javac.tree.JCTree.JCExpression;
import com.sun.tools.javac.tree.JCTree.JCIdent;
import com.sun.tools.javac.tree.JCTree.JCMethodDecl;
import com.sun.tools.javac.tree.JCTree.JCReturn;
import com.sun.tools.javac.tree.JCTree.JCVariableDecl;
import com.sun.tools.javac.tree.TreeMaker;

import pl.coco.compiler.instrumentation.synthetic.ContractSyntheticMethods;
import pl.coco.compiler.util.InvariantUtil;
import pl.compiler.commons.util.AstUtil;
import pl.compiler.commons.util.PrimitiveBoxer;

@Singleton
public class InstrumentedMethodBodyGenerator {

    private final TreeMaker treeMaker;
    private final InvocationStatementGenerator invocationStmtGenerator;
    private final PrimitiveBoxer boxer;

    @Inject
    public InstrumentedMethodBodyGenerator(TreeMaker treeMaker,
            InvocationStatementGenerator invocationStmtGenerator,
            PrimitiveBoxer boxer) {

        this.treeMaker = treeMaker;
        this.invocationStmtGenerator = invocationStmtGenerator;
        this.boxer = boxer;
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
        List<JCExpression> parameters = getParameters(input);
        JCStatement postconditionsInvocationStmt =
                invocationStmtGenerator.generateWithParameters(postconditions, parameters);
        processed.add(postconditionsInvocationStmt);
    }

    private List<JCExpression> getParameters(MethodBodyGeneratorInput input) {
        JCMethodDecl originalMethod = input.getOriginalMethod();
        List<JCExpression> originalParameters = originalMethod.getParameters().stream()
                .map(this::toIdentifier)
                .collect(Collectors.toCollection(ArrayList::new));

        if (!AstUtil.isVoid(originalMethod)) {
            JCExpression result = treeMaker.Ident(input.getResultSymbol());
            originalParameters.add(boxer.boxIfNeeded(result, originalMethod));
        }

        return originalParameters;
    }

    private JCIdent toIdentifier(VariableTree param) {
        JCVariableDecl variableDec = (JCVariableDecl) param;
        return treeMaker.Ident(variableDec.sym);
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
