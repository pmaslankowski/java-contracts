package pl.coco.compiler;

import javax.inject.Inject;
import javax.inject.Singleton;

import com.sun.source.util.TaskListener;

import pl.coco.compiler.arguments.CocoArgs;
import pl.coco.compiler.listeners.InstrumentationListener;
import pl.coco.compiler.listeners.StrippingListener;

@Singleton
public class TaskListenerFactory {

    private final InstrumentationListener instrumentationListener;
    private final StrippingListener strippingListener;
    private final CocoArgs cocoArgs;

    @Inject
    public TaskListenerFactory(InstrumentationListener instrumentationListener,
            StrippingListener strippingListener, CocoArgs cocoArgs) {
        this.instrumentationListener = instrumentationListener;
        this.strippingListener = strippingListener;
        this.cocoArgs = cocoArgs;
    }

    public TaskListener getInstrumentationListener() {
        if (cocoArgs.isContractCheckingEnabled()) {
            return instrumentationListener;
        } else {
            return strippingListener;
        }
    }
}
