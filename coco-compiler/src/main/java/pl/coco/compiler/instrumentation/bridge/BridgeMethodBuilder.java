package pl.coco.compiler.instrumentation.bridge;

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

public class BridgeMethodBuilder {

    public static final String BRIDGE_METHOD_PREFIX = "coco$bridge$";
    public static final long SYNTHETIC_METHOD_FLAG = 4096;

    private final TreeMaker treeMaker;
    private final Names names;

    @Inject
    public BridgeMethodBuilder(TreeMaker treeMaker, Names names) {
        this.treeMaker = treeMaker;
        this.names = names;
    }

    public JCMethodDecl buildBridge(JCMethodDecl originalMethod) {
        java.util.List<JCStatement> nonContractStatements =
                getNonContractStatements(originalMethod.getBody());
        JCBlock bridgeBody = treeMaker.Block(0, List.from(nonContractStatements));
        MethodSymbol bridgeSymbol = getBridgeMethodSymbol(originalMethod);
        return treeMaker.MethodDef(bridgeSymbol, originalMethod.type, bridgeBody);
    }

    private java.util.List<JCStatement> getNonContractStatements(JCBlock methodBody) {
        return methodBody.getStatements()
                .stream()
                .filter(statement -> !ContractAstUtil.isContractInvocation(statement))
                .collect(toList());
    }

    private MethodSymbol getBridgeMethodSymbol(JCMethodDecl originalMethod) {
        Name bridgeMethodName = getBridgeMethodName(originalMethod);

        long flags = getBridgeFlags(originalMethod);

        MethodSymbol result = new MethodSymbol(flags, bridgeMethodName,
                originalMethod.sym.type, originalMethod.sym.owner);
        result.params = originalMethod.sym.params;
        return result;
    }

    private long getBridgeFlags(JCMethodDecl originalMethod) {
        return originalMethod.sym.flags() | SYNTHETIC_METHOD_FLAG;
    }

    private Name getBridgeMethodName(JCMethodDecl originalMethod) {
        return names.fromString(BRIDGE_METHOD_PREFIX).append(originalMethod.getName());
    }
}
