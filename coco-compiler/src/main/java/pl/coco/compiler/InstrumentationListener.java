package pl.coco.compiler;

import javax.inject.Inject;

import com.sun.source.util.TaskEvent;
import com.sun.source.util.TaskListener;

import pl.coco.compiler.validation.ContractValidatingVisitor;

public class InstrumentationListener implements TaskListener {

    private final ContractValidatingVisitor validatingVisitor;
    private final ContractProcessingVisitor contractProcessingVisitor;

    @Inject
    public InstrumentationListener(ContractValidatingVisitor validatingVisitor,
            ContractProcessingVisitor contractProcessingVisitor) {
        this.validatingVisitor = validatingVisitor;
        this.contractProcessingVisitor = contractProcessingVisitor;
    }

    @Override
    public void started(TaskEvent taskEvent) {

    }

    @Override
    public void finished(TaskEvent taskEvent) {
        if (taskEvent.getKind() == TaskEvent.Kind.ANALYZE) {
            boolean isValid = taskEvent.getCompilationUnit().accept(validatingVisitor, null);
            if (isValid) {
                taskEvent.getCompilationUnit().accept(contractProcessingVisitor, null);
            }
        }
    }
}
