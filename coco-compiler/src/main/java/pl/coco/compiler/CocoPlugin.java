package pl.coco.compiler;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.sun.source.util.JavacTask;
import com.sun.source.util.Plugin;

import pl.coco.compiler.arguments.ArgumentParser;
import pl.coco.compiler.arguments.CocoArgs;

public class CocoPlugin implements Plugin {

    private static final String PLUGIN_NAME = "coco";

    @Override
    public String getName() {
        return PLUGIN_NAME;
    }

    @Override
    public void init(JavacTask javacTask, String... args) {
        ArgumentParser parser = new ArgumentParser();
        CocoArgs cocoArgs = parser.parseArgs(args);

        Injector injector = Guice.createInjector(new CocoModule(javacTask, cocoArgs));
        InstrumentationListener instrumentationListener = injector.getInstance(
                InstrumentationListener.class);

        javacTask.addTaskListener(instrumentationListener);
    }
}
