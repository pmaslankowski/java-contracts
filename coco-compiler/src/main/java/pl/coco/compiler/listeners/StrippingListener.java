package pl.coco.compiler.listeners;

import javax.inject.Inject;
import javax.inject.Singleton;

import com.sun.source.util.TaskEvent;
import com.sun.source.util.TaskListener;

import pl.coco.compiler.stripping.ContractStrippingVisitor;
import pl.coco.compiler.validation.ContractValidatingVisitor;

@Singleton
public class StrippingListener implements TaskListener {

    private final ContractValidatingVisitor validatingVisitor;
    private final ContractStrippingVisitor strippingVisitor;

    @Inject
    public StrippingListener(ContractValidatingVisitor validatingVisitor,
            ContractStrippingVisitor strippingVisitor) {
        this.validatingVisitor = validatingVisitor;
        this.strippingVisitor = strippingVisitor;
    }

    @Override
    public void started(TaskEvent taskEvent) {

    }

    @Override
    public void finished(TaskEvent taskEvent) {
        if (taskEvent.getKind() == TaskEvent.Kind.ANALYZE) {
            boolean isValid = taskEvent.getCompilationUnit().accept(validatingVisitor, null);
            if (isValid) {
                taskEvent.getCompilationUnit().accept(strippingVisitor, null);
            }
        }
    }
}
