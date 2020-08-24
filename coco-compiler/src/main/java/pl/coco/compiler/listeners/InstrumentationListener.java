package pl.coco.compiler.listeners;

import java.util.Optional;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sun.source.util.TaskEvent;
import com.sun.source.util.TaskListener;
import com.sun.tools.javac.api.BasicJavacTask;

import pl.coco.compiler.annotation.ContractAnnotationVisitor;
import pl.coco.compiler.instrumentation.ContractProcessingVisitor;
import pl.coco.compiler.instrumentation.ContractScanningVisitor;
import pl.coco.compiler.validation.ContractValidatingVisitor;

public class InstrumentationListener implements TaskListener {

    private static final Logger log = LoggerFactory.getLogger(InstrumentationListener.class);

    private final ContractAnnotationVisitor annotationVisitor;
    private final ContractScanningVisitor scanningVisitor;
    private final ContractValidatingVisitor validatingVisitor;
    private final ContractProcessingVisitor contractProcessingVisitor;

    @Inject
    public InstrumentationListener(ContractAnnotationVisitor annotationVisitor,
            ContractScanningVisitor scanningVisitor, ContractValidatingVisitor validatingVisitor,
            ContractProcessingVisitor contractProcessingVisitor, BasicJavacTask task) {
        this.annotationVisitor = annotationVisitor;
        this.scanningVisitor = scanningVisitor;
        this.validatingVisitor = validatingVisitor;
        this.contractProcessingVisitor = contractProcessingVisitor;
    }

    @Override
    public void started(TaskEvent taskEvent) {
    }

    @Override
    public void finished(TaskEvent taskEvent) {
        if (taskEvent.getKind() == TaskEvent.Kind.PARSE) {
            taskEvent.getCompilationUnit().accept(annotationVisitor, null);
        }

        if (taskEvent.getKind() == TaskEvent.Kind.ENTER) {
            taskEvent.getCompilationUnit().accept(scanningVisitor, null);
        }

        if (taskEvent.getKind() == TaskEvent.Kind.ANALYZE) {
            boolean isValid = Optional
                    .ofNullable(taskEvent.getCompilationUnit().accept(validatingVisitor, null))
                    .orElse(true);
            if (isValid) {
                taskEvent.getCompilationUnit().accept(contractProcessingVisitor, null);
                log.debug("Instrumented compilation unit:\n{}", taskEvent.getCompilationUnit());
            }
        }

    }
}
