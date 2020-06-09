package pl.coas.compiler.validation;

import javax.inject.Singleton;

import com.sun.source.tree.CompilationUnitTree;
import com.sun.source.util.TreeScanner;

@Singleton
public class AspectValidatingVisitor extends TreeScanner<Boolean, Void> {

    @Override
    public Boolean visitCompilationUnit(CompilationUnitTree compilationUnitTree, Void aVoid) {
        // TODO: to be implemented
        return true;
    }
}
