package pl.coco.compiler.instrumentation.synthetic;

import javax.inject.Inject;
import javax.inject.Singleton;

import com.sun.tools.javac.tree.JCTree.JCClassDecl;
import com.sun.tools.javac.tree.JCTree.JCMethodDecl;

import pl.coco.compiler.instrumentation.invocation.internal.old.OldValue;

@Singleton
public class ContractSyntheticMethodsGenerator {

    private final TargetMethodGenerator targetMethodGenerator;
    private final PreconditionMethodGenerator preconditionMethodGenerator;
    private final PostconditionMethodGenerator postconditionMethodGenerator;
    private final SelfPostconditionMethodGenerator selfPostconditionMethodGenerator;

    @Inject
    public ContractSyntheticMethodsGenerator(TargetMethodGenerator targetMethodGenerator,
            PreconditionMethodGenerator preconditionMethodGenerator,
            PostconditionMethodGenerator postconditionMethodGenerator,
            SelfPostconditionMethodGenerator selfPostconditionMethodGenerator) {
        this.targetMethodGenerator = targetMethodGenerator;
        this.preconditionMethodGenerator = preconditionMethodGenerator;
        this.postconditionMethodGenerator = postconditionMethodGenerator;
        this.selfPostconditionMethodGenerator = selfPostconditionMethodGenerator;
    }

    public ContractSyntheticMethods generateMethods(JCClassDecl clazz, JCMethodDecl method,
            java.util.List<OldValue> oldValues) {
        JCMethodDecl target = targetMethodGenerator.generate(method);
        JCMethodDecl preconditionMethod = preconditionMethodGenerator.generate(clazz, method);
        JCMethodDecl postconditionMethod =
                postconditionMethodGenerator.generate(clazz, method, oldValues);
        JCMethodDecl selfPostconditionMethod =
                selfPostconditionMethodGenerator.generate(clazz, method, oldValues);

        return new ContractSyntheticMethods(target, preconditionMethod, postconditionMethod,
                selfPostconditionMethod);
    }
}
