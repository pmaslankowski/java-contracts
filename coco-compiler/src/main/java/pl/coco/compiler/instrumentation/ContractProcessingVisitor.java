package pl.coco.compiler.instrumentation;

import javax.inject.Inject;
import javax.inject.Singleton;

import com.sun.source.tree.ClassTree;
import com.sun.source.tree.CompilationUnitTree;
import com.sun.source.tree.MethodTree;
import com.sun.source.util.TreeScanner;

@Singleton
public class ContractProcessingVisitor extends TreeScanner<Void, Void> {

    private final ClassLevelProcessor classProcessor;
    private final MethodLevelProcessor methodProcessor;

    private CompilationUnitTree compilationUnit;
    private ClassTree clazz;

    @Inject
    public ContractProcessingVisitor(ClassLevelProcessor classProcessor,
            MethodLevelProcessor methodProcessor) {
        this.classProcessor = classProcessor;
        this.methodProcessor = methodProcessor;
    }

    @Override
    public Void visitCompilationUnit(CompilationUnitTree compilationUnitTree, Void aVoid) {
        compilationUnit = compilationUnitTree;
        return super.visitCompilationUnit(compilationUnitTree, aVoid);
    }

    @Override
    public Void visitClass(ClassTree clazz, Void aVoid) {
        this.clazz = clazz;
        classProcessor.process(new ClassInput(compilationUnit, clazz));
        return super.visitClass(clazz, aVoid);
    }

    @Override
    public Void visitMethod(MethodTree method, Void aVoid) {
        MethodInput input = new MethodInput(compilationUnit, clazz, method);

        methodProcessor.process(input);

        return null;
    }
}
