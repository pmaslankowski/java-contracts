package pl.coco.compiler;

import javax.inject.Inject;
import javax.inject.Singleton;

import com.sun.source.tree.ClassTree;
import com.sun.source.tree.CompilationUnitTree;
import com.sun.source.tree.MethodTree;
import com.sun.source.util.TreeScanner;

import pl.coco.compiler.instrumentation.ClassInput;
import pl.coco.compiler.instrumentation.ContractProcessor;
import pl.coco.compiler.instrumentation.ContractScanner;
import pl.coco.compiler.instrumentation.MethodInput;

@Singleton
public class ContractProcessingVisitor extends TreeScanner<Void, Void> {

    private final ContractScanner scanner;
    private final ContractProcessor processor;

    private CompilationUnitTree compilationUnit;
    private ClassTree clazz;

    @Inject
    public ContractProcessingVisitor(ContractScanner scanner, ContractProcessor processor) {
        this.scanner = scanner;
        this.processor = processor;
    }

    @Override
    public Void visitCompilationUnit(CompilationUnitTree compilationUnitTree, Void aVoid) {
        compilationUnit = compilationUnitTree;
        return super.visitCompilationUnit(compilationUnitTree, aVoid);
    }

    @Override
    public Void visitClass(ClassTree clazz, Void aVoid) {
        this.clazz = clazz;
        processor.processClass(new ClassInput(compilationUnit, clazz));
        return super.visitClass(clazz, aVoid);
    }

    @Override
    public Void visitMethod(MethodTree method, Void aVoid) {
        MethodInput input = new MethodInput.Builder()
                .withCompilationUnit(compilationUnit)
                .withClazz(clazz)
                .withMethod(method)
                .build();

        scanner.scan(input);
        processor.processMethod(input);

        return null;
    }
}
