package pl.coco.compiler.util;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.lang.model.element.Element;

import com.sun.tools.javac.api.JavacTaskImpl;
import com.sun.tools.javac.code.Symbol;
import com.sun.tools.javac.code.Type;

@Singleton
public class TypeRegistry {

    private final JavacTaskImpl task;

    @Inject
    public TypeRegistry(JavacTaskImpl task) {
        this.task = task;
    }

    public Type getType(String name) {
        return task.getElements().getTypeElement(name).asType();
    }

    public Symbol getClassSymbol(String className) {
        return (Symbol) getClassElement(className);
    }

    public Type getClassType(String className) {
        return (Type) getClassElement(className).asType();
    }

    private Element getClassElement(String className) {
        return task.getElements().getTypeElement(className);
    }

    public Symbol getPackageSymbol(String packageName) {
        return (Symbol) getPackageElement(packageName);
    }

    private Element getPackageElement(String packageName) {
        return task.getElements().getPackageElement(packageName);
    }

    public Type getPackageType(String packageName) {
        return (Type) getPackageElement(packageName).asType();
    }
}
