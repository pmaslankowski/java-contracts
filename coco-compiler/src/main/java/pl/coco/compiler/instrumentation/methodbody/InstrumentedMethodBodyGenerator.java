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

import pl.coco.compiler.instrumentation.invocation.internal.old.OldValue;
import pl.coco.compiler.instrumentation.synthetic.ContractSyntheticMethods;
import pl.coco.compiler.util.InvariantUtil;
import pl.compiler.commons.util.AstUtil;
import pl.compiler.commons.util.PrimitiveBoxer;

@Singleton
public class InstrumentedMethodBodyGenerator {

    private final TreeMaker treeMaker;
    private final InvocationStatementGenerator invocationStmtGenerator;
    private final PrimitiveBoxer boxer;
    private final OldValueCloningInstrumenter oldValueCloningInstrumenter;

    @Inject
    public InstrumentedMethodBodyGenerator(TreeMaker treeMaker,
            InvocationStatementGenerator invocationStmtGenerator,
            PrimitiveBoxer boxer, OldValueCloningInstrumenter oldValueCloningInstrumenter) {

        this.treeMaker = treeMaker;
        this.invocationStmtGenerator = invocationStmtGenerator;
        this.boxer = boxer;
        this.oldValueCloningInstrumenter = oldValueCloningInstrumenter;
    }

    public List<JCStatement> generateBody(MethodBodyGeneratorInput input) {

        List<JCStatement> processed = new ArrayList<>();

        JCMethodDecl originalMethod = input.getOriginalMethod();
        List<OldValue> oldValues = input.getOldValues();

        if (AstUtil.isConstructor(originalMethod)) {
            addSuperCallFromOriginalMethod(originalMethod, processed);
        }

        addOldValuesCloningStmts(oldValues, processed, originalMethod);

        if (!AstUtil.isConstructor(originalMethod) && !AstUtil.isStatic(originalMethod)) {
            addInvariantInvocationIfNeeded(input.getClazz(), processed, InvariantPoint.BEFORE);
        }

        addPreconditionInvocation(input, processed);
        addTargetInvocation(input, processed);
        addPostconditionInvocation(input, processed, oldValues);
        addSelfPostconditionInvocation(input, processed, oldValues);

        if (!AstUtil.isStatic(originalMethod)) {
            addInvariantInvocationIfNeeded(input.getClazz(), processed, InvariantPoint.AFTER);
        }

        if (shouldReturnResult(originalMethod)) {
            addReturnStatement(input, processed);
        }

        return processed;
    }

    private void addOldValuesCloningStmts(List<OldValue> oldValues, List<JCStatement> processed,
            JCMethodDecl originalMethod) {

        List<JCStatement> cloningStmts = oldValueCloningInstrumenter
                .getOldValuesCloningStatements(originalMethod, oldValues);
        processed.addAll(cloningStmts);
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
            List<JCStatement> processed, List<OldValue> oldValues) {
        ContractSyntheticMethods syntheticMethods = input.getSyntheticMethods();
        JCMethodDecl postconditions = syntheticMethods.getPostconditions();
        List<JCExpression> parameters = getParameters(input, oldValues);
        JCStatement postconditionsInvocationStmt =
                invocationStmtGenerator.generateWithParameters(postconditions, parameters);
        processed.add(postconditionsInvocationStmt);
    }

    private void addSelfPostconditionInvocation(MethodBodyGeneratorInput input,
            List<JCStatement> processed, List<OldValue> oldValues) {

        ContractSyntheticMethods syntheticMethods = input.getSyntheticMethods();
        JCMethodDecl selfPostconditions = syntheticMethods.getSelfPostconditions();
        List<JCExpression> parameters = getParameters(input, oldValues);
        JCStatement selfPostconditionsInvocationStmt =
                invocationStmtGenerator.generateWithParameters(selfPostconditions, parameters);
        processed.add(selfPostconditionsInvocationStmt);
    }

    private List<JCExpression> getParameters(MethodBodyGeneratorInput input,
            List<OldValue> oldValues) {

        JCMethodDecl originalMethod = input.getOriginalMethod();
        List<JCExpression> parameters = originalMethod.getParameters().stream()
                .map(this::toIdentifier)
                .collect(Collectors.toCollection(ArrayList::new));

        oldValues.forEach(
                oldValue -> parameters.add(toIdentifier(oldValue.getClonedOriginalMethodVar())));

        if (!AstUtil.isVoid(originalMethod)) {
            JCExpression result = treeMaker.Ident(input.getResultSymbol());
            parameters.add(boxer.boxIfNeeded(result, originalMethod));
        }

        return parameters;
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
