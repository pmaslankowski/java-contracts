package pl.coco.compiler.annotation;

import java.util.HashSet;
import java.util.Set;

import javax.inject.Inject;
import javax.inject.Singleton;

import com.sun.tools.javac.tree.JCTree.JCCompilationUnit;
import com.sun.tools.javac.tree.JCTree.JCExpression;
import com.sun.tools.javac.tree.TreeMaker;

import pl.coco.compiler.annotation.util.SimpleAccessBuilder;

@Singleton
public class ContractImportAdder {

    private static final String CONTRACT_PATH = "pl.coco.api.code.Contract";
    private static final String INVARIANT_METHOD_PATH = "pl.coco.api.code.InvariantMethod";

    private final TreeMaker treeMaker;
    private final SimpleAccessBuilder accessBuilder;

    private final Set<String> sourceFileNamesWithImportAdded = new HashSet<>();

    @Inject
    public ContractImportAdder(TreeMaker treeMaker, SimpleAccessBuilder accessBuilder) {
        this.treeMaker = treeMaker;
        this.accessBuilder = accessBuilder;
    }

    public void addImportToCompilationUnit(JCCompilationUnit compilationUnit) {
        String sourceFileName = compilationUnit.sourcefile.getName();
        if (!sourceFileNamesWithImportAdded.contains(sourceFileName)) {
            doAddImportToCompilationUnit(compilationUnit);
            sourceFileNamesWithImportAdded.add(sourceFileName);
        }
    }

    private void doAddImportToCompilationUnit(JCCompilationUnit compilationUnit) {
        JCExpression contractAccess = getAccess(compilationUnit, CONTRACT_PATH);
        JCExpression invariantMethodAccess = getAccess(compilationUnit, INVARIANT_METHOD_PATH);
        compilationUnit.defs = compilationUnit.defs
                .prepend(treeMaker.Import(contractAccess, false))
                .prepend(treeMaker.Import(invariantMethodAccess, false));
    }

    private JCExpression getAccess(JCCompilationUnit compilationUnit, String accessPath) {
        return accessBuilder.build(accessPath, compilationUnit.pos);
    }
}
