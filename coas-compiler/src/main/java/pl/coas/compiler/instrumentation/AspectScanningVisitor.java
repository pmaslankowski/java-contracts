package pl.coas.compiler.instrumentation;

import javax.inject.Inject;
import javax.inject.Singleton;

import com.sun.source.tree.ClassTree;
import com.sun.source.util.TreeScanner;
import com.sun.tools.javac.comp.Attr;
import com.sun.tools.javac.tree.JCTree.JCClassDecl;

import pl.coas.compiler.instrumentation.util.AspectUtils;

@Singleton
public class AspectScanningVisitor extends TreeScanner<Void, Void> {

    private final AspectScanner scanner;
    private final Attr attributer;

    @Inject
    public AspectScanningVisitor(AspectScanner scanner, Attr attributer) {
        this.scanner = scanner;
        this.attributer = attributer;
    }

    @Override
    public Void visitClass(ClassTree classTree, Void aVoid) {
        JCClassDecl clazz = (JCClassDecl) classTree;
        // Attribution needs to be done here, because aspect scanning is performed when
        // ENTER phase ends. ContractScanner needs type information not only on method and class
        // declarations but also on expressions inside methods' bodies. Therefore we need to
        // perform one additional class attribution here
        attributer.attribClass(clazz.pos(), clazz.sym);

        if (AspectUtils.isAspectClass(classTree)) {
            scanner.scan(clazz);
        }
        return super.visitClass(classTree, aVoid);
    }
}
