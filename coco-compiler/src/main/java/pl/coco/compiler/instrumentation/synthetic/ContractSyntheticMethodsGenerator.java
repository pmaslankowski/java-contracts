package pl.coco.compiler.instrumentation.synthetic;

import javax.inject.Inject;
import javax.inject.Singleton;

import com.sun.tools.javac.tree.JCTree.JCClassDecl;
import com.sun.tools.javac.tree.JCTree.JCMethodDecl;

@Singleton
public class ContractSyntheticMethodsGenerator {

    private final TargetMethodGenerator targetMethodGenerator;
    private final PreconditionMethodGenerator preconditionMethodGenerator;
    private final PostconditionMethodGenerator postconditionMethodGenerator;

    @Inject
    public ContractSyntheticMethodsGenerator(TargetMethodGenerator targetMethodGenerator,
            PreconditionMethodGenerator preconditionMethodGenerator,
            PostconditionMethodGenerator postconditionMethodGenerator) {
        this.targetMethodGenerator = targetMethodGenerator;
        this.preconditionMethodGenerator = preconditionMethodGenerator;
        this.postconditionMethodGenerator = postconditionMethodGenerator;
    }

    public ContractSyntheticMethods generateMethods(JCClassDecl clazz, JCMethodDecl method) {
        JCMethodDecl target = targetMethodGenerator.generate(method);
        JCMethodDecl preconditionMethod = preconditionMethodGenerator.generate(clazz, method);
        JCMethodDecl postconditionMethod = postconditionMethodGenerator.generate(clazz, method);
        return new ContractSyntheticMethods(target, preconditionMethod, postconditionMethod);
    }
}
