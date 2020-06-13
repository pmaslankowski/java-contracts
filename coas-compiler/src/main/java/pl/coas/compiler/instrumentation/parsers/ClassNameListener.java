package pl.coas.compiler.instrumentation.parsers;

import pl.coas.compiler.instrumentation.antlr.ClassNameBaseListener;
import pl.coas.compiler.instrumentation.antlr.ClassNameParser;

public class ClassNameListener extends ClassNameBaseListener {

    private String className;

    @Override
    public void enterClassName(ClassNameParser.ClassNameContext ctx) {
        className = ctx.getText();
        super.enterClassName(ctx);
    }

    public String getClassName() {
        return className;
    }
}
