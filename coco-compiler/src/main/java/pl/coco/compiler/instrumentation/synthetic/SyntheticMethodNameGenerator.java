package pl.coco.compiler.instrumentation.synthetic;

import javax.inject.Inject;
import javax.inject.Singleton;

import com.sun.tools.javac.tree.JCTree.JCMethodDecl;
import com.sun.tools.javac.util.Name;
import com.sun.tools.javac.util.Names;

import pl.coco.compiler.util.AstUtil;

@Singleton
public class SyntheticMethodNameGenerator {

    private static final String TARGET_METHOD_PREFIX = "coco$target$";
    private static final String PRECONDITION_PREFIX = "coco$preconditions$";
    private static final String POSTCONDITION_PREFIX = "coco$postconditions$";
    private static final String INVARIANT_METHOD_NAME = "coco$invariant";

    private static final String CONSTRUCTOR_NAME = "$constructor$";

    private final Names names;

    @Inject
    public SyntheticMethodNameGenerator(Names names) {
        this.names = names;
    }

    public Name getTargetMethodName(JCMethodDecl method) {
        return getProcessedMethodNameWithPrefix(method, TARGET_METHOD_PREFIX);
    }

    public Name getPreconditionsMethodName(JCMethodDecl method) {
        return getProcessedMethodNameWithPrefix(method, PRECONDITION_PREFIX);

    }

    public Name getPostconditionMethodName(JCMethodDecl method) {
        return getProcessedMethodNameWithPrefix(method, POSTCONDITION_PREFIX);
    }

    public Name getInvariantMethodName() {
        return names.fromString(INVARIANT_METHOD_NAME);
    }

    private Name getProcessedMethodNameWithPrefix(JCMethodDecl originalMethod, String prefix) {
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
