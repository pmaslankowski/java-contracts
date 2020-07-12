package pl.coco.compiler.instrumentation.methodbody;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import javax.inject.Inject;
import javax.inject.Singleton;

import com.sun.tools.javac.code.Symbol;
import com.sun.tools.javac.code.Symbol.VarSymbol;
import com.sun.tools.javac.code.Type;
import com.sun.tools.javac.tree.JCTree.JCExpression;
import com.sun.tools.javac.tree.JCTree.JCMethodDecl;
import com.sun.tools.javac.tree.JCTree.JCStatement;
import com.sun.tools.javac.tree.JCTree.JCVariableDecl;
import com.sun.tools.javac.tree.TreeMaker;

import pl.coco.compiler.instrumentation.invocation.internal.old.OldValue;
import pl.coco.compiler.instrumentation.invocation.internal.old.OldValueSymbolProvider;
import pl.compiler.commons.invocation.MethodInvocationBuilder;
import pl.compiler.commons.invocation.MethodInvocationDescription;
import pl.compiler.commons.util.TypeRegistry;

@Singleton
public class OldValueCloningInstrumenter {

    private static final String JAVA_LANG_OBJECT = "java.lang.Object";
    private static final String CONTRACT_ENGINE_CLASS = "pl.coco.internal.ContractEngine";
    private static final String DEEP_CLONE_METHOD_NAME = "deepClone";

    private final TreeMaker tm;
    private final OldValueSymbolProvider oldValueSymbolProvider;
    private final TypeRegistry typeRegistry;
    private final MethodInvocationBuilder invocationBuilder;

    @Inject
    public OldValueCloningInstrumenter(TreeMaker tm, OldValueSymbolProvider oldValueSymbolProvider,
            TypeRegistry typeRegistry, MethodInvocationBuilder invocationBuilder) {
        this.tm = tm;
        this.oldValueSymbolProvider = oldValueSymbolProvider;
        this.typeRegistry = typeRegistry;
        this.invocationBuilder = invocationBuilder;
    }

    public List<JCStatement> getOldValuesCloningStatements(JCMethodDecl method,
            List<OldValue> oldValues) {

        // warning: method getOldValueCloningStatement has a side-effect: it sets
        // clonedOriginalMethodVarSym field in corresponding oldValue object
        return oldValues.stream()
                .map(oldValue -> getOldValueCloningStatement(method, oldValue))
                .collect(Collectors.toList());
    }

    private JCStatement getOldValueCloningStatement(JCMethodDecl method, OldValue oldValue) {
        JCVariableDecl originalParam = oldValue.getOriginal();
        VarSymbol clonedSymbol = oldValueSymbolProvider.getOldSymbol(method, originalParam);
        JCExpression clonedValue = getClonedValue(oldValue, method);
        JCVariableDecl clonedVar = tm.VarDef(clonedSymbol, clonedValue);

        // warning: side-effect
        oldValue.setClonedOriginalMethodVar(clonedVar);

        return clonedVar;
    }

    private JCExpression getClonedValue(OldValue oldValue, JCMethodDecl method) {
        JCVariableDecl originalParam = oldValue.getOriginal();
        if (originalParam.type.isPrimitive()) {
            return tm.Ident(originalParam);
        } else {
            return getDeeplyClonedValue(method, originalParam);
        }
    }

    private JCExpression getDeeplyClonedValue(JCMethodDecl method, JCVariableDecl originalParam) {
        Symbol deepCloneMethodSymbol = getDeepCloneMethodSymbol(method);
        MethodInvocationDescription desc = new MethodInvocationDescription.Builder()
                .withClassName(CONTRACT_ENGINE_CLASS)
                .withMethodSymbol(deepCloneMethodSymbol)
                .withArguments(com.sun.tools.javac.util.List
                        .from(Collections.singletonList(tm.Ident(originalParam))))
                .withPosition(method.pos)
                .build();
        return invocationBuilder.build(desc);
    }

    private Symbol getDeepCloneMethodSymbol(JCMethodDecl currentMethod) {
        Type objectType = typeRegistry.getType(JAVA_LANG_OBJECT);
        com.sun.tools.javac.util.List<Type> arguments =
                com.sun.tools.javac.util.List.from(Arrays.asList(objectType));
        return typeRegistry.getMethodSymbol(CONTRACT_ENGINE_CLASS, DEEP_CLONE_METHOD_NAME,
                currentMethod, arguments);
    }
}
