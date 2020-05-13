package pl.coco.compiler.validation;

import javax.inject.Inject;
import javax.inject.Singleton;

import com.sun.source.tree.CompilationUnitTree;
import com.sun.source.util.Trees;
import com.sun.tools.javac.tree.JCTree.JCMethodDecl;

import pl.coco.compiler.instrumentation.synthetic.MethodInput;

@Singleton
public class ContractValidatorFactory {

    private final Trees trees;

    @Inject
    public ContractValidatorFactory(Trees trees) {
        this.trees = trees;
    }

    public ContractValidator create(MethodInput input) {
        // TODO: maybe it can be done in a more elegant way?
        CompilationUnitTree compilationUnit = input.getCompilationUnit();
        JCMethodDecl method = (JCMethodDecl) input.getMethod();
        ErrorProducer errorProducer = new ErrorProducer(compilationUnit, trees);
        ContractResultValidator resultValidator =
                new ContractResultValidator(errorProducer, method);
        return new ContractValidator(errorProducer, resultValidator);
    }
}
