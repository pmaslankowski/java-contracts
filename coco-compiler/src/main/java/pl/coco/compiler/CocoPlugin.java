package pl.coco.compiler;

import com.sun.source.util.JavacTask;
import com.sun.source.util.Plugin;

public class CocoPlugin implements Plugin {

    private static final String PLUGIN_NAME = "Coco Plugin 1.0";

    @Override
    public String getName() {
        return PLUGIN_NAME;
    }

    @Override
    public void init(JavacTask javacTask, String... strings) {

    }
}
