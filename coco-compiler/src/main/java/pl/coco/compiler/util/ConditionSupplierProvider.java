package pl.coco.compiler.util;

import com.sun.tools.javac.api.JavacTaskImpl;
import com.sun.tools.javac.code.Type;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.tree.TreeMaker;
import com.sun.tools.javac.util.List;

public class ConditionSupplierProvider {

    private static final String CONDITION_SUPPLIER_CLASS = "pl.coco.internal.ConditionSupplier";

    private final TreeMaker treeMaker;
    private final TypeRegistry typeRegistry;

    public ConditionSupplierProvider(JavacTaskImpl task) {
        this.treeMaker = TreeMaker.instance(task.getContext());
        this.typeRegistry = new TypeRegistry(task);
    }

    public JCTree.JCLambda getSupplier(JCTree.JCExpression precondition) {
        JCTree.JCLambda conditionSupplier = treeMaker.Lambda(List.nil(), precondition);
        Type conditionSupplierType = typeRegistry.getType(CONDITION_SUPPLIER_CLASS);
        conditionSupplier.type = conditionSupplierType;
        conditionSupplier.targets = List.of(conditionSupplierType);
        return conditionSupplier;
    }
}
