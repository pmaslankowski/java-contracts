package pl.coco.compiler.validation;

import java.util.Optional;

import javax.inject.Inject;

import com.sun.source.tree.CompilationUnitTree;
import com.sun.source.tree.StatementTree;
import com.sun.tools.javac.tree.JCTree.JCMethodDecl;

import pl.coco.compiler.instrumentation.ContractMethod;
import pl.coco.compiler.model.ContractInvocation;
import pl.coco.compiler.util.ContractAstUtil;
import pl.coco.compiler.validation.forallexists.ForAllExistsValidator;
import pl.coco.compiler.validation.invariant.ClassInvariantCallValidator;
import pl.coco.compiler.validation.old.OldArgsValidator;
import pl.coco.compiler.validation.old.OldValidator;
import pl.coco.compiler.validation.result.ContractResultValidator;
import pl.coco.compiler.validation.result.ResultTypeValidator;
import pl.coco.compiler.validation.result.ResultTypeValidatorFactory;
import pl.compiler.commons.util.AstUtil;
import pl.compiler.commons.util.CollectionUtils;

public class ContractMethodValidator {

    private final ErrorProducer errorProducer;
    private final ResultTypeValidatorFactory resultTypeValidatorFactory;

    @Inject
    public ContractMethodValidator(ErrorProducer errorProducer,
            ResultTypeValidatorFactory resultTypeValidatorFactory) {

        this.errorProducer = errorProducer;
        this.resultTypeValidatorFactory = resultTypeValidatorFactory;
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
            checkIfOldOccursInsidePostconditionsOnly(input);
            checkIfOldIsUsedOnMethodArgsOnly(input);
            return true;
        } catch (ContractValidationException ex) {
            errorProducer.produceErrorMessage(ex);
            return false;
        }
    }

    private boolean doesContainContracts(JCMethodDecl method) {
        return method.getBody().getStatements().stream()
                .anyMatch(statement -> statement.accept(new ContractDetectingVisitor(), null));
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
            throw new ContractValidationException(
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
                throw new ContractValidationException(
                        ContractError.CONTRACT_BLOCK_CAN_CONTAIN_ONLY_CONTRACTS,
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
        method.accept(new ContractResultValidator(input));
    }

    private void checkIfResultTypeMatchesMethodType(MethodValidationInput input) {
        JCMethodDecl method = input.getMethod();
        ResultTypeValidator validator = resultTypeValidatorFactory.create(input);
        method.accept(validator);
    }

    private void checkIfForAllAndExistsOccursInsideContractSpecificationsOnly(
            MethodValidationInput input) {

        JCMethodDecl method = input.getMethod();
        method.accept(new ForAllExistsValidator(input));
    }

    private void checkIfInvariantsOccursInsideInvariantMethodsOnly(MethodValidationInput input) {
        JCMethodDecl method = input.getMethod();
        method.accept(new ClassInvariantCallValidator(input));
    }

    private void checkIfOldOccursInsidePostconditionsOnly(MethodValidationInput input) {
        JCMethodDecl method = input.getMethod();
        method.accept(new OldValidator(input));
    }

    private void checkIfOldIsUsedOnMethodArgsOnly(MethodValidationInput input) {
        JCMethodDecl method = input.getMethod();
        method.accept(new OldArgsValidator(input));
    }
}
