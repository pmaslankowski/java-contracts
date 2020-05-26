package pl.coco.compiler.stripping;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.inject.Singleton;

import com.sun.source.tree.ClassTree;
import com.sun.source.tree.MethodTree;
import com.sun.source.util.TreeScanner;
import com.sun.tools.javac.tree.JCTree.JCBlock;
import com.sun.tools.javac.tree.JCTree.JCClassDecl;
import com.sun.tools.javac.tree.JCTree.JCMethodDecl;
import com.sun.tools.javac.tree.JCTree.JCStatement;

import pl.coco.compiler.util.CollectionUtils;
import pl.coco.compiler.util.ContractAstUtil;
import pl.coco.compiler.util.InvariantUtil;

@Singleton
public class ContractStrippingVisitor extends TreeScanner<Void, Void> {

    @Override
    public Void visitClass(ClassTree classTree, Void aVoid) {
        JCClassDecl clazz = (JCClassDecl) classTree;
        Optional<JCMethodDecl> invariantMethod = InvariantUtil.findInvariantMethod(clazz);
        invariantMethod.ifPresent(invariant -> removeInvariantMethodFromClass(invariant, clazz));

        return super.visitClass(classTree, aVoid);
    }

    private void removeInvariantMethodFromClass(JCMethodDecl invariantMethod, JCClassDecl clazz) {
        clazz.defs = CollectionUtils.remove(clazz.defs, invariantMethod);
        clazz.sym.members_field.remove(invariantMethod.sym);
    }

    @Override
    public Void visitMethod(MethodTree method, Void aVoid) {
        removeContractStatementsFromMethod(method);
        return super.visitMethod(method, aVoid);
    }

    private void removeContractStatementsFromMethod(MethodTree method) {
        JCBlock body = (JCBlock) method.getBody();
        List<JCStatement> contractStatements = getContractStatements(body);
        body.stats = body.stats.diff(com.sun.tools.javac.util.List.from(contractStatements));
    }

    private List<JCStatement> getContractStatements(JCBlock body) {
        return body.stats.stream()
                .filter(ContractAstUtil::isContractInvocation)
                .collect(Collectors.toList());
    }
}
