package pl.coco.compiler.instrumentation;

import java.util.ArrayList;
import java.util.List;

import com.sun.tools.javac.tree.JCTree;

public class ProcessedContracts {

    private final List<JCTree.JCStatement> preconditions = new ArrayList<>();
    private final List<JCTree.JCStatement> postconditions = new ArrayList<>();

    public void add(ContractMethod contractMethod, JCTree.JCStatement statement) {
        switch (contractMethod) {
            case REQUIRES:
                addPrecondition(statement);
                break;
            case ENSURES:
                addPostcondition(statement);
                break;
            default:
                throw new IllegalArgumentException(
                        "Contract method: " + contractMethod + " is not supported in this classe.");
        }
    }

    private void addPrecondition(JCTree.JCStatement precondition) {
        preconditions.add(precondition);
    }

    private void addPostcondition(JCTree.JCStatement postcondition) {
        postconditions.add(postcondition);
    }

    public List<JCTree.JCStatement> getPreconditions() {
        return preconditions;
    }

    public List<JCTree.JCStatement> getPostconditions() {
        return postconditions;
    }
}
