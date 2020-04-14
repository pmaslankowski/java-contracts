package pl.coco.compiler;

public enum ContractType {
    REQUIRES("requires", "pl.coco.api.Contract", "pl.coco.internal.ContractEngine"),
    ENSURES("ensures", "pl.coco.api.Contract", "pl.coco.internal.ContractEngine");

    private String methodName;
    private String apiClassName;
    private String internalClassName;

    ContractType(String methodName, String apiClassName, String internalClassName) {
        this.methodName = methodName;
        this.apiClassName = apiClassName;
        this.internalClassName = internalClassName;
    }

    public String getMethodName() {
        return methodName;
    }

    public String getApiClassName() {
        return apiClassName;
    }

    public String getInternalClassName() {
        return internalClassName;
    }
}

