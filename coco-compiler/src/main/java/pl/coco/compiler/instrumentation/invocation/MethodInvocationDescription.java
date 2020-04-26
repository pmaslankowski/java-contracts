package pl.coco.compiler.instrumentation.invocation;

import static com.sun.tools.javac.tree.JCTree.JCExpression;

import com.sun.tools.javac.code.Symbol;
import com.sun.tools.javac.util.List;

public class MethodInvocationDescription {

    private String className;
    private Symbol methodSymbol;
    private List<JCExpression> arguments;
    private int position;

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public Symbol getMethodSymbol() {
        return methodSymbol;
    }

    public void setMethodSymbol(Symbol methodSymbol) {
        this.methodSymbol = methodSymbol;
    }

    public List<JCExpression> getArguments() {
        return arguments;
    }

    public void setArguments(List<JCExpression> arguments) {
        this.arguments = arguments;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public static class Builder {

        MethodInvocationDescription description = new MethodInvocationDescription();

        public Builder withClassName(String className) {
            description.setClassName(className);
            return this;
        }

        public Builder withMethodSymbol(Symbol symbol) {
            description.setMethodSymbol(symbol);
            return this;
        }

        public Builder withArguments(List<JCExpression> arguments) {
            description.setArguments(arguments);
            return this;
        }

        public Builder withPosition(int position) {
            description.setPosition(position);
            return this;
        }

        public MethodInvocationDescription build() {
            return description;
        }
    }
}
