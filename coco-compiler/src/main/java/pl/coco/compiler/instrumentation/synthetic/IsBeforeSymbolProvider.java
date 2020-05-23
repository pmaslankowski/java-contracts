package pl.coco.compiler.instrumentation.synthetic;

import javax.inject.Inject;
import javax.inject.Singleton;

import com.sun.tools.javac.code.Flags;
import com.sun.tools.javac.code.Symbol.VarSymbol;
import com.sun.tools.javac.code.Symtab;
import com.sun.tools.javac.tree.JCTree.JCMethodDecl;
import com.sun.tools.javac.util.Names;

@Singleton
public class IsBeforeSymbolProvider {

    private static final String IS_BEFORE_NAME = "isBefore";

    private final Names names;
    private final Symtab symtab;

    @Inject
    public IsBeforeSymbolProvider(Names names, Symtab symtab) {
        this.names = names;
        this.symtab = symtab;
    }

    public VarSymbol get(JCMethodDecl method) {
        return new VarSymbol(Flags.SYNTHETIC, names.fromString(IS_BEFORE_NAME), symtab.booleanType,
                method.sym);
    }
}
