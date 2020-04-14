package pl.coco.compiler;

import com.sun.source.util.JavacTask;
import com.sun.source.util.TaskEvent;
import com.sun.source.util.TaskListener;

import pl.coco.compiler.arguments.CocoArgs;
import pl.coco.compiler.instrumentation.ContractsInstrumentationVisitor;

public class InstrumentationListener implements TaskListener {

    private final JavacTask task;
    private final CocoArgs cocoArgs;

    public InstrumentationListener(JavacTask task, CocoArgs cocoArgs) {
        this.task = task;
        this.cocoArgs = cocoArgs;
    }

    @Override
    public void started(TaskEvent taskEvent) {

    }

    @Override
    public void finished(TaskEvent taskEvent) {
        if (taskEvent.getKind() == TaskEvent.Kind.ANALYZE) {
            taskEvent.getCompilationUnit().accept(
                    new ContractsInstrumentationVisitor(task, cocoArgs), null);
        }
    }
}
