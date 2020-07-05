package pl.coco.compiler.instrumentation.synthetic;

import com.sun.tools.javac.tree.JCTree.JCMethodDecl;

public class ContractSyntheticMethods {

    private final JCMethodDecl target;
    private final JCMethodDecl preconditions;
    private final JCMethodDecl postconditions;
    private final JCMethodDecl selfPostconditions;

    public ContractSyntheticMethods(JCMethodDecl target, JCMethodDecl preconditions,
            JCMethodDecl postconditions, JCMethodDecl selfPostconditions) {
        this.target = target;
        this.preconditions = preconditions;
        this.postconditions = postconditions;
        this.selfPostconditions = selfPostconditions;
    }

    public JCMethodDecl getTarget() {
        return target;
    }

    public JCMethodDecl getPreconditions() {
        return preconditions;
    }

    public JCMethodDecl getPostconditions() {
        return postconditions;
    }

    public JCMethodDecl getSelfPostconditions() {
        return selfPostconditions;
    }
}
