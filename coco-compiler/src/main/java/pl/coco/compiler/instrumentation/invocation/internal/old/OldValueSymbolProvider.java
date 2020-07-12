package pl.coco.compiler.instrumentation.invocation.internal.old;

import javax.inject.Inject;
import javax.inject.Singleton;

import com.sun.tools.javac.code.Flags;
import com.sun.tools.javac.code.Symbol.VarSymbol;
import com.sun.tools.javac.tree.JCTree.JCMethodDecl;
import com.sun.tools.javac.tree.JCTree.JCVariableDecl;
import com.sun.tools.javac.util.Name;
import com.sun.tools.javac.util.Names;

@Singleton
public class OldValueSymbolProvider {

    private static final String OLD_VALUE_PREFIX = "coco$old$";

    private final Names names;

    @Inject
    public OldValueSymbolProvider(Names names) {
        this.names = names;
    }

    public VarSymbol getOldSymbol(JCMethodDecl method, JCVariableDecl arg) {
        Name name = addPrefixToName(arg.name, OLD_VALUE_PREFIX);
        return new VarSymbol(Flags.SYNTHETIC, name, arg.type, method.sym);
    }

    private Name addPrefixToName(Name name, String prefix) {
        return names.fromString(prefix).append(name);
    }
}
