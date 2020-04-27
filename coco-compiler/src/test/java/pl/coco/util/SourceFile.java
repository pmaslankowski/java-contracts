package pl.coco.util;

import java.net.URI;

import javax.tools.SimpleJavaFileObject;

public class SourceFile extends SimpleJavaFileObject {

    private String qualifiedClassName;
    private String content;

    public SourceFile(String qualifiedClassName, String testSource) {
        super(getClassSourceURI(qualifiedClassName), Kind.SOURCE);
        this.qualifiedClassName = qualifiedClassName;
        content = testSource;
    }

    private static URI getClassSourceURI(String qualifiedClassName) {
        return URI.create(getLocation(qualifiedClassName));
    }

    private static String getLocation(String qualifiedClassName) {
        return String.format("file://%s%s", toPath(qualifiedClassName), Kind.SOURCE.extension);
    }

    private static String toPath(String qualifiedClassName) {
        return qualifiedClassName.replaceAll("\\.", "/");
    }

    @Override
    public CharSequence getCharContent(boolean ignoreEncodingErrors) {
        return content;
    }

    public String getQualifiedClassName() {
        return qualifiedClassName;
    }
}
