package pl.coco.compiler.instrumentation.methodbody;

import com.sun.tools.javac.code.Symbol.VarSymbol;
import com.sun.tools.javac.tree.JCTree.JCClassDecl;
import com.sun.tools.javac.tree.JCTree.JCMethodDecl;

import pl.coco.compiler.instrumentation.synthetic.ContractSyntheticMethods;

public class MethodBodyGeneratorInput {

    private final ContractSyntheticMethods syntheticMethods;
    private final JCClassDecl clazz;
    private final JCMethodDecl originalMethod;
    private final VarSymbol result;

    public MethodBodyGeneratorInput(ContractSyntheticMethods syntheticMethods, JCClassDecl clazz,
            JCMethodDecl originalMethod, VarSymbol result) {
        this.syntheticMethods = syntheticMethods;
        this.clazz = clazz;
        this.originalMethod = originalMethod;
        this.result = result;
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
}
