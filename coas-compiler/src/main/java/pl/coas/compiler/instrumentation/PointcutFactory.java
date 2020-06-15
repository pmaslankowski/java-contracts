package pl.coas.compiler.instrumentation;

import java.util.List;
import java.util.stream.Collectors;

import javax.inject.Inject;
import javax.inject.Singleton;

import com.sun.tools.javac.tree.JCTree.JCExpression;
import com.sun.tools.javac.tree.JCTree.JCFieldAccess;
import com.sun.tools.javac.tree.JCTree.JCLiteral;
import com.sun.tools.javac.util.Name;

import pl.coas.compiler.instrumentation.model.pointcut.AnnotatedArgsPointcut;
import pl.coas.compiler.instrumentation.model.pointcut.AnnotatedMethodPointcut;
import pl.coas.compiler.instrumentation.model.pointcut.AnnotatedTypePointcut;
import pl.coas.compiler.instrumentation.model.pointcut.ArgsPointcut;
import pl.coas.compiler.instrumentation.model.pointcut.DynamicTargetPointcut;
import pl.coas.compiler.instrumentation.model.pointcut.MethodPointcut;
import pl.coas.compiler.instrumentation.model.pointcut.Pointcut;
import pl.coas.compiler.instrumentation.model.pointcut.StaticTargetPointcut;
import pl.coas.compiler.instrumentation.parsers.TypeNamesParserImpl;
import pl.coas.compiler.instrumentation.parsers.ClassNameParserImpl;
import pl.coas.compiler.instrumentation.parsers.DynamicTargetPointcutParser;
import pl.coas.compiler.instrumentation.parsers.MethodPointcutParserImpl;
import pl.coas.compiler.instrumentation.parsers.StaticTargetPointcutParser;
import pl.compiler.commons.model.SimpleMethodInvocation;

@Singleton
public class PointcutFactory {

    private final MethodPointcutParserImpl methodPointcutParser;
    private final StaticTargetPointcutParser staticTargetPointcutParser;
    private final DynamicTargetPointcutParser dynamicPointcutParser;
    private final ClassNameParserImpl classNameParser;
    private final TypeNamesParserImpl argsPointcutParser;

    @Inject
    public PointcutFactory(MethodPointcutParserImpl methodPointcutParser,
            StaticTargetPointcutParser staticTargetPointcutParser,
            DynamicTargetPointcutParser dynamicPointcutParser,
            ClassNameParserImpl classNameParser, TypeNamesParserImpl argsPointcutParser) {
        this.methodPointcutParser = methodPointcutParser;
        this.staticTargetPointcutParser = staticTargetPointcutParser;
        this.dynamicPointcutParser = dynamicPointcutParser;
        this.classNameParser = classNameParser;
        this.argsPointcutParser = argsPointcutParser;
    }

    public Pointcut newPointcut(SimpleMethodInvocation invocation) {
        Name methodName = invocation.getMethodName();
        if (methodName.contentEquals("method")) {
            return newMethodPointcut(invocation);
        }
        if (methodName.contentEquals("staticTarget")) {
            return newStaticTargetPointcut(invocation);
        }
        if (methodName.contentEquals("dynamicTarget")) {
            return newDynamicTargetPointcut(invocation);
        }
        if (methodName.contentEquals("args")) {
            return newArgsPointcut(invocation);
        }
        if (methodName.contentEquals("annotation")) {
            return newAnnotatedMethodPointcut(invocation);
        }
        if (methodName.contentEquals("annotatedType")) {
            return newAnnotatedTypePointcut(invocation);
        }
        if (methodName.contentEquals("annotatedArgs")) {
            return newAnnotatedArgsPointcut(invocation);
        }
        throw new IllegalArgumentException(
                "Method invocation " + invocation + " is not a pointcut definition");
    }

    private MethodPointcut newMethodPointcut(SimpleMethodInvocation invocation) {
        JCLiteral expr = (JCLiteral) invocation.getArguments().get(0);
        return methodPointcutParser.parse((String) expr.getValue());
    }

    private StaticTargetPointcut newStaticTargetPointcut(SimpleMethodInvocation invocation) {
        JCExpression expr = (JCExpression) invocation.getArguments().get(0);
        if (expr instanceof JCFieldAccess) {
            String className = getClassName((JCFieldAccess) expr);
            return new StaticTargetPointcut(className);
        } else if (expr instanceof JCLiteral) {
            Object pointcutExpr = ((JCLiteral) expr).getValue();
            return staticTargetPointcutParser.parse((String) pointcutExpr);
        } else {
            throw new IllegalArgumentException("Method invocation " + invocation
                    + " does not represent static target pointcut");
        }
    }

    private String getClassName(JCFieldAccess classAccess) {
        return classAccess.type.getTypeArguments().get(0).tsym.getQualifiedName().toString();
    }

    private DynamicTargetPointcut newDynamicTargetPointcut(SimpleMethodInvocation invocation) {
        JCExpression expr = (JCExpression) invocation.getArguments().get(0);
        if (expr instanceof JCFieldAccess) {
            String className = getClassName((JCFieldAccess) expr);
            return new DynamicTargetPointcut(className);
        } else if (expr instanceof JCLiteral) {
            Object pointcutExpr = ((JCLiteral) expr).getValue();
            return dynamicPointcutParser.parse((String) pointcutExpr);
        } else {
            throw new IllegalArgumentException("Method invocation " + invocation
                    + " does not represent dynamic target pointcut");
        }
    }

    private ArgsPointcut newArgsPointcut(SimpleMethodInvocation invocation) {
        JCExpression expr = (JCExpression) invocation.getArguments().get(0);
        if (expr instanceof JCFieldAccess) {
            List<String> argTypes = invocation.getArguments().stream()
                    .map(type -> getClassName((JCFieldAccess) expr))
                    .collect(Collectors.toList());
            return new ArgsPointcut(argTypes);
        } else if (expr instanceof JCLiteral) {
            Object pointcutExpr = ((JCLiteral) expr).getValue();
            List<String> argTypes = argsPointcutParser.parse((String) pointcutExpr);
            return new ArgsPointcut(argTypes);
        } else {
            throw new IllegalArgumentException("Method invocation " + invocation
                    + " does not represent args pointcut");
        }
    }

    private AnnotatedMethodPointcut newAnnotatedMethodPointcut(SimpleMethodInvocation invocation) {
        JCExpression expr = (JCExpression) invocation.getArguments().get(0);
        if (expr instanceof JCFieldAccess) {
            String annotationName = getClassName((JCFieldAccess) expr);
            return new AnnotatedMethodPointcut(annotationName);
        } else if (expr instanceof JCLiteral) {
            Object pointcutExpression = ((JCLiteral) expr).getValue();
            String annotationName = classNameParser.parse((String) pointcutExpression);
            return new AnnotatedMethodPointcut(annotationName);
        } else {
            throw new IllegalArgumentException("Method invocation " + invocation
                    + " does not represent annoteted method pointcut");
        }
    }

    private AnnotatedTypePointcut newAnnotatedTypePointcut(SimpleMethodInvocation invocation) {
        JCExpression expr = (JCExpression) invocation.getArguments().get(0);
        if (expr instanceof JCFieldAccess) {
            String annotationName = getClassName((JCFieldAccess) expr);
            return new AnnotatedTypePointcut(annotationName);
        } else if (expr instanceof JCLiteral) {
            Object pointcutExpression = ((JCLiteral) expr).getValue();
            String annotationName = classNameParser.parse((String) pointcutExpression);
            return new AnnotatedTypePointcut(annotationName);
        } else {
            throw new IllegalArgumentException("Method invocation " + invocation
                    + " does not represent annoteted type pointcut");
        }
    }

    private AnnotatedArgsPointcut newAnnotatedArgsPointcut(SimpleMethodInvocation invocation) {
        JCExpression expr = (JCExpression) invocation.getArguments().get(0);
        if (expr instanceof JCFieldAccess) {
            List<String> annotationTypes = invocation.getArguments().stream()
                    .map(type -> getClassName((JCFieldAccess) expr))
                    .collect(Collectors.toList());
            return new AnnotatedArgsPointcut(annotationTypes);
        } else if (expr instanceof JCLiteral) {
            Object pointcutExpr = ((JCLiteral) expr).getValue();
            List<String> annotationTypes = argsPointcutParser.parse((String) pointcutExpr);
            return new AnnotatedArgsPointcut(annotationTypes);
        } else {
            throw new IllegalArgumentException("Method invocation " + invocation
                    + " does not represent args pointcut");
        }
    }
}
