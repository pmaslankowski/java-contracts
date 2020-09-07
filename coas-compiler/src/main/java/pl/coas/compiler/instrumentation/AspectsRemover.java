package pl.coas.compiler.instrumentation;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.inject.Inject;
import javax.inject.Singleton;

import com.sun.tools.javac.tree.JCTree.JCExpressionStatement;
import com.sun.tools.javac.tree.JCTree.JCMethodDecl;
import com.sun.tools.javac.tree.JCTree.JCMethodInvocation;
import com.sun.tools.javac.tree.JCTree.JCStatement;

import pl.coas.compiler.instrumentation.model.Aspect;
import pl.compiler.commons.model.SimpleMethodInvocation;
import pl.compiler.commons.util.TreePasser;

@Singleton
public class AspectsRemover {

    private static final String ASPECT_CLASS_NAME = "pl.coas.api.Aspect";

    private final AspectRegistry aspectRegistry;

    @Inject
    public AspectsRemover(AspectRegistry aspectRegistry) {
        this.aspectRegistry = aspectRegistry;
    }

    public void removeAspectStatements() {
        for (Aspect aspect : aspectRegistry.getAllAspects()) {
            JCMethodDecl method = aspect.getAdvice().getMethod();
            removeAspectStatements(method);
        }
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
}
