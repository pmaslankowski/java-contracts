package pl.coco.compiler.instrumentation.invocation;

import javax.inject.Inject;

import com.sun.tools.javac.tree.JCTree.JCExpression;
import com.sun.tools.javac.tree.JCTree.JCIdent;
import com.sun.tools.javac.tree.JCTree.JCMethodInvocation;
import com.sun.tools.javac.tree.TreeMaker;
import com.sun.tools.javac.util.List;

public class MethodInvocationBuilder {

    private final TreeMaker treeMaker;
    private final ClassAccessBuilder classAccessBuilder;

    @Inject
    public MethodInvocationBuilder(TreeMaker treeMaker, ClassAccessBuilder classAccessBuilder) {
        this.treeMaker = treeMaker;
        this.classAccessBuilder = classAccessBuilder;
    }

    public JCMethodInvocation build(MethodInvocationDescription desc) {
        List<JCExpression> typeParameters = List.nil();
        JCExpression methodAccess = newMethodAccess(desc);
        return treeMaker.at(desc.getPosition())
                .Apply(typeParameters, methodAccess, desc.getArguments())
                .setType(desc.getMethodSymbol().asType().getReturnType());
    }

    private JCExpression newMethodAccess(MethodInvocationDescription desc) {
        if (desc.getClassName() != null) {
            return getStaticMethodAccess(desc);
        } else {
            return getInstanceMethodAccess(desc);
        }
    }

    private JCExpression getStaticMethodAccess(MethodInvocationDescription desc) {
        JCExpression classAccess = newClassAccess(desc);
        return treeMaker.at(desc.getPosition()).Select(classAccess, desc.getMethodSymbol());
    }

    private JCExpression newClassAccess(MethodInvocationDescription desc) {
        return classAccessBuilder.build(desc.getClassName(), desc.getPosition());
    }

    private JCIdent getInstanceMethodAccess(MethodInvocationDescription desc) {
        return treeMaker.at(desc.getPosition()).Ident(desc.getMethodSymbol());
    }
}
