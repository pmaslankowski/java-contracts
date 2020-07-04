package pl.coas.compiler.instrumentation.parsers;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import javax.inject.Singleton;

import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;

import pl.coas.compiler.instrumentation.antlr.PointcutLexer;
import pl.coas.compiler.instrumentation.antlr.PointcutParser;
import pl.coas.compiler.instrumentation.model.pointcut.AndPointcut;
import pl.coas.compiler.instrumentation.model.pointcut.AnnotatedArgsPointcut;
import pl.coas.compiler.instrumentation.model.pointcut.AnnotatedMethodPointcut;
import pl.coas.compiler.instrumentation.model.pointcut.AnnotatedTypePointcut;
import pl.coas.compiler.instrumentation.model.pointcut.ArgsPointcut;
import pl.coas.compiler.instrumentation.model.pointcut.MethodArguments;
import pl.coas.compiler.instrumentation.model.pointcut.MethodArgumentsWildcard;
import pl.coas.compiler.instrumentation.model.pointcut.MethodPointcut;
import pl.coas.compiler.instrumentation.model.pointcut.OrPointcut;
import pl.coas.compiler.instrumentation.model.pointcut.Pointcut;
import pl.coas.compiler.instrumentation.model.pointcut.RegularMethodArguments;
import pl.coas.compiler.instrumentation.model.pointcut.TargetPointcut;
import pl.coas.compiler.instrumentation.model.pointcut.WildcardString;

@Singleton
public class PointcutParserImpl extends BaseParser<Pointcut, PointcutLexer, PointcutParser> {

    @Override
    public Pointcut parse(String expression) {
        PointcutParser parser = doParse(expression);
        parser.getBuildParseTree();
        return parseRoot(parser.pointcutRule());
    }

    private Pointcut parseRoot(PointcutParser.PointcutRuleContext node) {
        return parseAlternatives(node.alternatives());
    }

    private Pointcut parseAlternatives(PointcutParser.AlternativesContext node) {
        Pointcut pointcut = parseConjunctions(node.conjunctions());
        if (node.alternatives() != null) {
            Pointcut others = parseAlternatives(node.alternatives());
            return new OrPointcut(pointcut, others);
        } else {
            return pointcut;
        }
    }

    private Pointcut parseConjunctions(PointcutParser.ConjunctionsContext node) {
        Pointcut pointcut = parseAtom(node.atom());
        if (node.conjunctions() != null) {
            Pointcut others = parseConjunctions(node.conjunctions());
            return new AndPointcut(pointcut, others);
        } else {
            return pointcut;
        }
    }

    private Pointcut parseAtom(PointcutParser.AtomContext node) {
        return parseAtomicRule(node.atomicRule());
    }

    private Pointcut parseAtomicRule(PointcutParser.AtomicRuleContext node) {
        if (node.methodRule() != null) {
            return parseMethodRule(node.methodRule());
        } else if (node.targetRule() != null) {
            return parseTargetRule(node.targetRule());
        } else if (node.argsRule() != null) {
            return parseArgsRule(node.argsRule());
        } else if (node.annotationRule() != null) {
            return parseAnnotationRule(node.annotationRule());
        } else if (node.annotatedTypeRule() != null) {
            return parseAnnotatedTypeRule(node.annotatedTypeRule());
        } else if (node.annotatedArgsRule() != null) {
            return parseAnnotatedArgsRule(node.annotatedArgsRule());
        } else {
            throw new IllegalStateException(
                    "Pointcut rule should be in one of the following form: " +
                            "method, target, args, annotation, annotatedType or annotatedArgs");
        }
    }

    private Pointcut parseMethodRule(PointcutParser.MethodRuleContext node) {
        return parseMethod(node.method());
    }

    private Pointcut parseMethod(PointcutParser.MethodContext node) {
        String kind = node.kind().getText();
        List<String> modifiers = parseModifiers(node.modifiers());
        String returnType = node.returnType().getText();
        String className = node.className().getText();
        String methodName = node.methodName().getText();
        MethodArguments args = parseArgs(node.args());
        List<String> exceptions = node.exceptions() != null ? parseExceptions(node.exceptions())
                : Collections.emptyList();

        return new MethodPointcut.Builder()
                .withKind(kind)
                .withModifiers(modifiers)
                .withReturnType(returnType)
                .withClassName(className)
                .withMethodName(methodName)
                .withArgumentTypes(args)
                .withExceptionsThrown(exceptions)
                .build();
    }

    private List<String> parseModifiers(PointcutParser.ModifiersContext node) {
        if (node.modifiers() != null) {
            List<String> res = parseModifiers(node.modifiers());
            res.add(0, node.modifier().getText());
            return res;
        } else if (node.modifier() != null) {
            LinkedList<String> res = new LinkedList<>();
            res.add(node.modifier().getText());
            return res;
        } else {
            return Collections.emptyList();
        }
    }

    private MethodArguments parseArgs(PointcutParser.ArgsContext node) {
        if (node.argWildcard() != null) {
            return new MethodArgumentsWildcard();
        } else {
            List<WildcardString> args = parseNonWildcardArgs(node).stream()
                    .map(WildcardString::new)
                    .collect(Collectors.toList());
            return new RegularMethodArguments(args);
        }
    }

    private List<String> parseNonWildcardArgs(PointcutParser.ArgsContext node) {
        if (node.args() != null) {
            List<String> args = parseNonWildcardArgs(node.args());
            args.add(0, node.arg().getText());
            return args;
        } else {
            List<String> res = new LinkedList<>();
            res.add(node.arg().getText());
            return res;
        }
    }

    private List<String> parseExceptions(PointcutParser.ExceptionsContext node) {
        if (node.exceptions() != null) {
            List<String> exceptions = parseExceptions(node.exceptions());
            exceptions.add(0, node.exception().getText());
            return exceptions;
        } else {
            LinkedList<String> res = new LinkedList<>();
            res.add(node.exception().getText());
            return res;
        }
    }

    private Pointcut parseTargetRule(PointcutParser.TargetRuleContext node) {
        return new TargetPointcut(node.className().getText());
    }

    private Pointcut parseArgsRule(PointcutParser.ArgsRuleContext node) {
        return new ArgsPointcut(parseArgs(node.args()));
    }

    private Pointcut parseAnnotationRule(PointcutParser.AnnotationRuleContext node) {
        return new AnnotatedMethodPointcut(node.className().getText());
    }

    private Pointcut parseAnnotatedTypeRule(PointcutParser.AnnotatedTypeRuleContext node) {
        return new AnnotatedTypePointcut(node.className().getText());
    }

    private Pointcut parseAnnotatedArgsRule(PointcutParser.AnnotatedArgsRuleContext node) {
        return new AnnotatedArgsPointcut(parseTypeNames(node.typeNames()));
    }

    private List<String> parseTypeNames(PointcutParser.TypeNamesContext node) {
        if (node.typeNames() != null) {
            List<String> typeNames = parseTypeNames(node.typeNames());
            typeNames.add(0, node.typeName().getText());
            return typeNames;
        } else {
            List<String> res = new LinkedList<>();
            res.add(node.typeName().getText());
            return res;
        }
    }

    @Override
    protected PointcutLexer getLexer(String expression) {
        return new PointcutLexer(CharStreams.fromString(expression));
    }

    @Override
    protected PointcutParser getParser(CommonTokenStream tokens) {
        return new PointcutParser(tokens);
    }
}
