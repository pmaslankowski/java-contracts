package pl.test.compiler.commons;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.net.URI;

import javax.tools.SimpleJavaFileObject;

public class ClassFile extends SimpleJavaFileObject {

    private ByteArrayOutputStream output;

    public ClassFile(URI uri) {
        super(uri, Kind.CLASS);
    }

    @Override
    public OutputStream openOutputStream() {
        return output = new ByteArrayOutputStream();
    }

    public byte[] getCompiledBinaries() {
        return output.toByteArray();
    }

}
