package pl.coco.compiler;

import javax.inject.Inject;
import javax.inject.Singleton;

import com.sun.source.util.TaskEvent;
import com.sun.source.util.TaskListener;

import pl.coco.compiler.stripping.ContractStrippingVisitor;

@Singleton
public class StrippingListener implements TaskListener {

    private final ContractStrippingVisitor strippingVisitor;

    @Inject
    public StrippingListener(ContractStrippingVisitor strippingVisitor) {
        this.strippingVisitor = strippingVisitor;
    }

    @Override
    public void started(TaskEvent taskEvent) {

    }

    @Override
    public void finished(TaskEvent taskEvent) {
        if (taskEvent.getKind() == TaskEvent.Kind.ANALYZE) {
            taskEvent.getCompilationUnit().accept(strippingVisitor, null);
        }
    }
}
