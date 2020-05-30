package pl.coco.compiler.instrumentation;

import javax.inject.Inject;
import javax.inject.Singleton;

import com.sun.source.tree.ClassTree;
import com.sun.source.tree.CompilationUnitTree;
import com.sun.source.tree.MethodTree;
import com.sun.source.util.TreeScanner;

import pl.coco.compiler.instrumentation.ClassInput;
import pl.coco.compiler.instrumentation.ClassLevelProcessor;
import pl.coco.compiler.instrumentation.ContractScanner;
import pl.coco.compiler.instrumentation.MethodInput;
import pl.coco.compiler.instrumentation.MethodLevelProcessor;

@Singleton
public class ContractProcessingVisitor extends TreeScanner<Void, Void> {

    private final ContractScanner scanner;
    private final ClassLevelProcessor classProcessor;
    private final MethodLevelProcessor methodProcessor;

    private CompilationUnitTree compilationUnit;
    private ClassTree clazz;

    @Inject
    public ContractProcessingVisitor(ContractScanner scanner, ClassLevelProcessor classProcessor,
            MethodLevelProcessor methodProcessor) {
        this.scanner = scanner;
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

        scanner.scan(input);
        methodProcessor.process(input);

        return null;
    }
}
