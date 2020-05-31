package pl.coco.compiler.instrumentation;

import javax.inject.Inject;
import javax.inject.Singleton;

import com.sun.source.tree.ClassTree;
import com.sun.source.tree.CompilationUnitTree;
import com.sun.source.tree.MethodTree;
import com.sun.source.util.TreeScanner;
import com.sun.tools.javac.comp.Attr;
import com.sun.tools.javac.tree.JCTree.JCClassDecl;

@Singleton
public class ContractScanningVisitor extends TreeScanner<Void, Void> {

    private final ContractScanner scanner;

    private CompilationUnitTree compilationUnit;
    private JCClassDecl clazz;
    private Attr attributer;

    @Inject
    public ContractScanningVisitor(ContractScanner scanner, Attr attributer) {
        this.scanner = scanner;
        this.attributer = attributer;
    }

    @Override
    public Void visitCompilationUnit(CompilationUnitTree compilationUnitTree, Void aVoid) {
        compilationUnit = compilationUnitTree;
        return super.visitCompilationUnit(compilationUnitTree, aVoid);
    }

    @Override
    public Void visitClass(ClassTree clazz, Void aVoid) {
        this.clazz = (JCClassDecl) clazz;
        // Attribution needs to be done here, because contract scanning is performed when
        // ENTER phase ends. ContractScanner needs type information not only on method and class
        // declarations but also on expressions inside methods' bodies. Therefore we need to
        // perform one additional class attribution here
        attributer.attribClass(this.clazz.pos(), this.clazz.sym);
        return super.visitClass(clazz, aVoid);
    }

    @Override
    public Void visitMethod(MethodTree method, Void aVoid) {
        MethodInput input = new MethodInput(compilationUnit, clazz, method);
        scanner.scan(input);
        return null;
    }
}
