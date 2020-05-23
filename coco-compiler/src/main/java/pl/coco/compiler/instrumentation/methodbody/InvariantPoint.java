package pl.coco.compiler.instrumentation.methodbody;

public enum InvariantPoint {

    BEFORE(true), AFTER(false);

    private final boolean isBefore;

    InvariantPoint(boolean isBefore) {
        this.isBefore = isBefore;
    }

    public boolean isBefore() {
        return isBefore;
    }
}
