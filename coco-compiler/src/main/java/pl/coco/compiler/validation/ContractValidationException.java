package pl.coco.compiler.validation;

import com.sun.source.tree.CompilationUnitTree;
import com.sun.source.tree.Tree;

public class ContractValidationException extends RuntimeException {

    private final ContractError error;
    private final Tree offending;
    private final CompilationUnitTree compilationUnit;

    public ContractValidationException(ContractError error, Tree offending,
            CompilationUnitTree compilationUnit) {

        super(error.getMessage());
        this.error = error;
        this.offending = offending;
        this.compilationUnit = compilationUnit;
    }

    public ContractError getError() {
        return error;
    }

    public Tree getOffending() {
        return offending;
    }

    public CompilationUnitTree getCompilationUnit() {
        return compilationUnit;
    }
}
