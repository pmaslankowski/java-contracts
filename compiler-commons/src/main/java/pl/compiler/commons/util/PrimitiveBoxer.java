package pl.compiler.commons.util;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import javax.inject.Inject;
import javax.inject.Singleton;

import com.sun.tools.javac.code.Scope;
import com.sun.tools.javac.code.Symbol;
import com.sun.tools.javac.code.Symbol.MethodSymbol;
import com.sun.tools.javac.code.Type;
import com.sun.tools.javac.code.Types;
import com.sun.tools.javac.comp.AttrContext;
import com.sun.tools.javac.comp.Env;
import com.sun.tools.javac.comp.Resolve;
import com.sun.tools.javac.tree.JCTree.JCExpression;
import com.sun.tools.javac.tree.JCTree.JCMethodDecl;
import com.sun.tools.javac.tree.TreeMaker;
import com.sun.tools.javac.util.JCDiagnostic.DiagnosticPosition;
import com.sun.tools.javac.util.List;

@Singleton
public class PrimitiveBoxer {

    private static final String DUP_METHOD_NAME = "dup";

    private final TreeMaker treeMaker;
    private final Types types;
    private final Resolve resolver;

    @Inject
    public PrimitiveBoxer(TreeMaker treeMaker, Types types, Resolve resolver) {
        this.treeMaker = treeMaker;
        this.types = types;
        this.resolver = resolver;
    }

    public JCExpression boxIfNeeded(JCExpression expr, JCMethodDecl enclosing) {
        if (expr.type.isPrimitive()) {
            return boxPrimitive(expr, enclosing);
        } else {
            return expr;
        }
    }

    private JCExpression boxPrimitive(JCExpression expr, JCMethodDecl enclosing) {
        Type box = types.boxedClass(expr.type).type;
        Symbol ctor = lookupConstructor(enclosing, expr.pos(), box, List.of(expr.type));
        return treeMaker.Create(ctor, List.of(expr));

    }

    private MethodSymbol lookupConstructor(JCMethodDecl enclosing, DiagnosticPosition pos,
            Type clazz, List<Type> args) {

        Env<AttrContext> env = getEnv(enclosing);
        return resolver.resolveInternalConstructor(pos, env, clazz, args, null);
    }

    private Env<AttrContext> getEnv(JCMethodDecl enclosing) {
        try {
            // this hack is needed because AttrContext.dup(Scope) method is package private
            Scope scope = new Scope(enclosing.sym);
            Method dup = AttrContext.class.getDeclaredMethod(DUP_METHOD_NAME, Scope.class);
            dup.setAccessible(true);
            AttrContext attrContext = (AttrContext) dup.invoke(new AttrContext(), scope);
            return new Env<>(enclosing, attrContext);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }
}
