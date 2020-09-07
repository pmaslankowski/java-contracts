package pl.coas.compiler.validation;

import com.sun.source.tree.CompilationUnitTree;
import com.sun.source.tree.Tree;

public class AspectValidationException extends RuntimeException {

    private final AspectError error;
    private final Tree offending;
    private final CompilationUnitTree compilationUnit;

    public AspectValidationException(AspectError error, Tree offending,
            CompilationUnitTree compilationUnit) {

        super(error.getMessage());
        this.error = error;
        this.offending = offending;
        this.compilationUnit = compilationUnit;
    }

    public AspectError getError() {
        return error;
    }

    public Tree getOffending() {
        return offending;
    }

    public CompilationUnitTree getCompilationUnit() {
        return compilationUnit;
    }
}
