package pl.coco.compiler.annotation;

import java.util.stream.Collectors;

import javax.inject.Inject;
import javax.inject.Singleton;

import com.sun.tools.javac.code.Flags;
import com.sun.tools.javac.code.TypeTag;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.tree.JCTree.JCAnnotation;
import com.sun.tools.javac.tree.JCTree.JCBlock;
import com.sun.tools.javac.tree.JCTree.JCExpression;
import com.sun.tools.javac.tree.JCTree.JCMethodDecl;
import com.sun.tools.javac.tree.JCTree.JCModifiers;
import com.sun.tools.javac.tree.JCTree.JCPrimitiveTypeTree;
import com.sun.tools.javac.tree.JCTree.JCStatement;
import com.sun.tools.javac.tree.JCTree.JCTypeParameter;
import com.sun.tools.javac.tree.TreeMaker;
import com.sun.tools.javac.util.List;
import com.sun.tools.javac.util.Name;
import com.sun.tools.javac.util.Names;

@Singleton
public class InvariantTranslator {

    private static final String INVARIANT_METHOD_NAME = "$invariantMethod";
    private static final String INVARIANT_METHOD_ANNOTATION_NAME = "InvariantMethod";

    private final TreeMaker treeMaker;
    private final Names names;
    private final AnnotationTranslator annotationTranslator;

    @Inject
    public InvariantTranslator(TreeMaker treeMaker, Names names,
            AnnotationTranslator annotationTranslator) {
        this.treeMaker = treeMaker;
        this.names = names;
        this.annotationTranslator = annotationTranslator;
    }

    public JCMethodDecl translateToInvariantMethod(java.util.List<ContractAnnotation> invariants) {
        JCBlock body = translateToMethodBody(invariants);

        JCModifiers modifiers = getMethodModifiers();
        Name methodName = names.fromString(INVARIANT_METHOD_NAME);
        JCPrimitiveTypeTree returnType = treeMaker.TypeIdent(TypeTag.VOID);
        List<JCTypeParameter> typeParameters = List.nil();
        List<JCTree.JCVariableDecl> params = List.nil();
        List<JCExpression> thrown = List.nil();

        return treeMaker.MethodDef(modifiers, methodName, returnType, typeParameters, params,
                thrown, body, null);
    }

    private JCBlock translateToMethodBody(java.util.List<ContractAnnotation> invariants) {
        java.util.List<JCStatement> translatedInvariants = invariants.stream()
                .map(annotationTranslator::translate)
                .collect(Collectors.toList());

        return treeMaker.Block(0, List.from(translatedInvariants));
    }

    private JCModifiers getMethodModifiers() {
        long flags = 0;
        flags |= Flags.PRIVATE;
        flags |= Flags.SYNTHETIC;
        JCAnnotation invariantAnnotation = getInvariantMethodAnnotation();
        return treeMaker.Modifiers(flags, List.of(invariantAnnotation));
    }

    private JCAnnotation getInvariantMethodAnnotation() {
        Name annotationName = names.fromString(INVARIANT_METHOD_ANNOTATION_NAME);
        List<JCExpression> params = List.nil();
        return treeMaker.Annotation(treeMaker.Ident(annotationName), params);
    }
}
