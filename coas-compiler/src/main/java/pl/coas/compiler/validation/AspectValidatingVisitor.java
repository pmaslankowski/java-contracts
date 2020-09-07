package pl.coas.compiler.validation;

import javax.inject.Inject;
import javax.inject.Singleton;

import com.sun.source.tree.ClassTree;
import com.sun.source.tree.CompilationUnitTree;
import com.sun.source.tree.MethodTree;
import com.sun.source.util.TreeScanner;

@Singleton
public class AspectValidatingVisitor extends TreeScanner<Boolean, Void> {

    private final AspectValidator validator;

    private CompilationUnitTree compilationUnit;

    @Inject
    public AspectValidatingVisitor(AspectValidator validator) {
        this.validator = validator;
    }

    @Override
    public Boolean visitCompilationUnit(CompilationUnitTree compilationUnitTree, Void aVoid) {
        compilationUnit = compilationUnitTree;
        return super.visitCompilationUnit(compilationUnitTree, null);
    }

    @Override
    public Boolean visitClass(ClassTree clazz, Void aVoid) {
        boolean isValid = validator.isClassValid(new ClassValidationInput(compilationUnit, clazz));
        if (!isValid) {
            return false;
        }

        return super.visitClass(clazz, null);
    }

    @Override
    public Boolean visitMethod(MethodTree method, Void aVoid) {
        return validator.isMethodValid(new MethodValidationInput(compilationUnit, method));
    }
}
