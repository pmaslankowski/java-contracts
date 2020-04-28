package pl.coco.compiler;

import javax.inject.Inject;

import com.sun.source.util.TaskEvent;
import com.sun.source.util.TaskListener;

import pl.coco.compiler.instrumentation.ContractsInstrumentationVisitor;
import pl.coco.compiler.instrumentation.ContractsScanningVisitor;

public class InstrumentationListener implements TaskListener {

    private final ContractsScanningVisitor scanningVisitor;
    private final ContractsInstrumentationVisitor instrumentationVisitor;

    @Inject
    public InstrumentationListener(ContractsScanningVisitor scanningVisitor,
            ContractsInstrumentationVisitor instrumentationVisitor) {

        this.scanningVisitor = scanningVisitor;
        this.instrumentationVisitor = instrumentationVisitor;
    }

    @Override
    public void started(TaskEvent taskEvent) {

    }

    @Override
    public void finished(TaskEvent taskEvent) {
        if (taskEvent.getKind() == TaskEvent.Kind.ANALYZE) {
            taskEvent.getCompilationUnit().accept(scanningVisitor, null);
            taskEvent.getCompilationUnit().accept(instrumentationVisitor, null);
        }
    }
}
