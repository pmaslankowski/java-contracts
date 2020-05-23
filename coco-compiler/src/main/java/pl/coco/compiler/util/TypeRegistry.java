package pl.coco.compiler.util;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.lang.model.element.Element;

import com.sun.tools.javac.api.JavacTaskImpl;
import com.sun.tools.javac.code.Symbol;
import com.sun.tools.javac.code.Type;
import com.sun.tools.javac.comp.AttrContext;
import com.sun.tools.javac.comp.Env;
import com.sun.tools.javac.comp.Resolve;
import com.sun.tools.javac.tree.JCTree.JCMethodDecl;
import com.sun.tools.javac.util.List;
import com.sun.tools.javac.util.Name;
import com.sun.tools.javac.util.Names;

@Singleton
public class TypeRegistry {

    private final JavacTaskImpl task;
    private final Names names;
    private final Resolve resolver;

    @Inject
    public TypeRegistry(JavacTaskImpl task, Names names, Resolve resolver) {
        this.task = task;
        this.names = names;
        this.resolver = resolver;
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

    public Symbol getMethodSymbol(String fullyQualifiedClassName, String methodName,
            JCMethodDecl currentMethod, List<Type> arguments) {

        Env<AttrContext> env = new Env<>(currentMethod, new AttrContext());
        Type subject = getType(fullyQualifiedClassName);
        Name methodIdentifier = names.fromString(methodName);
        return resolver.resolveInternalMethod(currentMethod.pos(), env, subject, methodIdentifier,
                arguments, List.nil());
    }
}
