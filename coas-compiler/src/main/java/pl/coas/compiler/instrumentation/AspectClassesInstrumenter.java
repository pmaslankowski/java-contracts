package pl.coas.compiler.instrumentation;

import javax.inject.Inject;
import javax.inject.Singleton;

import com.sun.tools.javac.code.Flags;
import com.sun.tools.javac.code.Symbol;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.tree.JCTree.JCClassDecl;
import com.sun.tools.javac.tree.JCTree.JCExpression;
import com.sun.tools.javac.tree.JCTree.JCNewClass;
import com.sun.tools.javac.tree.JCTree.JCVariableDecl;
import com.sun.tools.javac.tree.TreeMaker;
import com.sun.tools.javac.util.Names;

import pl.coas.api.AspectType;
import pl.coas.compiler.instrumentation.model.Advice;
import pl.coas.compiler.instrumentation.model.Aspect;
import pl.coas.compiler.instrumentation.util.AccessBuilder;
import pl.compiler.commons.util.AstUtil;
import pl.compiler.commons.util.TreePasser;

@Singleton
public class AspectClassesInstrumenter {

    private static final String INSTANCE_FIELD_NAME = "coas$aspect$instance";

    private final AspectRegistry registry;
    private final TreeMaker tm;
    private final AccessBuilder accessBuilder;
    private final Names names;

    @Inject
    public AspectClassesInstrumenter(AspectRegistry registry, TreeMaker tm,
            AccessBuilder accessBuilder, Names names) {
        this.registry = registry;
        this.tm = tm;
        this.accessBuilder = accessBuilder;
        this.names = names;
    }

    public void instrument() {
        for (Aspect aspect : registry.getAllAspects()) {
            if (aspect.getType() == AspectType.SINGLETON) {
                addInstanceField(aspect.getAdvice());
            }
        }
    }

    private void addInstanceField(Advice advice) {
        JCClassDecl clazz = advice.getClazz();
        if (!hasInstanceFieldAlready(clazz)) {
            JCVariableDecl instanceField = createInstanceField(clazz);
            AstUtil.addFieldToClass(instanceField, clazz);
        }
    }

    private JCVariableDecl createInstanceField(JCClassDecl clazz) {
        String typeName = clazz.type.tsym.getQualifiedName().toString();
        JCExpression classAccess = accessBuilder.build(typeName);
        JCNewClass newAspectClass = tm.NewClass(null, com.sun.tools.javac.util.List.nil(),
                classAccess, com.sun.tools.javac.util.List.nil(), null);

        return tm.VarDef(
                new Symbol.VarSymbol(Flags.PUBLIC | Flags.STATIC | Flags.FINAL,
                        names.fromString(INSTANCE_FIELD_NAME),
                        clazz.type, clazz.sym),
                newAspectClass);
    }

    private boolean hasInstanceFieldAlready(JCClassDecl clazz) {
        return clazz.getMembers().stream()
                .anyMatch(this::isInstanceField);
    }

    private Boolean isInstanceField(JCTree member) {
        return TreePasser.of(member)
                .as(JCVariableDecl.class)
                .get()
                .map(this::isInstanceField)
                .orElse(false);
    }

    private boolean isInstanceField(JCVariableDecl variable) {
        return variable.getName().contentEquals(INSTANCE_FIELD_NAME);
    }
}
