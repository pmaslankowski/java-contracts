package pl.coco.internal;

@FunctionalInterface
public interface ConditionSupplier {

    boolean get() throws Exception;
}
