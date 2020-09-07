package pl.coas.compiler.listeners;

import java.util.Optional;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sun.source.util.TaskEvent;
import com.sun.source.util.TaskListener;

import pl.coas.compiler.instrumentation.AnnotationScanningVisitor;
import pl.coas.compiler.instrumentation.AspectClassesInstrumentingVisitor;
import pl.coas.compiler.instrumentation.AspectScanningVisitor;
import pl.coas.compiler.instrumentation.AspectsRemover;
import pl.coas.compiler.instrumentation.JoinpointInstrumentingVisitor;
import pl.coas.compiler.validation.AspectValidatingVisitor;

@Singleton
public class InstrumentationListener implements TaskListener {

    private static final Logger log = LoggerFactory.getLogger(InstrumentationListener.class);

    private final AnnotationScanningVisitor annotationScanningVisitor;
    private final AspectScanningVisitor scanningVisitor;
    private final AspectValidatingVisitor validatingVisitor;
    private final AspectClassesInstrumentingVisitor aspectClassesInstrumentingVisitor;
    private final JoinpointInstrumentingVisitor instrumentingVisitor;
    private final AspectsRemover aspectRemover;

    @Inject
    public InstrumentationListener(AnnotationScanningVisitor annotationScanningVisitor,
            AspectScanningVisitor scanningVisitor,
            AspectValidatingVisitor validatingVisitor,
            AspectClassesInstrumentingVisitor aspectClassesInstrumentingVisitor,
            JoinpointInstrumentingVisitor instrumentingVisitor, AspectsRemover aspectRemover) {
        this.annotationScanningVisitor = annotationScanningVisitor;
        this.scanningVisitor = scanningVisitor;
        this.validatingVisitor = validatingVisitor;
        this.aspectClassesInstrumentingVisitor = aspectClassesInstrumentingVisitor;
        this.instrumentingVisitor = instrumentingVisitor;
        this.aspectRemover = aspectRemover;
    }

    @Override
    public void started(TaskEvent taskEvent) {

    }

    @Override
    public void finished(TaskEvent taskEvent) {
        if (taskEvent.getKind() == TaskEvent.Kind.ENTER) {
            taskEvent.getCompilationUnit().accept(annotationScanningVisitor, null);
            taskEvent.getCompilationUnit().accept(scanningVisitor, null);
            // Here is potential problem with validation. This instrumentation has to be
            // performed here, because some joinpoints could be processed before corresponding
            // aspect has been instrumented
            taskEvent.getCompilationUnit().accept(aspectClassesInstrumentingVisitor, null);
        }

        if (taskEvent.getKind() == TaskEvent.Kind.ANALYZE) {
            boolean isValid = Optional
                    .ofNullable(taskEvent.getCompilationUnit().accept(validatingVisitor, null))
                    .orElse(true);
            if (!isValid) {
                return;
            }
            taskEvent.getCompilationUnit().accept(instrumentingVisitor, null);
            aspectRemover.removeAspectStatements();
            log.debug("Instrumented compilation unit:\n{}", taskEvent.getCompilationUnit());
        }
    }
}
