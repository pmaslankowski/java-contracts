package pl.coco.compiler.util;

import static java.util.stream.Collectors.toList;

import com.sun.source.tree.StatementTree;
import com.sun.source.util.JavacTask;
import com.sun.tools.javac.api.BasicJavacTask;
import com.sun.tools.javac.code.Symbol;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.tree.TreeMaker;
import com.sun.tools.javac.util.Context;
import com.sun.tools.javac.util.List;
import com.sun.tools.javac.util.Names;

public class BridgeMethodBuilder {

    public static final String BRIDGE_METHOD_SUFFIX = "__COCO_BRIDGE";
    private final TreeMaker treeMaker;
    private final Names names;

    public BridgeMethodBuilder(JavacTask task) {
        //TODO: move common logic from BridgeMethodBuilder and MethodInvocationBuilder to common superclass
        Context context = ((BasicJavacTask) task).getContext();
        this.treeMaker = TreeMaker.instance(context);
        this.names = Names.instance(context);
    }

    public JCTree.JCMethodDecl buildBridge(JCTree.JCMethodDecl originalMethod) {
        JCTree.JCBlock methodBody = originalMethod.getBody();
        java.util.List<JCTree.JCStatement> otherStatements = methodBody.getStatements()
                .stream()
                .filter(statement -> !isContract(statement))
                .collect(toList());

        JCTree.JCBlock body = treeMaker.Block(/* TODO: make sure if these flags are correct*/0,
                List.from(otherStatements));

        Symbol.MethodSymbol bridgeSymbol = new Symbol.MethodSymbol(originalMethod.sym.flags(),
                originalMethod.getName().append(names.fromString(BRIDGE_METHOD_SUFFIX)),
                originalMethod.sym.type, originalMethod.sym.owner);

        return treeMaker.MethodDef(bridgeSymbol, body);
    }

    private boolean isContract(StatementTree statement) {
        return ContractAstUtil.getContractInvocation(statement).isPresent();
    }
}
