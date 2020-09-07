package pl.coas.compiler.validation;

import java.util.Optional;

import javax.inject.Inject;
import javax.inject.Singleton;

import com.sun.source.tree.CompilationUnitTree;
import com.sun.source.tree.ExpressionStatementTree;
import com.sun.source.tree.ExpressionTree;
import com.sun.source.tree.MethodInvocationTree;
import com.sun.source.tree.StatementTree;
import com.sun.tools.javac.code.Type;
import com.sun.tools.javac.tree.JCTree.JCMethodDecl;
import com.sun.tools.javac.util.List;

import pl.compiler.commons.model.SimpleMethodInvocation;
import pl.compiler.commons.util.AstUtil;
import pl.compiler.commons.util.CollectionUtils;
import pl.compiler.commons.util.TreePasser;

@Singleton
public class AspectValidator {

    public static final String JOINPOINT_CLASS = "pl.coas.api.JoinPoint";
    public static final String OBJECT_CLASS = "java.lang.Object";
    private static final String ASPECT_CLASS_NAME = "pl.coas.api.Aspect";
    private final ErrorProducer errorProducer;

    @Inject
    public AspectValidator(ErrorProducer errorProducer) {
        this.errorProducer = errorProducer;
    }

    public boolean isClassValid(ClassValidationInput input) {
        return true;
    }

    public boolean isMethodValid(MethodValidationInput input) {
        if (doesContainAspects(input)) {
            return areAspectsValid(input);
        }
        return true;
    }

    private boolean areAspectsValid(MethodValidationInput input) {
        try {
            checkIfAllAspectsAreInOneBlockAtTheBeginningOfMethod(input);
            checkIfAspectMethodHasCorrectSignature(input);
            return true;
        } catch (AspectValidationException e) {
            errorProducer.produceErrorMessage(e);
            return false;
        }
    }

    private void checkIfAllAspectsAreInOneBlockAtTheBeginningOfMethod(MethodValidationInput input) {
        Optional<Integer> firstContractIdx =
                CollectionUtils.getIndexOfFirstElementMatchingPredicate(input.getStatements(),
                        this::isAspectStatement);
        Optional<Integer> lastContractIdx =
                CollectionUtils.getIndexOfLastElementMatchingPredicate(input.getStatements(),
                        this::isAspectStatement);

        if (firstContractIdx.isPresent() && lastContractIdx.isPresent()) {
            checkIfFirstAspectIsAtTheBeginningOfAMethod(input, firstContractIdx.get());
            checkIfAspectBlockContainsAspectsOnly(input, firstContractIdx.get(),
                    lastContractIdx.get());
        }
    }

    private boolean isAspectStatement(StatementTree statement) {
        Optional<MethodInvocationTree> invocation = TreePasser.of(statement)
                .as(ExpressionStatementTree.class)
                .map(ExpressionStatementTree::getExpression)
                .flatMapAndGet(this::getMethodInvocationFromExpr);

        return invocation.flatMap(SimpleMethodInvocation::of)
                .map(this::isAspectInvocation)
                .orElse(false);
    }

    private Optional<MethodInvocationTree> getMethodInvocationFromExpr(
            ExpressionTree expression) {

        return TreePasser.of(expression)
                .as(MethodInvocationTree.class)
                .get();
    }

    private boolean isAspectInvocation(SimpleMethodInvocation invocation) {
        return invocation.getFullyQualifiedClassName().contentEquals(ASPECT_CLASS_NAME);
    }

    private void checkIfFirstAspectIsAtTheBeginningOfAMethod(MethodValidationInput input,
            int firstAspectIdx) {

        boolean isAspectFirstStatement = firstAspectIdx == 0;
        boolean isContractSecondStatementInConstructor =
                AstUtil.isConstructor(input.getMethod()) && firstAspectIdx == 1;

        if (!isAspectFirstStatement && !isContractSecondStatementInConstructor) {
            StatementTree offending = input.getStatements().get(firstAspectIdx);
            CompilationUnitTree compilationUnit = input.getCompilationUnit();
            throw new AspectValidationException(
                    AspectError.ASPECT_CAN_OCCUR_IN_BLOCK_AT_THE_BEGINNING_OF_THE_METHOD,
                    offending, compilationUnit);
        }
    }

    private void checkIfAspectBlockContainsAspectsOnly(MethodValidationInput input,
            int firstAspectIdx, int lastAspectIdx) {

        for (int i = firstAspectIdx; i < lastAspectIdx; i++) {
            StatementTree statement = input.getStatements().get(i);
            CompilationUnitTree compilationUnit = input.getCompilationUnit();
            if (!isAspectStatement(statement)) {
                throw new AspectValidationException(
                        AspectError.ASPECT_BLOCK_CAN_CONTAIN_ASPECTS_ONLY,
                        statement, compilationUnit);
            }
        }
    }

    private void checkIfAspectMethodHasCorrectSignature(MethodValidationInput input) {
        if (doesContainAspects(input)) {
            JCMethodDecl method = input.getMethod();
            Type returnType = method.type.getReturnType();
            if (!returnType.tsym.getQualifiedName().toString().equals(OBJECT_CLASS)) {
                throw new AspectValidationException(
                        AspectError.INVALID_ADVICE_SINGATURE, method, input.getCompilationUnit());
            }

            List<Type> paramTypes = method.type.getParameterTypes();
            if (paramTypes.size() != 1 ||
                    !paramTypes.get(0).tsym.getQualifiedName().toString().equals(JOINPOINT_CLASS)) {
                throw new AspectValidationException(
                        AspectError.INVALID_ADVICE_SINGATURE, method, input.getCompilationUnit());
            }
        }
    }

    private boolean doesContainAspects(MethodValidationInput input) {
        return CollectionUtils.getIndexOfFirstElementMatchingPredicate(
                input.getStatements(), this::isAspectStatement)
                .isPresent();
    }
}
