package pl.coco.compiler.validation;

import javax.inject.Inject;
import javax.tools.Diagnostic;

import com.sun.source.tree.CompilationUnitTree;
import com.sun.source.tree.Tree;
import com.sun.source.util.Trees;

public class ErrorProducer {

    private final CompilationUnitTree compilationUnit;
    private final Trees trees;

    private int errorCount = 0;

    @Inject
    public ErrorProducer(CompilationUnitTree compilationUnit, Trees trees) {
        this.compilationUnit = compilationUnit;
        this.trees = trees;
    }

    public void raiseError(ContractError error, Tree offending) {
        errorCount++;
        trees.printMessage(Diagnostic.Kind.ERROR, error.getMessage(), offending, compilationUnit);
    }

    public int getErrorCount() {
        return errorCount;
    }
}
