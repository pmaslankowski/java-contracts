package pl.coco.compiler.instrumentation.synthetic;

import com.sun.tools.javac.tree.JCTree.JCMethodDecl;

public class ContractSyntheticMethods {

    private final JCMethodDecl target;
    private final JCMethodDecl preconditions;
    private final JCMethodDecl postconditions;

    public ContractSyntheticMethods(JCMethodDecl target, JCMethodDecl preconditions,
            JCMethodDecl postconditions) {
        this.target = target;
        this.preconditions = preconditions;
        this.postconditions = postconditions;
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

}
