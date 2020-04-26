package pl.coco.compiler.instrumentation;

import com.sun.source.tree.ClassTree;
import com.sun.source.tree.MethodTree;

public class ContractProcessorInput {

    private ClassTree clazz;
    private MethodTree method;

    public ClassTree getClazz() {
        return clazz;
    }

    public void setClazz(ClassTree clazz) {
        this.clazz = clazz;
    }

    public MethodTree getMethod() {
        return method;
    }

    public void setMethod(MethodTree method) {
        this.method = method;
    }

    public static class Builder {

        private final ContractProcessorInput input = new ContractProcessorInput();

        public Builder withClazz(ClassTree clazz) {
            input.setClazz(clazz);
            return this;
        }

        public Builder withMethod(MethodTree method) {
            input.setMethod(method);
            return this;
        }

        public ContractProcessorInput build() {
            return input;
        }
    }
}
