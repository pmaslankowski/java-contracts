package pl.coas.compiler.instrumentation.parsers;

import java.util.ArrayList;
import java.util.List;

import pl.coas.compiler.instrumentation.antlr.TypeNamesBaseListener;
import pl.coas.compiler.instrumentation.antlr.TypeNamesParser;

public class TypeNamesListener extends TypeNamesBaseListener {

    private final List<String> args = new ArrayList<>();

    @Override
    public void enterArg(TypeNamesParser.ArgContext ctx) {
        args.add(ctx.getText());
        super.enterArg(ctx);
    }

    public List<String> getArgs() {
        return args;
    }
}
