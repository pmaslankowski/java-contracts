package pl.coco.compiler.instrumentation.contract;

import static java.util.stream.Collectors.toList;

import com.sun.source.tree.MethodTree;
import com.sun.source.tree.VariableTree;
import com.sun.tools.javac.code.Flags;
import com.sun.tools.javac.code.Symbol.MethodSymbol;
import com.sun.tools.javac.code.Type;
import com.sun.tools.javac.code.Type.MethodType;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.tree.JCTree.JCExpression;
import com.sun.tools.javac.tree.JCTree.JCIdent;
import com.sun.tools.javac.tree.JCTree.JCMethodDecl;
import com.sun.tools.javac.tree.JCTree.JCVariableDecl;
import com.sun.tools.javac.tree.TreeMaker;
import com.sun.tools.javac.util.List;
import com.sun.tools.javac.util.Name;
import com.sun.tools.javac.util.Names;

public abstract class AbstractMethodGenerator {

    protected final TreeMaker treeMaker;
    protected final Names names;

    public AbstractMethodGenerator(TreeMaker treeMaker, Names names) {
        this.treeMaker = treeMaker;
        this.names = names;
    }

    public abstract JCMethodDecl generate(JCTree.JCClassDecl clazz, JCMethodDecl method);

    protected MethodSymbol getMethodSymbolMirroring(String prefix, JCMethodDecl originalMethod) {
        Name bridgeMethodName = getPreconditionMethodName(prefix, originalMethod);
        long flags = getPreconditionMethodFlags(originalMethod);
        MethodType type = getPreconditionMethodType(originalMethod);

        MethodSymbol result =
                new MethodSymbol(flags, bridgeMethodName, type, originalMethod.sym.owner);

        result.params = originalMethod.sym.params;

        return result;
    }

    private Name getPreconditionMethodName(String prefix, JCMethodDecl originalMethod) {
        return names.fromString(prefix).append(originalMethod.getName());
    }

    private MethodType getPreconditionMethodType(JCMethodDecl originalMethod) {
        return new MethodType(originalMethod.sym.type.getParameterTypes(),
                new Type.JCVoidType(), List.nil(), null);
    }

    private long getPreconditionMethodFlags(JCMethodDecl originalMethod) {
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

}
