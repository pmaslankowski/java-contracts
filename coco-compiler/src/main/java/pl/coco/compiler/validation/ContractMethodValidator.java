package pl.coco.compiler.validation;

import java.util.Optional;

import javax.inject.Inject;

import com.sun.source.tree.CompilationUnitTree;
import com.sun.source.tree.StatementTree;
import com.sun.tools.javac.tree.JCTree.JCMethodDecl;

import pl.coco.compiler.instrumentation.ContractMethod;
import pl.coco.compiler.instrumentation.invocation.ContractInvocation;
import pl.coco.compiler.util.AstUtil;
import pl.coco.compiler.util.CollectionUtils;
import pl.coco.compiler.util.ContractAstUtil;
import pl.coco.compiler.validation.forallexists.ForAllExistsValidator;
import pl.coco.compiler.validation.forallexists.ForAllExistsValidatorFactory;
import pl.coco.compiler.validation.invariant.InvariantCallValidator;
import pl.coco.compiler.validation.invariant.InvariantCallValidatorFactory;
import pl.coco.compiler.validation.result.ContractResultValidator;
import pl.coco.compiler.validation.result.ContractResultValidatorFactory;
import pl.coco.compiler.validation.result.ResultTypeValidator;
import pl.coco.compiler.validation.result.ResultTypeValidatorFactory;

public class ContractMethodValidator {

    private final ErrorProducer errorProducer;
    private final ContractResultValidatorFactory resultValidatorFactory;
    private final ResultTypeValidatorFactory resultTypeValidatorFactory;
    private final ForAllExistsValidatorFactory forAllExistsValidatorFactory;
    private final InvariantCallValidatorFactory invariantCallValidatorFactory;

    @Inject
    public ContractMethodValidator(ErrorProducer errorProducer,
            ContractResultValidatorFactory resultValidatorFactory,
            ResultTypeValidatorFactory resultTypeValidatorFactory,
            ForAllExistsValidatorFactory forAllExistsValidatorFactory,
            InvariantCallValidatorFactory invariantCallValidatorFactory) {
        this.errorProducer = errorProducer;
        this.resultValidatorFactory = resultValidatorFactory;
        this.resultTypeValidatorFactory = resultTypeValidatorFactory;
        this.forAllExistsValidatorFactory = forAllExistsValidatorFactory;
        this.invariantCallValidatorFactory = invariantCallValidatorFactory;
    }

    public boolean isValid(MethodValidationInput input) {
        if (doesContainContracts(input.getMethod())) {
            return areContractsValid(input);
        }
        return true;
    }

    private boolean areContractsValid(MethodValidationInput input) {
        try {
            checkIfAllContractsAreInOneBlockAtTheBeginningOfMethod(input);
            checkIfResultOccursInsideEnsuresInNonVoidMethodsOnly(input);
            checkIfResultTypeMatchesMethodType(input);
            checkIfForAllAndExistsOccursInsideContractSpecificationsOnly(input);
            checkIfInvariantsOccursInsideInvariantMethodsOnly(input);
            return true;
        } catch (ContractValidationException e) {
            return false;
        }
    }

    private boolean doesContainContracts(JCMethodDecl method) {
        return method.getBody().getStatements().stream()
                .anyMatch(ContractAstUtil::isContractInvocation);
    }

    private void checkIfAllContractsAreInOneBlockAtTheBeginningOfMethod(
            MethodValidationInput input) {

        Optional<Integer> firstContractIdx =
                CollectionUtils.getIndexOfFirstElementMatchingPredicate(input.getStatements(),
                        this::isContractThatMustBeAtTheMethodBeginning);
        Optional<Integer> lastContractIdx =
                CollectionUtils.getIndexOfLastElementMatchingPredicate(input.getStatements(),
                        this::isContractThatMustBeAtTheMethodBeginning);

        if (firstContractIdx.isPresent() && lastContractIdx.isPresent()) {
            checkIfFirstContractIsAtTheBeginningOfAMethod(input, firstContractIdx.get());
            checkIfContractBlockContainContractsOnly(input, firstContractIdx.get(),
                    lastContractIdx.get());
        }
    }

    private void checkIfFirstContractIsAtTheBeginningOfAMethod(MethodValidationInput input,
            int firstContractIdx) {

        boolean isContractFirstStatement = firstContractIdx == 0;
        boolean isContractSecondStatementInConstructor =
                AstUtil.isConstructor(input.getMethod()) && firstContractIdx == 1;

        if (!isContractFirstStatement && !isContractSecondStatementInConstructor) {
            StatementTree offending = input.getStatements().get(firstContractIdx);
            CompilationUnitTree compilationUnit = input.getCompilationUnit();
            errorProducer.raiseError(
                    ContractError.CONTRACT_CAN_OCCUR_IN_BLOCK_AT_THE_BEGINNING_OF_THE_METHOD,
                    offending, compilationUnit);
        }
    }

    private void checkIfContractBlockContainContractsOnly(MethodValidationInput input,
            int firstContractIdx, int lastContractIdx) {

        for (int i = firstContractIdx; i < lastContractIdx; i++) {
            StatementTree statement = input.getStatements().get(i);
            CompilationUnitTree compilationUnit = input.getCompilationUnit();
            if (!isContractThatMustBeAtTheMethodBeginning(statement)) {
                errorProducer.raiseError(ContractError.CONTRACT_BLOCK_CAN_CONTAIN_ONLY_CONTRACTS,
                        statement, compilationUnit);
            }
        }
    }

    private boolean isContractThatMustBeAtTheMethodBeginning(StatementTree statement) {
        if (ContractAstUtil.isContractInvocation(statement)) {
            ContractInvocation contract = ContractAstUtil.getContractInvocation(statement);
            ContractMethod contractMethod = contract.getContractMethod();
            return contractMethod.canOccurAtTheBeginningOfTheMethodOnly();
        }
        return false;
    }

    private void checkIfResultOccursInsideEnsuresInNonVoidMethodsOnly(MethodValidationInput input) {
        JCMethodDecl method = input.getMethod();
        ContractResultValidator validator = resultValidatorFactory.create(input);
        method.accept(validator);
    }

    private void checkIfResultTypeMatchesMethodType(MethodValidationInput input) {
        JCMethodDecl method = input.getMethod();
        ResultTypeValidator validator = resultTypeValidatorFactory.create(input);
        method.accept(validator);
    }

    private void checkIfForAllAndExistsOccursInsideContractSpecificationsOnly(
            MethodValidationInput input) {

        JCMethodDecl method = input.getMethod();
        ForAllExistsValidator validator = forAllExistsValidatorFactory.create(input);
        method.accept(validator);
    }

    private void checkIfInvariantsOccursInsideInvariantMethodsOnly(MethodValidationInput input) {
        JCMethodDecl method = input.getMethod();
        InvariantCallValidator validator = invariantCallValidatorFactory.create(input);
        method.accept(validator);
    }
}