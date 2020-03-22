package pl.coco.compiler;

public enum ContractType {
    REQUIRES("requires", "Contract", "pl.coco.internal.ContractEngine"),
    ENSURES("ensures", "Contract", "pl.coco.internal.ContractEngine");

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

