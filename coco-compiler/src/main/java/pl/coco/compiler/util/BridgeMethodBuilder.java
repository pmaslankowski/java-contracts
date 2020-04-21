package pl.coco.compiler.util;

import static java.util.stream.Collectors.toList;

import com.sun.source.util.JavacTask;
import com.sun.tools.javac.api.BasicJavacTask;
import com.sun.tools.javac.code.Symbol.MethodSymbol;
import com.sun.tools.javac.tree.JCTree.JCBlock;
import com.sun.tools.javac.tree.JCTree.JCMethodDecl;
import com.sun.tools.javac.tree.JCTree.JCStatement;
import com.sun.tools.javac.tree.TreeMaker;
import com.sun.tools.javac.util.Context;
import com.sun.tools.javac.util.List;
import com.sun.tools.javac.util.Name;
import com.sun.tools.javac.util.Names;

public class BridgeMethodBuilder {

    public static final String BRIDGE_METHOD_SUFFIX = "__COCO_BRIDGE";
    private final TreeMaker treeMaker;
    private final Names names;

    public BridgeMethodBuilder(JavacTask task) {
        Context context = ((BasicJavacTask) task).getContext();
        this.treeMaker = TreeMaker.instance(context);
        this.names = Names.instance(context);
    }

    public JCMethodDecl buildBridge(JCMethodDecl originalMethod) {
        java.util.List<JCStatement> nonContractStatements =
                getNonContractStatements(originalMethod.getBody());
        JCBlock bridgeBody = treeMaker.Block(0, List.from(nonContractStatements));
        MethodSymbol bridgeSymbol = getBridgeMethodSymbol(originalMethod);
        return treeMaker.MethodDef(bridgeSymbol, bridgeBody);
    }

    private java.util.List<JCStatement> getNonContractStatements(JCBlock methodBody) {
        return methodBody.getStatements()
                .stream()
                .filter(statement -> !ContractAstUtil.isContractInvocation(statement))
                .collect(toList());
    }

    private MethodSymbol getBridgeMethodSymbol(JCMethodDecl originalMethod) {
        Name bridgeMethodName = getBridgeMethodName(originalMethod);
        return new MethodSymbol(originalMethod.sym.flags(), bridgeMethodName,
                originalMethod.sym.type, originalMethod.sym.owner);
    }

    private Name getBridgeMethodName(JCMethodDecl originalMethod) {
        return originalMethod.getName().append(names.fromString(BRIDGE_METHOD_SUFFIX));
    }
}
