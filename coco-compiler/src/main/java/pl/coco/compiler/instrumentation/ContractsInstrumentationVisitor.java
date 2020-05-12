package pl.coco.compiler.instrumentation;

import java.util.List;
import java.util.Optional;

import javax.inject.Inject;

import com.sun.source.tree.ClassTree;
import com.sun.source.tree.MethodTree;
import com.sun.source.tree.Tree;
import com.sun.source.util.TreeScanner;

import pl.coco.compiler.instrumentation.synthetic.MethodInput;
import pl.coco.compiler.util.TreePasser;

public class ContractsInstrumentationVisitor extends TreeScanner<Void, Void> {

    private final ContractProcessor processor;

    @Inject
    public ContractsInstrumentationVisitor(ContractProcessor processor) {
        this.processor = processor;
    }

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
                .withClazz(clazz)
                .withMethod(method)
                .build();
        processor.process(input);
    }
}
