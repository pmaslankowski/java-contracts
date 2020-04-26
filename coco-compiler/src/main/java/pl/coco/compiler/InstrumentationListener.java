package pl.coco.compiler;

import javax.inject.Inject;

import com.sun.source.util.TaskEvent;
import com.sun.source.util.TaskListener;

import pl.coco.compiler.instrumentation.ContractsInstrumentationVisitor;

public class InstrumentationListener implements TaskListener {

    private final ContractsInstrumentationVisitor visitor;

    @Inject
    public InstrumentationListener(ContractsInstrumentationVisitor visitor) {
        this.visitor = visitor;
    }

    @Override
    public void started(TaskEvent taskEvent) {

    }

    @Override
    public void finished(TaskEvent taskEvent) {
        if (taskEvent.getKind() == TaskEvent.Kind.ANALYZE) {
            taskEvent.getCompilationUnit().accept(visitor, null);
        }
    }
}
