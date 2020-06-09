package pl.coas.compiler.listeners;

import javax.inject.Singleton;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sun.source.util.TaskEvent;
import com.sun.source.util.TaskListener;

import pl.coas.compiler.instrumentation.AspectInstrumentingVisitor;
import pl.coas.compiler.instrumentation.AspectScanningVisitor;
import pl.coas.compiler.validation.AspectValidatingVisitor;

@Singleton
public class InstrumentationListener implements TaskListener {

    private static final Logger log = LoggerFactory.getLogger(InstrumentationListener.class);

    private final AspectScanningVisitor scanningVisitor;
    private final AspectValidatingVisitor validatingVisitor;
    private final AspectInstrumentingVisitor instrumentingVisitor;

    public InstrumentationListener(AspectScanningVisitor scanningVisitor,
            AspectValidatingVisitor validatingVisitor,
            AspectInstrumentingVisitor instrumentingVisitor) {
        this.scanningVisitor = scanningVisitor;
        this.validatingVisitor = validatingVisitor;
        this.instrumentingVisitor = instrumentingVisitor;
    }

    @Override
    public void started(TaskEvent taskEvent) {

    }

    @Override
    public void finished(TaskEvent taskEvent) {
        if (taskEvent.getKind() == TaskEvent.Kind.ENTER) {
            taskEvent.getCompilationUnit().accept(scanningVisitor, null);
        }

        if (taskEvent.getKind() == TaskEvent.Kind.ANALYZE) {
            boolean isValid = taskEvent.getCompilationUnit().accept(validatingVisitor, null);
            if (isValid) {
                taskEvent.getCompilationUnit().accept(instrumentingVisitor, null);
                log.debug("Instrumented compilation unit:\n{}", taskEvent.getCompilationUnit());
            }
        }
    }
}
