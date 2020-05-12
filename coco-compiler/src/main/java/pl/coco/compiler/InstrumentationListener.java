package pl.coco.compiler;

import javax.inject.Inject;

import com.sun.source.util.TaskEvent;
import com.sun.source.util.TaskListener;

public class InstrumentationListener implements TaskListener {

    private final ContractsVisitor contractsVisitor;

    @Inject
    public InstrumentationListener(ContractsVisitor contractsVisitor) {
        this.contractsVisitor = contractsVisitor;
    }

    @Override
    public void started(TaskEvent taskEvent) {

    }

    @Override
    public void finished(TaskEvent taskEvent) {
        if (taskEvent.getKind() == TaskEvent.Kind.ANALYZE) {
            taskEvent.getCompilationUnit().accept(contractsVisitor, null);
        }
    }
}
