package pl.coas.compiler.validation;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.tools.Diagnostic;

import com.sun.source.util.Trees;

@Singleton
public class ErrorProducer {

    private final Trees trees;

    @Inject
    public ErrorProducer(Trees trees) {
        this.trees = trees;
    }

    public void produceErrorMessage(AspectValidationException ex) {
        trees.printMessage(Diagnostic.Kind.ERROR, ex.getMessage(), ex.getOffending(),
                ex.getCompilationUnit());
    }
}
