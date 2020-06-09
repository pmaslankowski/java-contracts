package pl.test.compiler.commons;

import java.util.HashMap;
import java.util.Map;

public class CompiledClasses {

    private Map<String, byte[]> binaries = new HashMap<>();

    public void addBinary(String className, byte[] binary) {
        binaries.put(className, binary);
    }

    public byte[] getCompiledClass(String className) {
        return binaries.get(className);
    }
}
