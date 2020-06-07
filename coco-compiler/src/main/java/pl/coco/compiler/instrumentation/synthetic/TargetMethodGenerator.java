package pl.coco.compiler.instrumentation.synthetic;

import static java.util.stream.Collectors.toList;

import javax.inject.Inject;
import javax.inject.Singleton;

import com.sun.tools.javac.code.Symbol.MethodSymbol;
import com.sun.tools.javac.tree.JCTree.JCBlock;
import com.sun.tools.javac.tree.JCTree.JCMethodDecl;
import com.sun.tools.javac.tree.JCTree.JCStatement;
import com.sun.tools.javac.tree.TreeMaker;
import com.sun.tools.javac.util.List;
import com.sun.tools.javac.util.Name;

import pl.coco.compiler.util.ContractAstUtil;
import pl.compiler.commons.util.AstUtil;

@Singleton
public class TargetMethodGenerator {

    private static final long SYNTHETIC_METHOD_FLAG = 4096;

    private final TreeMaker treeMaker;
    private final SyntheticMethodNameGenerator nameGenerator;

    @Inject
    public TargetMethodGenerator(TreeMaker treeMaker,
            SyntheticMethodNameGenerator nameGenerator) {
        this.treeMaker = treeMaker;
        this.nameGenerator = nameGenerator;
    }

    public JCMethodDecl generate(JCMethodDecl originalMethod) {
        JCBlock targetBody = getTargetBody(originalMethod);
        MethodSymbol targetSymbol = getTargetMethodSymbol(originalMethod);
        return treeMaker.MethodDef(targetSymbol, originalMethod.type, targetBody);
    }

    private JCBlock getTargetBody(JCMethodDecl originalMethod) {
        java.util.List<JCStatement> nonContractStatements =
                getNonContractStatements(originalMethod.getBody());

        if (AstUtil.isConstructor(originalMethod)) {
            nonContractStatements.remove(0);
        }

        return treeMaker.Block(0, List.from(nonContractStatements));
    }

    private java.util.List<JCStatement> getNonContractStatements(JCBlock methodBody) {
        return methodBody.getStatements()
                .stream()
                .filter(statement -> !ContractAstUtil.isContractInvocation(statement))
                .collect(toList());
    }

    private MethodSymbol getTargetMethodSymbol(JCMethodDecl originalMethod) {
        Name bridgeMethodName = nameGenerator.getTargetMethodName(originalMethod);

        long flags = getTargetFlags(originalMethod);

        MethodSymbol result = new MethodSymbol(flags, bridgeMethodName,
                originalMethod.sym.type, originalMethod.sym.owner);
        result.params = originalMethod.sym.params;
        return result;
    }

    private long getTargetFlags(JCMethodDecl originalMethod) {
        return originalMethod.sym.flags() | SYNTHETIC_METHOD_FLAG;
    }
}
