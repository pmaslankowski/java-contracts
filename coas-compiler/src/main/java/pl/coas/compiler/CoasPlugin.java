package pl.coas.compiler;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.sun.source.util.JavacTask;
import com.sun.source.util.Plugin;
import com.sun.source.util.TaskListener;

import pl.coas.compiler.arguments.ArgumentParser;
import pl.coas.compiler.arguments.CoasArgs;

public class CoasPlugin implements Plugin {

    private static final String PLUGIN_NAME = "coas";

    @Override
    public String getName() {
        return PLUGIN_NAME;
    }

    @Override
    public void init(JavacTask javacTask, String... args) {
        ArgumentParser parser = new ArgumentParser();
        CoasArgs coasArgs = parser.parseArgs(args);

        Injector injector = Guice.createInjector(new CoasModule(javacTask, coasArgs));

        TaskListenerFactory taskListenerFactory = injector.getInstance(TaskListenerFactory.class);
        TaskListener cocoTaskListener = taskListenerFactory.getInstrumentationListener();

        javacTask.addTaskListener(cocoTaskListener);
    }
}
