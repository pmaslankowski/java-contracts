package pl.coco.compiler.instrumentation;

import javax.inject.Inject;

import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.tree.JCTree.JCMethodDecl;

import pl.coco.compiler.instrumentation.bridge.TargetMethodBuilder;
import pl.coco.compiler.instrumentation.contract.PostconditionMethodGenerator;
import pl.coco.compiler.instrumentation.contract.PreconditionMethodGenerator;

public class ContractSyntheticMethodsGenerator {

    private final TargetMethodBuilder targetMethodBuilder;
    private final PreconditionMethodGenerator preconditionMethodGenerator;
    private final PostconditionMethodGenerator postconditionMethodGenerator;

    @Inject
    public ContractSyntheticMethodsGenerator(TargetMethodBuilder targetMethodBuilder,
            PreconditionMethodGenerator preconditionMethodGenerator,
            PostconditionMethodGenerator postconditionMethodGenerator) {
        this.targetMethodBuilder = targetMethodBuilder;
        this.preconditionMethodGenerator = preconditionMethodGenerator;
        this.postconditionMethodGenerator = postconditionMethodGenerator;
    }

    public ContractSyntheticMethods generateMethods(JCTree.JCClassDecl clazz, JCMethodDecl method) {
        JCMethodDecl target = targetMethodBuilder.buildTarget(method);
        JCMethodDecl preconditionMethod = preconditionMethodGenerator.generate(clazz, method);
        JCMethodDecl postconditionMethod = postconditionMethodGenerator.generate(clazz, method);
        return new ContractSyntheticMethods(target, preconditionMethod, postconditionMethod);
    }
}
