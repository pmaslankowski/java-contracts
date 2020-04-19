package pl.coco.compiler.util;

import static java.util.stream.Collectors.toList;

import com.sun.source.util.JavacTask;
import com.sun.tools.javac.api.BasicJavacTask;
import com.sun.tools.javac.code.Symbol;
import com.sun.tools.javac.tree.JCTree;
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

    public JCTree.JCMethodDecl buildBridge(JCTree.JCMethodDecl originalMethod) {
        java.util.List<JCTree.JCStatement> nonContractStatements = getNonContractStatements(
                originalMethod.getBody());
        JCTree.JCBlock bridgeBody = treeMaker.Block(0, List.from(nonContractStatements));
        Symbol.MethodSymbol bridgeSymbol = getBridgeMethodSymbol(originalMethod);
        return treeMaker.MethodDef(bridgeSymbol, bridgeBody);
    }

    private java.util.List<JCTree.JCStatement> getNonContractStatements(JCTree.JCBlock methodBody) {
        return methodBody.getStatements()
                .stream()
                .filter(statement -> !ContractAstUtil.isContractInvocation(statement))
                .collect(toList());
    }

    private Symbol.MethodSymbol getBridgeMethodSymbol(JCTree.JCMethodDecl originalMethod) {
        Name bridgeMethodName = getBridgeMethodName(originalMethod);
        return new Symbol.MethodSymbol(originalMethod.sym.flags(), bridgeMethodName,
                originalMethod.sym.type, originalMethod.sym.owner);
    }

    private Name getBridgeMethodName(JCTree.JCMethodDecl originalMethod) {
        return originalMethod.getName().append(names.fromString(BRIDGE_METHOD_SUFFIX));
    }
}
