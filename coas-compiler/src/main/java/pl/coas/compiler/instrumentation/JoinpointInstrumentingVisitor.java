package pl.coas.compiler.instrumentation;

import java.lang.reflect.Field;
import java.util.Set;

import javax.inject.Inject;
import javax.inject.Singleton;

import com.sun.source.tree.ClassTree;
import com.sun.source.tree.MethodTree;
import com.sun.source.util.TreeScanner;
import com.sun.tools.javac.code.Flags;
import com.sun.tools.javac.code.Lint;
import com.sun.tools.javac.code.Lint.LintCategory;
import com.sun.tools.javac.comp.Attr;
import com.sun.tools.javac.tree.JCTree.JCClassDecl;
import com.sun.tools.javac.tree.JCTree.JCMethodDecl;

@Singleton
public class JoinpointInstrumentingVisitor extends TreeScanner<Void, Void> {

    private final JoinpointInstrumenter instrumenter;
    private final Attr attributer;

    private JCClassDecl clazz;

    @Inject
    public JoinpointInstrumentingVisitor(JoinpointInstrumenter instrumenter, Attr attributer) {
        this.instrumenter = instrumenter;
        this.attributer = attributer;
    }

    @Override
    public Void visitClass(ClassTree classTree, Void aVoid) {
        clazz = (JCClassDecl) classTree;
        super.visitClass(classTree, aVoid);

        attributeClass(clazz);

        return null;
    }

    private void attributeClass(JCClassDecl clazz) {
        clazz.sym.flags_field = clazz.sym.flags_field | Flags.UNATTRIBUTED;
        // we need to disable unchecked warnings because when advice is applied to generic method
        // instrumentation inserts unchecked casts into the code
        // we don't want user to see warnings caused by these casts
        disableUncheckedWarnings(attributer);
        attributer.attribClass(clazz.pos(), clazz.sym);
    }

    @SuppressWarnings("unchecked")
    private void disableUncheckedWarnings(Attr attributer) {
        // unfortunately API is package private so in order to disable unchecked warnings we have to
        // use reflection hacking
        try {
            Field chkField = attributer.getClass().getDeclaredField("chk");
            chkField.setAccessible(true);
            Object chk = chkField.get(attributer);
            Field lintField = chk.getClass().getDeclaredField("lint");
            lintField.setAccessible(true);
            Lint lint = (Lint) lintField.get(chk);
            Field suppressedValuesField = lint.getClass().getDeclaredField("suppressedValues");
            suppressedValuesField.setAccessible(true);
            Set<LintCategory> suppressedValues =
                    (Set<LintCategory>) suppressedValuesField.get(lint);
            suppressedValues.add(LintCategory.UNCHECKED);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Void visitMethod(MethodTree methodTree, Void aVoid) {
        JCMethodDecl method = (JCMethodDecl) methodTree;
        instrumenter.instrument(clazz, method);

        return super.visitMethod(methodTree, aVoid);
    }
}
