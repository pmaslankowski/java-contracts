package pl.coco.compiler.validation;

import static com.sun.tools.javac.tree.JCTree.JCMethodDecl;

import java.util.List;

import com.sun.source.tree.CompilationUnitTree;
import com.sun.source.tree.StatementTree;

import pl.coco.compiler.instrumentation.synthetic.MethodInput;

public class ValidationInput {

    private final CompilationUnitTree compilationUnit;
    private final JCMethodDecl method;
    private final List<? extends StatementTree> statements;

    public ValidationInput(CompilationUnitTree compilationUnit, JCMethodDecl method) {
        this.compilationUnit = compilationUnit;
        this.method = method;
        this.statements = method.getBody().getStatements();
    }

    public static ValidationInput of(MethodInput input) {
        return new ValidationInput(input.getCompilationUnit(), (JCMethodDecl) input.getMethod());
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
