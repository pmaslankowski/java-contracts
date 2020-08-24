package pl.coas.compiler.instrumentation;

import javax.inject.Inject;
import javax.inject.Singleton;

import com.sun.source.tree.ClassTree;
import com.sun.source.util.TreeScanner;
import com.sun.tools.javac.code.Flags;
import com.sun.tools.javac.code.Symbol;
import com.sun.tools.javac.comp.Attr;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.tree.JCTree.JCClassDecl;
import com.sun.tools.javac.tree.JCTree.JCExpression;
import com.sun.tools.javac.tree.JCTree.JCNewClass;
import com.sun.tools.javac.tree.JCTree.JCVariableDecl;
import com.sun.tools.javac.tree.TreeMaker;
import com.sun.tools.javac.util.Names;

import pl.coas.compiler.instrumentation.util.AccessBuilder;
import pl.coas.compiler.instrumentation.util.AspectUtils;
import pl.compiler.commons.util.AstUtil;
import pl.compiler.commons.util.TreePasser;

@Singleton
public class AspectClassesInstrumentingVisitor extends TreeScanner<Void, Void> {

    private static final String INSTANCE_FIELD_NAME = "coas$aspect$instance";

    private final TreeMaker tm;
    private final AccessBuilder accessBuilder;
    private final Names names;
    private final Attr attributer;

    @Inject
    public AspectClassesInstrumentingVisitor(TreeMaker tm, AccessBuilder accessBuilder, Names names,
            Attr attributer) {
        this.tm = tm;
        this.accessBuilder = accessBuilder;
        this.names = names;
        this.attributer = attributer;
    }

    @Override
    public Void visitClass(ClassTree classTree, Void aVoid) {
        if (AspectUtils.isAspectClass(classTree)) {
            instrument((JCClassDecl) classTree);
        }
        return null;
    }

    public void instrument(JCClassDecl clazz) {
        addInstanceField(clazz);
        System.out.println("Aspect class after instrumentation:");
        System.out.println(clazz.toString());
    }

    private void addInstanceField(JCClassDecl clazz) {
        if (!hasInstanceFieldAlready(clazz)) {
            JCVariableDecl instanceField = createInstanceField(clazz);
            AstUtil.addFieldToClass(instanceField, clazz);
            attributeClass(clazz);
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

    // TODO: powtórzenie kodu z klasy JoinpointInstrumentingVisitor
    private void attributeClass(JCClassDecl clazz) {
        clazz.sym.flags_field = clazz.sym.flags_field | Flags.UNATTRIBUTED;
        // we need to disable unchecked warnings because when advice is applied to generic method
        // instrumentation inserts unchecked casts into the code
        // we don't want user to see warnings caused by these casts
        attributer.attribClass(clazz.pos(), clazz.sym);
    }
}
