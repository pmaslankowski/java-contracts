package pl.coco.compiler.instrumentation.synthetic;

import com.sun.source.tree.ClassTree;
import com.sun.source.tree.CompilationUnitTree;
import com.sun.source.tree.MethodTree;

public class MethodInput {

    private CompilationUnitTree compilationUnit;
    private ClassTree clazz;
    private MethodTree method;

    public CompilationUnitTree getCompilationUnit() {
        return compilationUnit;
    }

    public void setCompilationUnit(CompilationUnitTree compilationUnit) {
        this.compilationUnit = compilationUnit;
    }

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

        private final MethodInput input = new MethodInput();

        public Builder withCompilationUnit(CompilationUnitTree compilationUnit) {
            input.setCompilationUnit(compilationUnit);
            return this;
        }

        public Builder withClazz(ClassTree clazz) {
            input.setClazz(clazz);
            return this;
        }

        public Builder withMethod(MethodTree method) {
            input.setMethod(method);
            return this;
        }

        public MethodInput build() {
            return input;
        }
    }
}
