package pl.coco.compiler.annotation;

public enum ContractAnnotationType {

    REQUIRES("Requires", "pl.coco.api.code.Contract.requires"),
    ENSURES("Ensures", "pl.coco.api.code.Contract.ensures"),
    INVARIANT("ClassInvariant", "pl.coco.api.code.Contract.classInvariant");

    private final String name;
    private final String correspondingMethod;

    ContractAnnotationType(String name, String correspondingMethod) {
        this.name = name;
        this.correspondingMethod = correspondingMethod;
    }

    public String getName() {
        return name;
    }

    public String getCorrespondingMethod() {
        return correspondingMethod;
    }
}
