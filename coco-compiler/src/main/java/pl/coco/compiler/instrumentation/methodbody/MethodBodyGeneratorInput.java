package pl.coco.compiler.instrumentation.methodbody;

import java.util.List;

import com.sun.tools.javac.code.Symbol.VarSymbol;
import com.sun.tools.javac.tree.JCTree.JCClassDecl;
import com.sun.tools.javac.tree.JCTree.JCMethodDecl;

import pl.coco.compiler.instrumentation.invocation.internal.old.OldValue;
import pl.coco.compiler.instrumentation.synthetic.ContractSyntheticMethods;

public class MethodBodyGeneratorInput {

    private final ContractSyntheticMethods syntheticMethods;
    private final JCClassDecl clazz;
    private final JCMethodDecl originalMethod;
    private final VarSymbol result;
    private final List<OldValue> oldValues;

    public MethodBodyGeneratorInput(ContractSyntheticMethods syntheticMethods, JCClassDecl clazz,
            JCMethodDecl originalMethod, VarSymbol result, List<OldValue> oldValues) {
        this.syntheticMethods = syntheticMethods;
        this.clazz = clazz;
        this.originalMethod = originalMethod;
        this.result = result;
        this.oldValues = oldValues;
    }

    public ContractSyntheticMethods getSyntheticMethods() {
        return syntheticMethods;
    }

    public JCClassDecl getClazz() {
        return clazz;
    }

    public JCMethodDecl getOriginalMethod() {
        return originalMethod;
    }

    public VarSymbol getResultSymbol() {
        return result;
    }

    public List<OldValue> getOldValues() {
        return oldValues;
    }
}
