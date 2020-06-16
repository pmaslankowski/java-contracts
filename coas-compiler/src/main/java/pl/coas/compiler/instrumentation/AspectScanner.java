package pl.coas.compiler.instrumentation;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.inject.Inject;
import javax.inject.Singleton;

import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.tree.JCTree.JCClassDecl;
import com.sun.tools.javac.tree.JCTree.JCExpressionStatement;
import com.sun.tools.javac.tree.JCTree.JCMethodDecl;
import com.sun.tools.javac.tree.JCTree.JCMethodInvocation;
import com.sun.tools.javac.tree.JCTree.JCStatement;

import pl.coas.compiler.instrumentation.model.Advice;
import pl.coas.compiler.instrumentation.model.Aspect;
import pl.coas.compiler.instrumentation.model.pointcut.Pointcut;
import pl.coas.util.StreamUtil;
import pl.compiler.commons.model.SimpleMethodInvocation;
import pl.compiler.commons.util.TreePasser;

//TODO: wsparcie dla wyrażeń logicznych
@Singleton
public class AspectScanner {

    private static final String ASPECT_CLASS_NAME = "pl.coas.api.Aspect";

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
        java.util.List<SimpleMethodInvocation> aspectStatements = getAspectStatements(method);
        for (SimpleMethodInvocation invocation : aspectStatements) {
            Aspect aspect = getAspect(clazz, method, invocation);
            aspectRegistry.registerAspect(aspect);
        }
    }

    private java.util.List<SimpleMethodInvocation> getAspectStatements(JCMethodDecl method) {
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

    private Aspect getAspect(JCClassDecl clazz, JCMethodDecl method,
            SimpleMethodInvocation invocation) {

        Pointcut pointcut = getPointcut(invocation);
        Advice advice = new Advice(clazz, method);
        return new Aspect(pointcut, advice);
    }

    private Pointcut getPointcut(SimpleMethodInvocation invocation) {
        JCMethodInvocation pointcutExpr = (JCMethodInvocation) invocation.getArguments().get(0);
        SimpleMethodInvocation pointcutInvocation = SimpleMethodInvocation.of(pointcutExpr)
                .orElseThrow(() -> new IllegalStateException(
                        "Pointcut argument should be a simple method invocation."));
        return pointcutFactory.newPointcut(pointcutInvocation);
    }
}
