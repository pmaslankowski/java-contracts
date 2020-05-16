package pl.coco.compiler.validation;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.tools.Diagnostic;

import com.sun.source.tree.CompilationUnitTree;
import com.sun.source.tree.Tree;
import com.sun.source.util.Trees;

@Singleton
public class ErrorProducer {

    private final Trees trees;

    @Inject
    public ErrorProducer(Trees trees) {
        this.trees = trees;
    }

    public void raiseError(ContractError error, Tree offending,
            CompilationUnitTree compilationUnit) {

        trees.printMessage(Diagnostic.Kind.ERROR, error.getMessage(), offending, compilationUnit);
        throw new ContractValidationException(error);
    }
}
