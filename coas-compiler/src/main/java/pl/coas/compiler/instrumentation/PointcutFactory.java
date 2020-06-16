package pl.coas.compiler.instrumentation;

import java.util.List;
import java.util.stream.Collectors;

import javax.inject.Inject;
import javax.inject.Singleton;

import com.sun.source.tree.MethodInvocationTree;
import com.sun.tools.javac.tree.JCTree.JCBinary;
import com.sun.tools.javac.tree.JCTree.JCExpression;
import com.sun.tools.javac.tree.JCTree.JCFieldAccess;
import com.sun.tools.javac.tree.JCTree.JCLiteral;
import com.sun.tools.javac.tree.JCTree.JCMethodInvocation;
import com.sun.tools.javac.tree.JCTree.JCUnary;
import com.sun.tools.javac.tree.JCTree.Tag;
import com.sun.tools.javac.util.Name;

import pl.coas.compiler.instrumentation.model.pointcut.AndPointcut;
import pl.coas.compiler.instrumentation.model.pointcut.AnnotatedArgsPointcut;
import pl.coas.compiler.instrumentation.model.pointcut.AnnotatedMethodPointcut;
import pl.coas.compiler.instrumentation.model.pointcut.AnnotatedTypePointcut;
import pl.coas.compiler.instrumentation.model.pointcut.ArgsPointcut;
import pl.coas.compiler.instrumentation.model.pointcut.MethodPointcut;
import pl.coas.compiler.instrumentation.model.pointcut.NotPointcut;
import pl.coas.compiler.instrumentation.model.pointcut.OrPointcut;
import pl.coas.compiler.instrumentation.model.pointcut.Pointcut;
import pl.coas.compiler.instrumentation.model.pointcut.RegularMethodArguments;
import pl.coas.compiler.instrumentation.model.pointcut.TargetPointcut;
import pl.coas.compiler.instrumentation.model.pointcut.WildcardString;
import pl.coas.compiler.instrumentation.parsers.ClassNameParserImpl;
import pl.coas.compiler.instrumentation.parsers.MethodPointcutParserImpl;
import pl.coas.compiler.instrumentation.parsers.TargetPointcutParser;
import pl.coas.compiler.instrumentation.parsers.TypeNamesParserImpl;
import pl.compiler.commons.model.SimpleMethodInvocation;

@Singleton
public class PointcutFactory {

    private final MethodPointcutParserImpl methodPointcutParser;
    private final TargetPointcutParser targetPointcutParser;
    private final ClassNameParserImpl classNameParser;
    private final TypeNamesParserImpl argsPointcutParser;

    @Inject
    public PointcutFactory(MethodPointcutParserImpl methodPointcutParser,
            TargetPointcutParser targetPointcutParser,
            ClassNameParserImpl classNameParser, TypeNamesParserImpl argsPointcutParser) {
        this.methodPointcutParser = methodPointcutParser;
        this.targetPointcutParser = targetPointcutParser;
        this.classNameParser = classNameParser;
        this.argsPointcutParser = argsPointcutParser;
    }

    public Pointcut newPointcut(JCExpression expression) {
        if (expression instanceof JCMethodInvocation) {
            MethodInvocationTree invocation = (MethodInvocationTree) expression;
            SimpleMethodInvocation simpleInvocation = SimpleMethodInvocation.of(invocation)
                    .orElseThrow(() -> new IllegalStateException(
                            "Pointcut expression should be simple method invocation"));
            return newPointcutFromMethodInvocation(simpleInvocation);
        } else if (expression instanceof JCBinary) {
            JCBinary operator = (JCBinary) expression;
            return newPointcutFromBinaryOp(operator);
        } else if (expression instanceof JCUnary) {
            JCUnary operator = (JCUnary) expression;
            return newPointcutFromUnaryOp(operator);
        } else {
            throw new IllegalArgumentException(
                    "Pointcut expression has to be method invocation or logical expression");
        }
    }

    private Pointcut newPointcutFromMethodInvocation(SimpleMethodInvocation invocation) {
        Name methodName = invocation.getMethodName();
        if (methodName.contentEquals("method")) {
            return newMethodPointcut(invocation);
        }
        if (methodName.contentEquals("target")) {
            return newTargetPointcut(invocation);
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

    private TargetPointcut newTargetPointcut(SimpleMethodInvocation invocation) {
        JCExpression expr = (JCExpression) invocation.getArguments().get(0);
        if (expr instanceof JCFieldAccess) {
            String className = getClassName((JCFieldAccess) expr);
            return new TargetPointcut(className);
        } else if (expr instanceof JCLiteral) {
            Object pointcutExpr = ((JCLiteral) expr).getValue();
            return targetPointcutParser.parse((String) pointcutExpr);
        } else {
            throw new IllegalArgumentException("Method invocation " + invocation
                    + " does not represent dynamic target pointcut");
        }
    }

    private String getClassName(JCFieldAccess classAccess) {
        return classAccess.type.getTypeArguments().get(0).tsym.getQualifiedName().toString();
    }

    private ArgsPointcut newArgsPointcut(SimpleMethodInvocation invocation) {
        JCExpression expr = (JCExpression) invocation.getArguments().get(0);
        if (expr instanceof JCFieldAccess) {
            List<WildcardString> argTypes = invocation.getArguments().stream()
                    .map(type -> getClassName((JCFieldAccess) expr))
                    .map(WildcardString::new)
                    .collect(Collectors.toList());
            return new ArgsPointcut(new RegularMethodArguments(argTypes));
        } else if (expr instanceof JCLiteral) {
            Object pointcutExpr = ((JCLiteral) expr).getValue();
            List<WildcardString> argTypes = argsPointcutParser.parse((String) pointcutExpr).stream()
                    .map(WildcardString::new)
                    .collect(Collectors.toList());
            return new ArgsPointcut(new RegularMethodArguments(argTypes));
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

    private Pointcut newPointcutFromBinaryOp(JCBinary operator) {
        if (operator.getTag() == Tag.OR) {
            Pointcut leftOperand = newPointcut(operator.getLeftOperand());
            Pointcut rightOperand = newPointcut(operator.getRightOperand());
            return new OrPointcut(leftOperand, rightOperand);
        } else if (operator.getTag() == Tag.AND) {
            Pointcut leftOperand = newPointcut(operator.getLeftOperand());
            Pointcut rightOperand = newPointcut(operator.getRightOperand());
            return new AndPointcut(leftOperand, rightOperand);
        } else {
            throw new IllegalArgumentException(
                    "Binary pointcut expression has to be either logical AND or OR.");
        }
    }

    private Pointcut newPointcutFromUnaryOp(JCUnary operator) {
        if (operator.getTag() == Tag.NOT) {
            Pointcut operand = newPointcut(operator.getExpression());
            return new NotPointcut(operand);
        } else {
            throw new IllegalArgumentException(
                    "Unary pointcut expression has to be logical NOT.");
        }
    }
}
