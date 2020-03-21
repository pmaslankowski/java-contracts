package pl.coco.util;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;

import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.sun.source.util.TaskEvent;
import com.sun.source.util.TaskListener;
import com.sun.tools.javac.api.JavacTaskImpl;

public class TestCompiler {

    public byte[] compile(String qualifiedClassName, String testSource, String pluginArg) {
        StringWriter output = new StringWriter();
        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        FileManager fileManager = newFileManager(compiler);
        List<SourceFile> compilationUnits = getCompilationUnits(qualifiedClassName, testSource);
        List<String> arguments = getArguments(pluginArg);

        JavaCompiler.CompilationTask task = compiler.getTask(
                output, fileManager, null, arguments, null, compilationUnits);

        task.call();

        String outputString = output.toString();
        if (!outputString.isEmpty()) {
            throw new CompilationFailedException(outputString);
        }

        return Iterables.get(fileManager.getCompiled(), 0).getCompiledBinaries();
    }

    private FileManager newFileManager(JavaCompiler compiler) {
        return new FileManager(
                compiler.getStandardFileManager(null, null, null));
    }

    private List<SourceFile> getCompilationUnits(String qualifiedClassName, String testSource) {
        return Collections.singletonList(new SourceFile(qualifiedClassName, testSource));
    }

    private ArrayList<String> getArguments(String pluginArg) {
        String classpath = System.getProperty("java.class.path");
        return Lists.newArrayList("-classpath", classpath, pluginArg);
    }
}
