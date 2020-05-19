package pl.coco.compiler.validation;

import static com.sun.tools.javac.tree.JCTree.JCMethodDecl;

import java.util.List;

import com.sun.source.tree.CompilationUnitTree;
import com.sun.source.tree.MethodTree;
import com.sun.source.tree.StatementTree;

public class MethodValidationInput {

    private final CompilationUnitTree compilationUnit;
    private final JCMethodDecl method;
    private final List<? extends StatementTree> statements;

    public MethodValidationInput(CompilationUnitTree compilationUnit, MethodTree method) {
        this.compilationUnit = compilationUnit;
        this.method = (JCMethodDecl) method;
        this.statements = method.getBody().getStatements();
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
