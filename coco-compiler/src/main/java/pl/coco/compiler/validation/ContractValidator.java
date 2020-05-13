package pl.coco.compiler.validation;

import java.util.List;

import javax.inject.Inject;

import com.sun.source.tree.StatementTree;
import com.sun.tools.javac.tree.JCTree.JCMethodDecl;

import pl.coco.compiler.instrumentation.invocation.ContractInvocation;
import pl.coco.compiler.instrumentation.synthetic.MethodInput;
import pl.coco.compiler.util.AstUtil;
import pl.coco.compiler.util.CollectionUtils;
import pl.coco.compiler.util.ContractAstUtil;

public class ContractValidator {

    private final ErrorProducer errorProducer;
    private final ContractResultValidator resultValidator;
    private final ResultTypeValidator resultTypeValidator;

    @Inject
    public ContractValidator(ErrorProducer errorProducer, ContractResultValidator resultValidator,
            ResultTypeValidator resultTypeValidator) {
        this.errorProducer = errorProducer;
        this.resultValidator = resultValidator;
        this.resultTypeValidator = resultTypeValidator;
    }

    public int validate(MethodInput input) {

        JCMethodDecl method = (JCMethodDecl) input.getMethod();
        if (doesContainContracts(method)) {
            checkIfAllContractsAreInOneBlockAtTheBeginningOfMethod(input);
            checkIfResultOccursInsideEnsuresInNonVoidMethodsOnly(input);
            checkIfResultTypeMatchesMethodType(input);
            return errorProducer.getErrorCount();
        }
        return 0;
    }

    private boolean doesContainContracts(JCMethodDecl method) {
        return method.getBody().getStatements().stream()
                .anyMatch(ContractAstUtil::isContractInvocation);
    }

    private void checkIfAllContractsAreInOneBlockAtTheBeginningOfMethod(MethodInput input) {
        JCMethodDecl method = (JCMethodDecl) input.getMethod();
        List<? extends StatementTree> stmts = method.getBody().getStatements();

        int firstContractIdx = CollectionUtils.getIndexOfFirstElementMatchingPredicate(
                stmts, this::isContractThatMustBeAtTheMethodBeginning);
        int lastContractIdx = CollectionUtils.getIndexOfLastElementMatchingPredicate(
                stmts, this::isContractThatMustBeAtTheMethodBeginning);

        checkIfFirstContractIsAtTheBeginningOfAMethod(method, stmts, firstContractIdx);
        checkIfContractBlockContainContractsOnly(stmts, firstContractIdx, lastContractIdx);
    }

    private void checkIfFirstContractIsAtTheBeginningOfAMethod(JCMethodDecl method,
            List<? extends StatementTree> statements, int firstContractIdx) {

        if (firstContractIdx != 0 && !(AstUtil.isConstructor(method) && firstContractIdx == 1)) {
            errorProducer.raiseError(
                    ContractError.CONTRACT_CAN_OCCUR_IN_BLOCK_AT_THE_BEGINNING_OF_THE_METHOD,
                    statements.get(firstContractIdx));
        }
    }

    private void checkIfContractBlockContainContractsOnly(List<? extends StatementTree> statements,
            int firstContractIdx, int lastContractIdx) {

        for (int i = firstContractIdx; i < lastContractIdx; i++) {
            StatementTree statement = statements.get(i);
            if (!isContractThatMustBeAtTheMethodBeginning(statement)) {
                errorProducer.raiseError(ContractError.CONTRACT_BLOCK_CAN_CONTAIN_ONLY_CONTRACTS,
                        statement);
            }
        }
    }

    private boolean isContractThatMustBeAtTheMethodBeginning(StatementTree statement) {
        if (ContractAstUtil.isContractInvocation(statement)) {
            ContractInvocation contract = ContractAstUtil.getContractInvocation(statement);
            return contract.canOccurAtTheBeginningOfTheMethodOnly();
        }
        return false;
    }

    private void checkIfResultOccursInsideEnsuresInNonVoidMethodsOnly(MethodInput input) {
        JCMethodDecl method = (JCMethodDecl) input.getMethod();
        method.accept(resultValidator);
    }

    private void checkIfResultTypeMatchesMethodType(MethodInput input) {
        JCMethodDecl method = (JCMethodDecl) input.getMethod();
        method.accept(resultTypeValidator);
    }
}
