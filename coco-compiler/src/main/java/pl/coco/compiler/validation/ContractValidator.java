package pl.coco.compiler.validation;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.tools.Diagnostic;

import com.sun.source.tree.StatementTree;
import com.sun.source.util.Trees;
import com.sun.tools.javac.tree.JCTree.JCMethodDecl;

import pl.coco.compiler.instrumentation.invocation.ContractInvocation;
import pl.coco.compiler.instrumentation.synthetic.MethodInput;
import pl.coco.compiler.util.AstUtil;
import pl.coco.compiler.util.CollectionUtils;
import pl.coco.compiler.util.ContractAstUtil;

@Singleton
public class ContractValidator {

    public static final String CONTRACT_CALL_CAN_OCCUR_IN_BLOCK_AT_THE_BEGINNING_OF_THE_METHOD_MSG =
            "This contract call can occur in the contracts block at the beginning of the method only.";
    public static final String CONTRACT_BLOCK_CAN_CONTAIN_ONLY_CONTRACTS = "" +
            "Contract block at the beginning of the method can contain only contracts " +
            "and cannot be interspersed with any other statements.";

    private final Trees trees;

    @Inject
    public ContractValidator(Trees trees) {
        this.trees = trees;
    }

    public void validate(MethodInput input) {
        JCMethodDecl method = (JCMethodDecl) input.getMethod();
        if (doesContainContracts(method)) {
            checkIfAllContractsAreInOneBlockAtTheBeginningOfMethod(input);
        }
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

        checkIfFirstContractIsAtTheBeginningOfAMethod(input, method, stmts, firstContractIdx);
        checkIfContractBlockContainContractsOnly(input, stmts, firstContractIdx, lastContractIdx);
    }

    private void checkIfFirstContractIsAtTheBeginningOfAMethod(MethodInput input,
            JCMethodDecl method, List<? extends StatementTree> stmts, int firstContractIdx) {
        if (firstContractIdx != 0 && !(AstUtil.isConstructor(method) && firstContractIdx == 1)) {
            trees.printMessage(Diagnostic.Kind.ERROR,
                    CONTRACT_CALL_CAN_OCCUR_IN_BLOCK_AT_THE_BEGINNING_OF_THE_METHOD_MSG,
                    stmts.get(firstContractIdx),
                    input.getCompilationUnit());
        }
    }

    private void checkIfContractBlockContainContractsOnly(MethodInput input,
            List<? extends StatementTree> statements, int firstContractIdx, int lastContractIdx) {

        for (int i = firstContractIdx; i < lastContractIdx; i++) {
            StatementTree statement = statements.get(i);
            if (!isContractThatMustBeAtTheMethodBeginning(statement)) {
                trees.printMessage(Diagnostic.Kind.ERROR, CONTRACT_BLOCK_CAN_CONTAIN_ONLY_CONTRACTS,
                        statement, input.getCompilationUnit());
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
}
