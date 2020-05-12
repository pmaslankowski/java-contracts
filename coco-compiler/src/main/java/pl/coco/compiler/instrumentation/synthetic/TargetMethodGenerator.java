package pl.coco.compiler.instrumentation.synthetic;

import static java.util.stream.Collectors.toList;

import javax.inject.Inject;

import com.sun.tools.javac.code.Symbol.MethodSymbol;
import com.sun.tools.javac.tree.JCTree.JCBlock;
import com.sun.tools.javac.tree.JCTree.JCMethodDecl;
import com.sun.tools.javac.tree.JCTree.JCStatement;
import com.sun.tools.javac.tree.TreeMaker;
import com.sun.tools.javac.util.List;
import com.sun.tools.javac.util.Name;
import com.sun.tools.javac.util.Names;

import pl.coco.compiler.util.ContractAstUtil;

public class TargetMethodGenerator {

    public static final String TARGET_METHOD_PREFIX = "coco$target$";
    public static final long SYNTHETIC_METHOD_FLAG = 4096;

    private final TreeMaker treeMaker;
    private final Names names;

    @Inject
    public TargetMethodGenerator(TreeMaker treeMaker, Names names) {
        this.treeMaker = treeMaker;
        this.names = names;
    }

    public JCMethodDecl generate(JCMethodDecl originalMethod) {
        java.util.List<JCStatement> nonContractStatements =
                getNonContractStatements(originalMethod.getBody());
        JCBlock targetBody = treeMaker.Block(0, List.from(nonContractStatements));
        MethodSymbol targetSymbol = getTargetMethodSymbol(originalMethod);
        return treeMaker.MethodDef(targetSymbol, originalMethod.type, targetBody);
    }

    private java.util.List<JCStatement> getNonContractStatements(JCBlock methodBody) {
        return methodBody.getStatements()
                .stream()
                .filter(statement -> !ContractAstUtil.isContractInvocation(statement))
                .collect(toList());
    }

    private MethodSymbol getTargetMethodSymbol(JCMethodDecl originalMethod) {
        Name bridgeMethodName = getTargetMethodName(originalMethod);

        long flags = getTargetFlags(originalMethod);

        MethodSymbol result = new MethodSymbol(flags, bridgeMethodName,
                originalMethod.sym.type, originalMethod.sym.owner);
        result.params = originalMethod.sym.params;
        return result;
    }

    private long getTargetFlags(JCMethodDecl originalMethod) {
        return originalMethod.sym.flags() | SYNTHETIC_METHOD_FLAG;
    }

    private Name getTargetMethodName(JCMethodDecl originalMethod) {
        return names.fromString(TARGET_METHOD_PREFIX).append(originalMethod.getName());
    }
}
