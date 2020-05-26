package pl.coco.compiler.annotation;

public class ContractAnnotation {

    private final String expr;
    private final int pos;
    private final ContractAnnotationType type;

    public ContractAnnotation(String expr, int pos, ContractAnnotationType type) {
        this.expr = expr;
        this.pos = pos;
        this.type = type;
    }

    public String getExpr() {
        return expr;
    }

    public int getPos() {
        return pos;
    }

    public ContractAnnotationType getType() {
        return type;
    }

    public String getCorrespondingMethod() {
        return type.getCorrespondingMethod();
    }
}
