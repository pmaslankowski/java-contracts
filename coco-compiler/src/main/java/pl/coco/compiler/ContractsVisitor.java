package pl.coco.compiler;

import java.util.List;
import java.util.Optional;

import javax.inject.Inject;
import javax.inject.Singleton;

import com.sun.source.tree.ClassTree;
import com.sun.source.tree.CompilationUnitTree;
import com.sun.source.tree.MethodTree;
import com.sun.source.tree.Tree;
import com.sun.source.util.TreeScanner;

import pl.coco.compiler.instrumentation.ContractProcessor;
import pl.coco.compiler.instrumentation.ContractScanner;
import pl.coco.compiler.instrumentation.synthetic.MethodInput;
import pl.coco.compiler.util.TreePasser;
import pl.coco.compiler.validation.ContractValidator;
import pl.coco.compiler.validation.ContractValidatorFactory;

@Singleton
public class ContractsVisitor extends TreeScanner<Void, Void> {

    private final ContractValidatorFactory validatorFactory;
    private final ContractScanner scanner;
    private final ContractProcessor processor;

    private CompilationUnitTree compilationUnit;

    @Inject
    public ContractsVisitor(ContractValidatorFactory validatorFactory, ContractScanner scanner,
            ContractProcessor processor) {
        this.validatorFactory = validatorFactory;
        this.scanner = scanner;
        this.processor = processor;
    }

    @Override
    public Void visitCompilationUnit(CompilationUnitTree compilationUnitTree, Void aVoid) {
        compilationUnit = compilationUnitTree;
        return super.visitCompilationUnit(compilationUnitTree, aVoid);
    }

    // TODO: przerobić, żeby zbierał dane o klasie tak jak zbiera dane o jednostce kompilacji
    @Override
    public Void visitClass(ClassTree clazz, Void aVoid) {
        List<? extends Tree> members = clazz.getMembers();
        for (Tree member : members) {
            Optional<MethodTree> method = TreePasser.of(member)
                    .as(MethodTree.class)
                    .get();

            method.ifPresent(methodDef -> processMethod(clazz, methodDef));
        }

        return super.visitClass(clazz, aVoid);
    }

    private void processMethod(ClassTree clazz, MethodTree method) {
        MethodInput input = new MethodInput.Builder()
                .withCompilationUnit(compilationUnit)
                .withClazz(clazz)
                .withMethod(method)
                .build();

        ContractValidator validator = validatorFactory.create(input);

        if (validator.validate(input) > 0) {
            return;
        }

        scanner.scan(input);
        processor.process(input);
    }
}
