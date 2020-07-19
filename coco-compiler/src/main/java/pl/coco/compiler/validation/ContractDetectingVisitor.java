package pl.coco.compiler.validation;

import com.sun.source.tree.MethodInvocationTree;
import com.sun.source.tree.Tree;
import com.sun.source.util.TreeScanner;

import pl.coco.compiler.util.ContractAstUtil;

public class ContractDetectingVisitor extends TreeScanner<Boolean, Void> {

    @Override
    public Boolean scan(Tree tree, Void aVoid) {
        Boolean res = super.scan(tree, aVoid);
        return res != null ? res : false;
    }

    @Override
    public Boolean reduce(Boolean res1, Boolean res2) {
        boolean unboxedRes1 = res1 != null ? res1 : false;
        boolean unboxedRes2 = res2 != null ? res2 : false;
        return unboxedRes1 || unboxedRes2;
    }

    @Override
    public Boolean visitMethodInvocation(MethodInvocationTree methodInvocation, Void aVoid) {
        if (ContractAstUtil.isContractInvocation(methodInvocation)) {
            return true;
        }
        return super.visitMethodInvocation(methodInvocation, aVoid);
    }
}
