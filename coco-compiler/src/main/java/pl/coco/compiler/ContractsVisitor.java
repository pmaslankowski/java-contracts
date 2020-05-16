package pl.coco.compiler;

import javax.inject.Inject;
import javax.inject.Singleton;

import com.sun.source.tree.ClassTree;
import com.sun.source.tree.CompilationUnitTree;
import com.sun.source.tree.MethodTree;
import com.sun.source.util.TreeScanner;

import pl.coco.compiler.instrumentation.ContractProcessor;
import pl.coco.compiler.instrumentation.ContractScanner;
import pl.coco.compiler.instrumentation.synthetic.MethodInput;
import pl.coco.compiler.validation.ContractValidator;
import pl.coco.compiler.validation.ValidationInput;

@Singleton
public class ContractsVisitor extends TreeScanner<Void, Void> {

    private final ContractValidator validator;
    private final ContractScanner scanner;
    private final ContractProcessor processor;

    private CompilationUnitTree compilationUnit;
    private ClassTree clazz;

    @Inject
    public ContractsVisitor(ContractValidator validator, ContractScanner scanner,
            ContractProcessor processor) {
        this.validator = validator;
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
        return super.visitClass(clazz, aVoid);
    }

    @Override
    public Void visitMethod(MethodTree method, Void aVoid) {
        MethodInput input = new MethodInput.Builder()
                .withCompilationUnit(compilationUnit)
                .withClazz(clazz)
                .withMethod(method)
                .build();

        if (!validator.isValid(ValidationInput.of(input))) {
            return super.visitMethod(method, aVoid);
        }

        scanner.scan(input);
        processor.process(input);

        return super.visitMethod(method, aVoid);
    }
}
