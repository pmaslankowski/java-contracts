package pl.coco.compiler.instrumentation.invocation.internal.old;

import java.util.Objects;

import com.sun.tools.javac.code.Symbol.VarSymbol;
import com.sun.tools.javac.tree.JCTree.JCVariableDecl;

public class OldValue {

    private JCVariableDecl original;
    private VarSymbol clonedPostconditionMethodParamSym;
    private JCVariableDecl clonedOriginalMethodVar;

    public OldValue(JCVariableDecl original, VarSymbol clonedPostconditionMethodParamSym) {
        this.original = original;
        this.clonedPostconditionMethodParamSym = clonedPostconditionMethodParamSym;
    }

    public JCVariableDecl getOriginal() {
        return original;
    }

    public VarSymbol getClonedPostconditionMethodParamSym() {
        return clonedPostconditionMethodParamSym;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        OldValue oldValue = (OldValue) o;
        return areVarsEqual(original, oldValue.original) &&
                areSymbolsEqual(clonedPostconditionMethodParamSym,
                        oldValue.clonedPostconditionMethodParamSym)
                && areVarsEqual(clonedOriginalMethodVar, oldValue.clonedOriginalMethodVar);
    }

    @Override
    public int hashCode() {
        return Objects.hash(hashVar(original), hashSym(clonedPostconditionMethodParamSym),
                hashVar(clonedOriginalMethodVar));
    }

    private boolean areVarsEqual(JCVariableDecl var1, JCVariableDecl var2) {
        return var1 == var2 || (var1 != null && var1.name.contentEquals(var2.name));
    }

    private int hashVar(JCVariableDecl var) {
        return var.name.toString().hashCode();
    }

    private boolean areSymbolsEqual(VarSymbol sym1, VarSymbol sym2) {
        return sym1 == sym2 || (sym1 != null && sym1.name.contentEquals(sym2.name));
    }

    private int hashSym(VarSymbol sym) {
        return sym.name.toString().hashCode();
    }

    public JCVariableDecl getClonedOriginalMethodVar() {
        return clonedOriginalMethodVar;
    }

    public void setClonedOriginalMethodVar(JCVariableDecl clonedOriginalMethodVar) {
        this.clonedOriginalMethodVar = clonedOriginalMethodVar;
    }
}
