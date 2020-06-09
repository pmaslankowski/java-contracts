package pl.coas.compiler;

import javax.inject.Inject;
import javax.inject.Singleton;

import pl.coas.compiler.arguments.CoasArgs;
import pl.coas.compiler.listeners.InstrumentationListener;

@Singleton
public class TaskListenerFactory {

    private final InstrumentationListener instrumentationListener;
    private final CoasArgs coasArgs;

    @Inject
    public TaskListenerFactory(InstrumentationListener instrumentationListener, CoasArgs coasArgs) {
        this.instrumentationListener = instrumentationListener;
        this.coasArgs = coasArgs;
    }

    public InstrumentationListener getInstrumentationListener() {
        // TODO: add stripping listener
        return instrumentationListener;
    }
}
