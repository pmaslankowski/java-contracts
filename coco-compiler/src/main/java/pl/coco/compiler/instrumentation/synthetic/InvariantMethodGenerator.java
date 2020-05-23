package pl.coco.compiler.instrumentation.synthetic;

import java.util.stream.Collectors;

import javax.inject.Inject;
import javax.inject.Singleton;

import com.sun.tools.javac.code.Symbol.MethodSymbol;
import com.sun.tools.javac.code.Symbol.VarSymbol;
import com.sun.tools.javac.code.Symtab;
import com.sun.tools.javac.code.Type.JCVoidType;
import com.sun.tools.javac.code.Type.MethodType;
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
    private final IsBeforeSymbolProvider isBeforeSymbolProvider;
    private final Symtab symtab;

    @Inject
    public InvariantMethodGenerator(TreeMaker treeMaker, Names names,
            InvariantInvocationBuilder invariantInvocationBuilder,
            SyntheticMethodNameGenerator nameGenerator,
            IsBeforeSymbolProvider isBeforeSymbolProvider, Symtab symtab) {
        super(treeMaker, names);
        this.treeMaker = treeMaker;
        this.invariantInvocationBuilder = invariantInvocationBuilder;
        this.nameGenerator = nameGenerator;
        this.isBeforeSymbolProvider = isBeforeSymbolProvider;
        this.symtab = symtab;
    }

    public JCMethodDecl generate(JCClassDecl clazz, JCMethodDecl method) {
        MethodSymbol invariantSymbol = getInvariantSymbol(method);
        return treeMaker.MethodDef(invariantSymbol, invariantSymbol.type,
                getProcessedInvariantBody(method));
    }

    private MethodSymbol getInvariantSymbol(JCMethodDecl invariantMethod) {
        Name invariantMethodName = nameGenerator.getInvariantMethodName();
        MethodType invariantMethodType = getInvariantMethodType();
        long flags = getProtectedMethodFlags(invariantMethod);

        MethodSymbol result = new MethodSymbol(flags, invariantMethodName, invariantMethodType,
                invariantMethod.sym.owner);

        VarSymbol isBeforeSymbol = isBeforeSymbolProvider.get(invariantMethod);
        result.params = List.of(isBeforeSymbol);

        return result;
    }

    private MethodType getInvariantMethodType() {
        return new MethodType(List.of(symtab.booleanType), new JCVoidType(), List.nil(), null);
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
