package pl.coco.util;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;

import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;

public class TestCompiler {

    private static final String PLUGIN_NAME = "coco";

    public byte[] compile(String qualifiedClassName, String testSource) {
        StringWriter output = new StringWriter();
        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        FileManager fileManager = newFileManager(compiler);
        List<SourceFile> compilationUnits = getCompilationUnits(qualifiedClassName, testSource);
        List<String> arguments = getArguments();

        JavaCompiler.CompilationTask task = compiler.getTask(
                output, fileManager, null, arguments, null, compilationUnits);

        task.call();

        return Iterables.get(fileManager.getCompiled(), 0).getCompiledBinaries();
    }

    private FileManager newFileManager(JavaCompiler compiler) {
        return new FileManager(
                compiler.getStandardFileManager(null, null, null));
    }

    private List<SourceFile> getCompilationUnits(String qualifiedClassName, String testSource) {
        return Collections.singletonList(new SourceFile(qualifiedClassName, testSource));
    }

    private ArrayList<String> getArguments() {
        String classpath = System.getProperty("java.class.path");
        return Lists.newArrayList("-classpath", classpath, "-Xplugin:" + PLUGIN_NAME);
    }
}
