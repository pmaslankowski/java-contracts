package pl.test.compiler.commons;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import javax.tools.FileObject;
import javax.tools.ForwardingJavaFileManager;
import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;

public class FileManager extends ForwardingJavaFileManager<StandardJavaFileManager> {

    private List<ClassFile> compiled = new ArrayList<>();
    
    protected FileManager(StandardJavaFileManager fileManager) {
        super(fileManager);
    }

    @Override
    public JavaFileObject getJavaFileForOutput(Location location,
            String className, JavaFileObject.Kind kind, FileObject sibling) {

        ClassFile result = new ClassFile(URI.create("string://" + className));
        compiled.add(result);
        return result;
    }

    public List<ClassFile> getCompiled() {
        return compiled;
    }
}
