package pl.coco.compiler.instrumentation.synthetic;

import java.util.stream.Collectors;

import javax.inject.Inject;
import javax.inject.Singleton;

import com.sun.tools.javac.code.Symbol.MethodSymbol;
import com.sun.tools.javac.tree.JCTree.JCBlock;
import com.sun.tools.javac.tree.JCTree.JCClassDecl;
import com.sun.tools.javac.tree.JCTree.JCMethodDecl;
import com.sun.tools.javac.tree.JCTree.JCStatement;
import com.sun.tools.javac.tree.TreeMaker;
import com.sun.tools.javac.util.List;
import com.sun.tools.javac.util.Name;
import com.sun.tools.javac.util.Names;

import pl.coco.compiler.instrumentation.invocation.ContractInvocation;
import pl.coco.compiler.instrumentation.invocation.internal.invariant.InvariantInvocationBuilder;
import pl.coco.compiler.util.ContractAstUtil;

@Singleton
public class InvariantMethodGenerator extends AbstractMethodGenerator {

    private final TreeMaker treeMaker;
    private final InvariantInvocationBuilder invariantInvocationBuilder;
    private final SyntheticMethodNameGenerator nameGenerator;

    @Inject
    public InvariantMethodGenerator(TreeMaker treeMaker, Names names,
            InvariantInvocationBuilder invariantInvocationBuilder,
            SyntheticMethodNameGenerator nameGenerator) {
        super(treeMaker, names);
        this.treeMaker = treeMaker;
        this.invariantInvocationBuilder = invariantInvocationBuilder;
        this.nameGenerator = nameGenerator;
    }

    public JCMethodDecl generate(JCClassDecl clazz, JCMethodDecl method) {
        MethodSymbol invariantSymbol = getInvariantSymbol(method);
        return treeMaker.MethodDef(invariantSymbol, invariantSymbol.type,
                getProcessedInvariantBody(method));
    }

    private MethodSymbol getInvariantSymbol(JCMethodDecl invariantMethod) {
        Name invariantMethodName = nameGenerator.getInvariantMethodName();
        long flags = getProtectedMethodFlags(invariantMethod);

        MethodSymbol result = new MethodSymbol(flags, invariantMethodName, invariantMethod.type,
                invariantMethod.sym.owner);

        result.params = invariantMethod.sym.params;
        return result;
    }

    private JCBlock getProcessedInvariantBody(JCMethodDecl originalInvariant) {

        List<JCStatement> statements = originalInvariant.getBody().getStatements();
        java.util.List<ContractInvocation> invariants = statements.stream()
                .map(ContractAstUtil::getContractInvocation)
                .collect(Collectors.toList());

        java.util.List<JCStatement> processedInvariants = invariants.stream()
                .map(contract -> invariantInvocationBuilder.build(contract, originalInvariant))
                .collect(Collectors.toList());

        return treeMaker.Block(0, List.from(processedInvariants));
    }
}
