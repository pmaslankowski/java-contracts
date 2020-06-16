package pl.coas.compiler.instrumentation;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.inject.Inject;
import javax.inject.Singleton;

import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.tree.JCTree.JCClassDecl;
import com.sun.tools.javac.tree.JCTree.JCExpression;
import com.sun.tools.javac.tree.JCTree.JCExpressionStatement;
import com.sun.tools.javac.tree.JCTree.JCFieldAccess;
import com.sun.tools.javac.tree.JCTree.JCLiteral;
import com.sun.tools.javac.tree.JCTree.JCMethodDecl;
import com.sun.tools.javac.tree.JCTree.JCMethodInvocation;
import com.sun.tools.javac.tree.JCTree.JCStatement;

import pl.coas.api.AspectType;
import pl.coas.compiler.instrumentation.model.Advice;
import pl.coas.compiler.instrumentation.model.Aspect;
import pl.coas.compiler.instrumentation.model.pointcut.Pointcut;
import pl.coas.util.StreamUtil;
import pl.compiler.commons.model.SimpleMethodInvocation;
import pl.compiler.commons.util.TreePasser;

@Singleton
public class AspectScanner {

    private static final String ASPECT_CLASS_NAME = "pl.coas.api.Aspect";
    private static final String POINTCUT_METHOD = "on";
    private static final String TYPE_METHOD = "type";
    private static final String ORDER_METHOD = "order";

    private final PointcutFactory pointcutFactory;
    private final AspectRegistry aspectRegistry;

    @Inject
    public AspectScanner(PointcutFactory pointcutFactory, AspectRegistry aspectRegistry) {
        this.pointcutFactory = pointcutFactory;
        this.aspectRegistry = aspectRegistry;
    }

    public void scan(JCClassDecl clazz) {
        for (JCTree member : clazz.getMembers()) {
            TreePasser.of(member)
                    .as(JCMethodDecl.class)
                    .get()
                    .ifPresent(method -> processMethod(clazz, method));
        }
    }

    private void processMethod(JCClassDecl clazz, JCMethodDecl method) {
        List<SimpleMethodInvocation> aspectInvocations = getAspectInvocations(method);
        if (!aspectInvocations.isEmpty()) {
            Aspect aspect = getAspect(clazz, method, aspectInvocations);
            aspectRegistry.registerAspect(aspect);
            removeAspectStatements(method);
        }
    }

    private Aspect getAspect(JCClassDecl clazz, JCMethodDecl method,
            List<SimpleMethodInvocation> aspectInvocations) {

        Pointcut pointcut = getPointcut(aspectInvocations);
        Advice advice = new Advice(clazz, method);
        int order = getAspectOrder(aspectInvocations);
        AspectType type = getAspectType(aspectInvocations);

        return new Aspect(pointcut, advice, order, type);
    }

    private java.util.List<SimpleMethodInvocation> getAspectInvocations(JCMethodDecl method) {
        List<SimpleMethodInvocation> invocations =
                method.getBody().getStatements().stream()
                        .map(this::asMethodInvocation)
                        .flatMap(StreamUtil::optionalStream)
                        .collect(Collectors.toList());

        return invocations.stream()
                .filter(this::isAspectInvocation)
                .collect(Collectors.toList());
    }

    private Optional<SimpleMethodInvocation> asMethodInvocation(JCStatement statement) {
        return TreePasser.of(statement)
                .as(JCExpressionStatement.class)
                .map(JCExpressionStatement::getExpression)
                .as(JCMethodInvocation.class)
                .flatMapAndGet(SimpleMethodInvocation::of);
    }

    private boolean isAspectInvocation(SimpleMethodInvocation invocation) {
        return invocation.getFullyQualifiedClassName().contentEquals(ASPECT_CLASS_NAME);
    }

    private int getAspectOrder(List<SimpleMethodInvocation> invocations) {
        Optional<SimpleMethodInvocation> orderInvocation = invocations.stream()
                .filter(this::isOrderInvocation)
                .findAny();

        return orderInvocation.flatMap(this::getOrder)
                .orElse(Integer.MAX_VALUE);
    }

    private boolean isOrderInvocation(SimpleMethodInvocation invocation) {
        return invocation.getMethodName().contentEquals(ORDER_METHOD);
    }

    private Optional<Integer> getOrder(SimpleMethodInvocation invocation) {
        return TreePasser.of(invocation.getArguments().get(0))
                .as(JCLiteral.class)
                .mapAndGet(literal -> (int) literal.getValue());
    }

    private AspectType getAspectType(List<SimpleMethodInvocation> invocations) {
        Optional<SimpleMethodInvocation> aspectTypeInvocation = invocations.stream()
                .filter(this::isAspectTypeInvocation)
                .findAny();

        return aspectTypeInvocation.flatMap(this::doGetAspectType)
                .orElse(AspectType.SINGLETON);
    }

    private boolean isAspectTypeInvocation(SimpleMethodInvocation invocation) {
        return invocation.getMethodName().contentEquals(TYPE_METHOD);
    }

    private Optional<AspectType> doGetAspectType(SimpleMethodInvocation invocation) {
        return TreePasser.of(invocation.getArguments().get(0))
                .as(JCFieldAccess.class)
                .mapAndGet(JCFieldAccess::getIdentifier)
                .map(Object::toString)
                .map(AspectType::valueOf);
    }

    private Pointcut getPointcut(List<SimpleMethodInvocation> invocations) {
        Optional<SimpleMethodInvocation> pointcutInvocation = invocations.stream()
                .filter(this::isPointcutInvocation)
                .findAny();

        return pointcutInvocation.map(this::doGetPointcut)
                .orElseThrow(() -> new IllegalStateException(
                        "Advice method should contain at least pointcut definition"));
    }

    private boolean isPointcutInvocation(SimpleMethodInvocation invocation) {
        return invocation.getMethodName().contentEquals(POINTCUT_METHOD);
    }

    private Pointcut doGetPointcut(SimpleMethodInvocation invocation) {
        JCExpression pointcutExpr = (JCExpression) invocation.getArguments().get(0);
        return pointcutFactory.newPointcut(pointcutExpr);
    }

    private void removeAspectStatements(JCMethodDecl method) {
        List<JCStatement> stmtsWithoutAspects = method.getBody().getStatements().stream()
                .filter(statement -> !isAspectStatement(statement))
                .collect(Collectors.toList());

        method.getBody().stats = com.sun.tools.javac.util.List.from(stmtsWithoutAspects);
    }

    private boolean isAspectStatement(JCStatement statement) {
        return asMethodInvocation(statement).map(this::isAspectInvocation)
                .orElse(false);
    }
}
