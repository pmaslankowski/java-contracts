package pl.coas.compiler.instrumentation.util;

import javax.inject.Inject;
import javax.inject.Singleton;

import com.sun.tools.javac.tree.JCTree.JCMethodDecl;
import com.sun.tools.javac.util.Name;
import com.sun.tools.javac.util.Names;

import pl.compiler.commons.util.AstUtil;

@Singleton
public class NameHelper {

    private static final String CONSTRUCTOR_NAME = "$constructor$";

    private final Names names;

    @Inject
    public NameHelper(Names names) {
        this.names = names;
    }

    public Name getNameWithPrefix(JCMethodDecl originalMethod, String prefix) {
        if (AstUtil.isConstructor(originalMethod)) {
            return addPrefixToName(names.fromString(CONSTRUCTOR_NAME), prefix);
        } else {
            return addPrefixToName(originalMethod.getName(), prefix);
        }
    }

    private Name addPrefixToName(Name name, String prefix) {
        return names.fromString(prefix).append(name);
    }
}
