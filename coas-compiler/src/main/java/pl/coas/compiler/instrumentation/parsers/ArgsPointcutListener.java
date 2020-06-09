package pl.coas.compiler.instrumentation.parsers;

import java.util.ArrayList;
import java.util.List;

import pl.coas.compiler.instrumentation.antlr.ArgsPointcutBaseListener;
import pl.coas.compiler.instrumentation.antlr.ArgsPointcutParser;

public class ArgsPointcutListener extends ArgsPointcutBaseListener {

    private final List<String> args = new ArrayList<>();

    @Override
    public void enterArg(ArgsPointcutParser.ArgContext ctx) {
        args.add(ctx.getText());
        super.enterArg(ctx);
    }

    public List<String> getArgs() {
        return args;
    }
}
