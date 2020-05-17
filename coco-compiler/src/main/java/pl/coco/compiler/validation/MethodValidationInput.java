package pl.coco.compiler.validation;

import static com.sun.tools.javac.tree.JCTree.JCMethodDecl;

import java.util.List;

import com.sun.source.tree.CompilationUnitTree;
import com.sun.source.tree.StatementTree;

import pl.coco.compiler.instrumentation.synthetic.MethodInput;

public class MethodValidationInput {

    private final CompilationUnitTree compilationUnit;
    private final JCMethodDecl method;
    private final List<? extends StatementTree> statements;

    public MethodValidationInput(CompilationUnitTree compilationUnit, JCMethodDecl method) {
        this.compilationUnit = compilationUnit;
        this.method = method;
        this.statements = method.getBody().getStatements();
    }

    public static MethodValidationInput of(MethodInput input) {
        return new MethodValidationInput(input.getCompilationUnit(),
                (JCMethodDecl) input.getMethod());
    }

    public CompilationUnitTree getCompilationUnit() {
        return compilationUnit;
    }

    public JCMethodDecl getMethod() {
        return method;
    }

    public List<? extends StatementTree> getStatements() {
        return statements;
    }

}
