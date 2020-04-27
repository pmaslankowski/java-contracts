package pl.coco.util;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;

import com.google.common.collect.Lists;

public class TestCompiler {

    public CompiledClasses compile(TestCompilerInput input) {
        StringWriter output = new StringWriter();
        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        FileManager fileManager = newFileManager(compiler);
        List<SourceFile> compilationUnits = input.getCompilationUnits();
        List<String> arguments = getArguments(input.getPluginArg());

        JavaCompiler.CompilationTask task = compiler.getTask(
                output, fileManager, null, arguments, null, compilationUnits);

        task.call();

        String outputString = output.toString();
        if (!outputString.isEmpty()) {
            throw new CompilationFailedException(outputString);
        }

        return asCompiledClasses(compilationUnits, fileManager.getCompiled());
    }

    private FileManager newFileManager(JavaCompiler compiler) {
        return new FileManager(
                compiler.getStandardFileManager(null, null, null));
    }

    private ArrayList<String> getArguments(String pluginArg) {
        String classpath = System.getProperty("java.class.path");
        return Lists.newArrayList("-classpath", classpath, pluginArg);
    }

    private CompiledClasses asCompiledClasses(List<SourceFile> compilationUnits,
            List<ClassFile> compiled) {

        CompiledClasses result = new CompiledClasses();
        for (int i = 0; i < compilationUnits.size(); i++) {
            result.addBinary(compilationUnits.get(i).getQualifiedClassName(),
                    compiled.get(i).getCompiledBinaries());
        }

        return result;
    }
}
