package pl.coco.compiler.instrumentation.synthetic;

import static java.util.stream.Collectors.toList;

import com.sun.source.tree.MethodTree;
import com.sun.source.tree.VariableTree;
import com.sun.tools.javac.code.Flags;
import com.sun.tools.javac.tree.JCTree.JCExpression;
import com.sun.tools.javac.tree.JCTree.JCIdent;
import com.sun.tools.javac.tree.JCTree.JCMethodDecl;
import com.sun.tools.javac.tree.JCTree.JCVariableDecl;
import com.sun.tools.javac.tree.TreeMaker;
import com.sun.tools.javac.util.Names;

public abstract class AbstractMethodGenerator {

    protected final TreeMaker treeMaker;
    protected final Names names;

    public AbstractMethodGenerator(TreeMaker treeMaker, Names names) {
        this.treeMaker = treeMaker;
        this.names = names;
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
}
