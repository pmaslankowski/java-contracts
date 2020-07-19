package pl.coas.compiler.instrumentation;

import java.util.Optional;

import javax.inject.Inject;
import javax.inject.Singleton;

import com.sun.source.tree.ClassTree;
import com.sun.source.tree.MethodTree;
import com.sun.source.util.TreeScanner;
import com.sun.tools.javac.tree.JCTree.JCAnnotation;
import com.sun.tools.javac.tree.JCTree.JCAssign;
import com.sun.tools.javac.tree.JCTree.JCClassDecl;
import com.sun.tools.javac.tree.JCTree.JCExpression;
import com.sun.tools.javac.tree.JCTree.JCFieldAccess;
import com.sun.tools.javac.tree.JCTree.JCIdent;
import com.sun.tools.javac.tree.JCTree.JCLiteral;
import com.sun.tools.javac.tree.JCTree.JCMethodDecl;
import com.sun.tools.javac.util.List;

import pl.coas.api.AspectType;
import pl.coas.compiler.instrumentation.model.Advice;
import pl.coas.compiler.instrumentation.model.Aspect;
import pl.coas.compiler.instrumentation.model.pointcut.Pointcut;
import pl.coas.compiler.instrumentation.parsers.PointcutParserImpl;
import pl.coas.compiler.instrumentation.util.AspectUtils;

@Singleton
public class AnnotationScanningVisitor extends TreeScanner<Void, Void> {

    private static final String ADVICE_ANNOTATION_NAME = "Advice";

    private final PointcutParserImpl pointcutParser;
    private final AspectRegistry registry;

    private JCClassDecl clazz;
    private JCMethodDecl method;

    @Inject
    public AnnotationScanningVisitor(PointcutParserImpl pointcutParser, AspectRegistry registry) {
        this.pointcutParser = pointcutParser;
        this.registry = registry;
    }

    @Override
    public Void visitClass(ClassTree classTree, Void aVoid) {
        if (AspectUtils.isAspectClass(classTree)) {
            clazz = (JCClassDecl) classTree;
            return super.visitClass(classTree, aVoid);
        } else {
            return null;
        }
    }

    @Override
    public Void visitMethod(MethodTree methodTree, Void aVoid) {
        method = (JCMethodDecl) methodTree;
        List<JCAnnotation> annotations = method.getModifiers().getAnnotations();
        Optional<JCAnnotation> adviceAnnotation = getAdviceAnnotation(annotations);
        adviceAnnotation.ifPresent(this::addAspectToRegistry);
        return null;
    }

    private Optional<JCAnnotation> getAdviceAnnotation(List<JCAnnotation> annotations) {
        return annotations.stream()
                .filter(this::isAdviceAnnotation)
                .findAny();
    }

    private boolean isAdviceAnnotation(JCAnnotation annotation) {
        return annotation.getAnnotationType().toString().equals(ADVICE_ANNOTATION_NAME);
    }

    private void addAspectToRegistry(JCAnnotation annotation) {
        Aspect aspect = getAspect(annotation);
        registry.registerAspect(aspect);
    }

    private Aspect getAspect(JCAnnotation annotation) {
        Advice advice = new Advice(clazz, method);
        int order = 0;
        AspectType type = AspectType.SINGLETON;
        Pointcut pointcut = null;

        for (JCExpression arg : annotation.getArguments()) {
            JCAssign property = (JCAssign) arg;
            JCIdent key = (JCIdent) property.lhs;
            if (key.getName().contentEquals("on")) {
                JCLiteral pointcutExpr = (JCLiteral) property.rhs;
                pointcut = pointcutParser.parse((String) pointcutExpr.getValue());
            }

            if (key.getName().contentEquals("order")) {
                order = (int) ((JCLiteral) property.rhs).getValue();
            }

            if (key.getName().contentEquals("type")) {
                JCFieldAccess typeAsFieldAccess = (JCFieldAccess) property.rhs;
                if (typeAsFieldAccess.name.contentEquals("PROTOTYPE")) {
                    type = AspectType.TRANSIENT;
                }
            }
        }

        return new Aspect(pointcut, advice, order, type);
    }
}
