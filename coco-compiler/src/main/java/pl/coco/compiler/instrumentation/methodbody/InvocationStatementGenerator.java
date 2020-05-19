package pl.coco.compiler.instrumentation.methodbody;

import static java.util.stream.Collectors.toList;

import javax.inject.Inject;
import javax.inject.Singleton;

import com.sun.source.tree.MethodTree;
import com.sun.source.tree.VariableTree;
import com.sun.tools.javac.code.Symbol.VarSymbol;
import com.sun.tools.javac.tree.JCTree.JCIdent;
import com.sun.tools.javac.tree.JCTree.JCMethodDecl;
import com.sun.tools.javac.tree.JCTree.JCMethodInvocation;
import com.sun.tools.javac.tree.JCTree.JCStatement;
import com.sun.tools.javac.tree.JCTree.JCVariableDecl;
import com.sun.tools.javac.tree.TreeMaker;

import pl.coco.compiler.instrumentation.invocation.MethodInvocationBuilder;
import pl.coco.compiler.instrumentation.invocation.MethodInvocationDescription;
import pl.coco.compiler.util.AstUtil;

@Singleton
public class InvocationStatementGenerator {

    private final TreeMaker treeMaker;
    private final MethodInvocationBuilder methodInvocationBuilder;

    @Inject
    public InvocationStatementGenerator(TreeMaker treeMaker,
            MethodInvocationBuilder methodInvocationBuilder) {
        this.treeMaker = treeMaker;
        this.methodInvocationBuilder = methodInvocationBuilder;
    }

    public JCStatement generate(JCMethodDecl method) {
        return treeMaker.Call(generateMethodInvocation(method));
    }

    public JCStatement generateTarget(JCMethodDecl target, VarSymbol result) {
        JCMethodInvocation targetMethodInvocation = generateMethodInvocation(target);

        if (AstUtil.isVoid(target)) {
            return treeMaker.Call(targetMethodInvocation);
        }

        return treeMaker.VarDef(result, targetMethodInvocation);
    }

    private JCMethodInvocation generateMethodInvocation(JCMethodDecl method) {

        java.util.List<JCIdent> parameters = getParameters(method);
        MethodInvocationDescription desc = new MethodInvocationDescription.Builder()
                .withMethodSymbol(method.sym)
                .withArguments(com.sun.tools.javac.util.List.from(parameters))
                .build();
        return methodInvocationBuilder.build(desc);
    }

    private java.util.List<JCIdent> getParameters(MethodTree method) {
        return method.getParameters().stream()
                .map(this::toIdentifier)
                .collect(toList());
    }

    private JCIdent toIdentifier(VariableTree param) {
        JCVariableDecl variableDec = (JCVariableDecl) param;
        return treeMaker.Ident(variableDec.sym);
    }
}
