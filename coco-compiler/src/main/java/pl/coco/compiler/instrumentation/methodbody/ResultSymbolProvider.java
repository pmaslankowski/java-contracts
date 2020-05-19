package pl.coco.compiler.instrumentation.methodbody;

import static com.sun.tools.javac.tree.JCTree.JCMethodDecl;

import javax.inject.Inject;
import javax.inject.Singleton;

import com.sun.tools.javac.code.Symbol.VarSymbol;
import com.sun.tools.javac.util.Names;

@Singleton
public class ResultSymbolProvider {

    // TODO: zmienić nazwę, żeby utrudnić clasha
    private static final String RESULT_VARIABLE_NAME = "result";

    private final Names names;

    @Inject
    public ResultSymbolProvider(Names names) {
        this.names = names;
    }

    public VarSymbol getResultSymbol(JCMethodDecl target) {
        return new VarSymbol(0,
                names.fromString(RESULT_VARIABLE_NAME),
                target.sym.getReturnType(),
                target.sym);
    }
}
