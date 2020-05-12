package pl.coco.compiler.instrumentation.synthetic;

import static java.util.stream.Collectors.toList;

import java.util.stream.Collectors;

import com.sun.source.tree.MethodTree;
import com.sun.source.tree.VariableTree;
import com.sun.tools.javac.code.Flags;
import com.sun.tools.javac.code.Symbol;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.tree.JCTree.JCClassDecl;
import com.sun.tools.javac.tree.JCTree.JCExpression;
import com.sun.tools.javac.tree.JCTree.JCIdent;
import com.sun.tools.javac.tree.JCTree.JCMethodDecl;
import com.sun.tools.javac.tree.JCTree.JCVariableDecl;
import com.sun.tools.javac.tree.TreeMaker;
import com.sun.tools.javac.util.Name;
import com.sun.tools.javac.util.Names;

import pl.coco.compiler.instrumentation.invocation.ContractInvocation;
import pl.coco.compiler.instrumentation.invocation.InternalInvocationBuilder;

public abstract class AbstractMethodGenerator {

    protected final TreeMaker treeMaker;
    protected final Names names;
    private final InternalInvocationBuilder internalInvocationBuilder;

    public AbstractMethodGenerator(TreeMaker treeMaker, Names names,
            InternalInvocationBuilder internalInvocationBuilder) {
        this.treeMaker = treeMaker;
        this.names = names;
        this.internalInvocationBuilder = internalInvocationBuilder;
    }

    public abstract JCMethodDecl generate(JCClassDecl clazz, JCMethodDecl method);

    protected Name getMethodNameWithPrefix(String prefix, JCMethodDecl originalMethod) {
        return names.fromString(prefix).append(originalMethod.getName());
    }

    protected long getProtectedMethodFlags(JCMethodDecl originalMethod) {
        long result = originalMethod.sym.flags();
        result &= ~Flags.PRIVATE;
        result &= ~Flags.PUBLIC;
        result |= Flags.PROTECTED;
        result |= Flags.SYNTHETIC;
        return result;
    }

    protected java.util.List<JCExpression> getMethodArguments(MethodTree method) {
        return method.getParameters().stream()
                .map(this::toIdentifier)
                .collect(toList());
    }

    private JCIdent toIdentifier(VariableTree param) {
        JCVariableDecl variableDec = (JCVariableDecl) param;
        return treeMaker.Ident(variableDec.sym);
    }

    protected java.util.List<JCTree.JCStatement> convertContractsToStatements(JCMethodDecl wrapper,
            java.util.List<ContractInvocation> postconditions, Symbol resultSymbol) {

        return postconditions.stream()
                .map(contract -> internalInvocationBuilder.build(contract, wrapper, resultSymbol))
                .collect(Collectors.toList());
    }
}
