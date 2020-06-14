package pl.coas.compiler.instrumentation.parsers;

import java.util.ArrayList;
import java.util.List;

import pl.coas.compiler.instrumentation.antlr.MethodPointcutBaseListener;
import pl.coas.compiler.instrumentation.antlr.MethodPointcutParser;

public class MethodPointcutListener extends MethodPointcutBaseListener {

    private String kind;
    private List<String> modifiers = new ArrayList<>();
    private String returnType;
    private String className;
    private String methodName;
    private List<String> args = new ArrayList<>();
    private List<String> exceptions = new ArrayList<>();

    @Override
    public void enterKind(MethodPointcutParser.KindContext ctx) {
        kind = ctx.getText();
        super.enterKind(ctx);
    }

    @Override
    public void enterModifier(MethodPointcutParser.ModifierContext ctx) {
        modifiers.add(ctx.getText());
        super.enterModifier(ctx);
    }

    @Override
    public void enterReturnType(MethodPointcutParser.ReturnTypeContext ctx) {
        returnType = ctx.getText();
        super.enterReturnType(ctx);
    }

    @Override
    public void enterClassName(MethodPointcutParser.ClassNameContext ctx) {
        className = ctx.getText();
        super.enterClassName(ctx);
    }

    @Override
    public void enterMethodName(MethodPointcutParser.MethodNameContext ctx) {
        methodName = ctx.getText();
        super.enterMethodName(ctx);
    }

    @Override
    public void enterArgWildcard(MethodPointcutParser.ArgWildcardContext ctx) {
        args.add(ctx.getText());
        super.enterArgWildcard(ctx);
    }

    @Override
    public void enterArg(MethodPointcutParser.ArgContext ctx) {
        args.add(ctx.getText());
        super.enterArg(ctx);
    }

    @Override
    public void enterException(MethodPointcutParser.ExceptionContext ctx) {
        exceptions.add(ctx.getText());
        super.enterException(ctx);
    }

    public String getKind() {
        return kind;
    }

    public List<String> getModifiers() {
        return modifiers;
    }

    public String getReturnType() {
        return returnType;
    }

    public String getClassName() {
        return className;
    }

    public String getMethodName() {
        return methodName;
    }

    public List<String> getArgs() {
        return args;
    }

    public List<String> getExceptions() {
        return exceptions;
    }
}
