package pl.coas.compiler.instrumentation.parsers;

import pl.coas.compiler.instrumentation.antlr.ClassPointcutBaseListener;
import pl.coas.compiler.instrumentation.antlr.ClassPointcutParser;

public class ClassNameListener extends ClassPointcutBaseListener {

    private String className;

    @Override
    public void enterClassName(ClassPointcutParser.ClassNameContext ctx) {
        className = ctx.getText();
        super.enterClassName(ctx);
    }

    public String getClassName() {
        return className;
    }
}
