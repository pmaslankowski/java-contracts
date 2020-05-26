package pl.coco.compiler.listeners;

import javax.inject.Inject;
import javax.inject.Singleton;

import com.sun.source.util.TaskListener;

import pl.coco.compiler.arguments.CocoArgs;

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
