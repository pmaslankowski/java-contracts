package pl.coco.compiler.instrumentation;

import static java.util.stream.Collectors.toList;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;

import pl.coco.compiler.ContractType;
import pl.coco.compiler.util.BridgeMethodBuilder;
import pl.coco.compiler.util.ContractAstUtil;
import pl.coco.compiler.util.MethodInvocationBuilder;
import pl.coco.compiler.util.TreePasser;

import com.sun.source.tree.BlockTree;
import com.sun.source.tree.ClassTree;
import com.sun.source.tree.ExpressionStatementTree;
import com.sun.source.tree.ExpressionTree;
import com.sun.source.tree.MethodInvocationTree;
import com.sun.source.tree.MethodTree;
import com.sun.source.tree.StatementTree;
import com.sun.source.util.JavacTask;
import com.sun.tools.javac.api.JavacTaskImpl;
import com.sun.tools.javac.code.Symbol;
import com.sun.tools.javac.code.Type;
import com.sun.tools.javac.comp.AttrContext;
import com.sun.tools.javac.comp.Env;
import com.sun.tools.javac.comp.Resolve;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.tree.JCTree.JCBlock;
import com.sun.tools.javac.tree.JCTree.JCExpression;
import com.sun.tools.javac.tree.JCTree.JCLiteral;
import com.sun.tools.javac.tree.JCTree.JCStatement;
import com.sun.tools.javac.tree.TreeMaker;
import com.sun.tools.javac.tree.TreeScanner;
import com.sun.tools.javac.util.List;
import com.sun.tools.javac.util.Names;

public class ContractProcessor {

    private final JavacTask task;
    private final TreeMaker treeMaker;
    private final ClassTree clazz;
    private final MethodTree method;
    private final Names names;

    public ContractProcessor(JavacTask task, TreeMaker treeMaker,
                             ClassTree clazz, MethodTree method, Names names) {
        this.task = task;
        this.treeMaker = treeMaker;
        this.clazz = clazz;
        this.method = method;
        this.names = names;
    }

    //TODO: refactor this completely
    //TODO: add contract inheritance
    //TODO: type checking for Contract.result()  calls
    //TODO: check that Contract.result() calls occur only inside Contract.ensures()
    //TODO: add positions to all treeMaker calls
    //TODO: make sure that it works with generics
    //TODO: make sure that it works with instance methods
    //TODO: make sure that it works with fully qualified contract class names
    public void process() {
        BlockTree methodBody = method.getBody();

        java.util.List<? extends StatementTree> contractStatements = methodBody.getStatements()
                .stream()
                .filter(statement -> ContractAstUtil.getContractInvocation(statement).isPresent())
                .collect(toList());

        if (contractStatements.size() > 0) {
            // creating bridge method
            JCTree.JCClassDecl classDecl = (JCTree.JCClassDecl) this.clazz;
            BridgeMethodBuilder bridgeMethodBuilder = new BridgeMethodBuilder(task);
            JCTree.JCMethodDecl bridgeMethod = bridgeMethodBuilder.buildBridge(
                    (JCTree.JCMethodDecl) method);
            classDecl.defs = classDecl.getMembers().append(bridgeMethod);
            classDecl.sym.members().enter(bridgeMethod.sym);

            // processing preconditions
            //TODO: refactor to stream
            ArrayList<JCStatement> preconditions = new ArrayList<>();
            for (StatementTree statement : contractStatements) {
                //TODO: refactor .get()
                ContractMethodInvocation contractInvocation =
                        ContractAstUtil.getContractInvocation(statement).get();
                if (contractInvocation.getContractType() == ContractType.REQUIRES) {
                    preconditions.add(buildRequiresCall(contractInvocation, (JCTree.JCExpressionStatement) statement));
                }
            }

            //TODO: make sure that these modifiers make sense
            Symbol.VarSymbol resultVarSymbol = new Symbol.VarSymbol(0,
                    names.fromString("result"),
                    bridgeMethod.sym.getReturnType(),
                    bridgeMethod.sym);

            // processing postconditons
            ArrayList<JCStatement> postconditions = new ArrayList<>();
            for (StatementTree statement : contractStatements) {
                ContractMethodInvocation contractInvocation =
                        ContractAstUtil.getContractInvocation(statement).get();
                if (contractInvocation.getContractType() == ContractType.ENSURES) {
                    postconditions.add(buildEnsuresCall(contractInvocation, (JCTree.JCExpressionStatement) statement, resultVarSymbol));
                }
            }

            // bridge method call
            java.util.List<JCTree.JCIdent> parameters = method.getParameters().stream().map(param -> {
                JCTree.JCVariableDecl variableDec = (JCTree.JCVariableDecl) param;
                return treeMaker.Ident(variableDec.sym);
            }).collect(toList());

//            java.util.List<JCExpression> arguments = method.getParameters().stream()
//                    .map(formalParam -> {
//                        String name = formalParam.getName().toString();
//                        return treeMaker.Ident(names.fromString(name));
//                    }).collect(toList());

            JCTree.JCMethodInvocation bridgeMethodInvocation = new MethodInvocationBuilder(task)
                    .withMethodName(bridgeMethod.getName().toString())
                    .withMethodSymbol(bridgeMethod.sym)
                    .withArguments(List.from(parameters))
                    .buildAsMethodInvocation();

            JCTree.JCVariableDecl resultVarDef = treeMaker
                    .VarDef(resultVarSymbol, bridgeMethodInvocation);

            JCTree.JCReturn returnStatement = treeMaker
                    .Return(treeMaker.Ident(resultVarSymbol));

            // processed statements:
            ArrayList<JCStatement> processedStatements = new ArrayList<>(preconditions);
            processedStatements.add(resultVarDef);
            processedStatements.addAll(postconditions);
            processedStatements.add(returnStatement);

            ((JCBlock) methodBody).stats = List.from(processedStatements);
        }
    }

    private boolean isContract(StatementTree statement) {
        Optional<JCTree.JCFieldAccess> fieldAccess = TreePasser.of(statement)
                .as(ExpressionStatementTree.class)
                .map(ExpressionStatementTree::getExpression)
                .as(MethodInvocationTree.class)
                .map(MethodInvocationTree::getMethodSelect)
                .as(JCTree.JCFieldAccess.class)
                .get();

        if (!fieldAccess.isPresent()) {
            return false;
        }

        return fieldAccess.map(access -> access.sym.owner.name.equals(names.fromString("pl.coco.api.Contract"))).orElse(false);
    }

    // TODO: extract common logic from this and buildEnsuresCall to separate method
    private JCStatement buildRequiresCall(ContractMethodInvocation methodInvocation, JCTree.JCExpressionStatement statement) {
        //Env<AttrContext> env = TypeEnvs.instance(((JavacTaskImpl) task).getContext()).values().iterator().next();
        Env<AttrContext> env = new Env<>(statement, new AttrContext());
        Resolve rs = Resolve.instance(((JavacTaskImpl) task).getContext());
        Names names = Names.instance(((JavacTaskImpl) task).getContext());
        Type subject = (Type) task.getElements().getTypeElement("pl.coco.internal.ContractEngine").asType();
        Type string = (Type) task.getElements().getTypeElement("java.lang.String").asType();
        Type bool = (Type) task.getElements().getTypeElement("java.lang.Boolean").asType();
        Symbol methodSymbol = rs.resolveInternalMethod((statement).pos(), env, subject, names.fromString("requires"), List.from(Arrays.asList(bool, string)), List.nil());
        //new Type.MethodType(List.from(Collections.singletonList(argType)), resType, List.from(Collections.singletonList(thrownType)));
        return new MethodInvocationBuilder(task)
                .withClassName(ContractType.REQUIRES.getInternalClassName())
                .withMethodName(ContractType.REQUIRES.getMethodName())
                .withArguments(getArgumentsForContractEngine(methodInvocation.getArguments()))
                .withPosition(methodInvocation.getPos())
                .withMethodSymbol(methodSymbol)
                .build();
    }

    private List<JCExpression> getArgumentsForContractEngine(
            java.util.List<? extends ExpressionTree> originalArguments) {

        JCExpression precondition = (JCExpression) originalArguments.get(0);
        JCLiteral preconditionAsStringLiteral = treeMaker.Literal(precondition.toString());
        return List.from(Arrays.asList(precondition, preconditionAsStringLiteral));
    }

    private JCStatement buildEnsuresCall(ContractMethodInvocation methodInvocation, JCTree.JCExpressionStatement statement, Symbol.VarSymbol resultSymbol) {
        Env<AttrContext> env = new Env<>(statement, new AttrContext());
        Resolve rs = Resolve.instance(((JavacTaskImpl) task).getContext());
        Names names = Names.instance(((JavacTaskImpl) task).getContext());
        Type subject = (Type) task.getElements().getTypeElement("pl.coco.internal.ContractEngine").asType();
        Type string = (Type) task.getElements().getTypeElement("java.lang.String").asType();
        Type bool = (Type) task.getElements().getTypeElement("java.lang.Boolean").asType();
        Symbol methodSymbol = rs.resolveInternalMethod((statement).pos(), env, subject, names.fromString("ensures"), List.from(Arrays.asList(bool, string)), List.nil());
        //new Type.MethodType(List.from(Collections.singletonList(argType)), resType, List.from(Collections.singletonList(thrownType)));
        return new MethodInvocationBuilder(task)
                .withClassName(ContractType.ENSURES.getInternalClassName())
                .withMethodName(ContractType.ENSURES.getMethodName())
                .withArguments(getArgumentsForEnsures(methodInvocation.getArguments(), resultSymbol))
                .withPosition(methodInvocation.getPos())
                .withMethodSymbol(methodSymbol)
                .build();
    }

    //TODO: refactor this
    private List<JCTree.JCExpression> getArgumentsForEnsures(
            java.util.List<? extends ExpressionTree> arguments, Symbol.VarSymbol resultSymbol) {

        JCExpression postcondition = (JCExpression) arguments.get(0);
        JCLiteral postconditionAsStringLiteral = treeMaker.Literal(postcondition.toString());

        postcondition.accept(new TreeScanner() {

            @Override
            public void visitUnary(JCTree.JCUnary unary) {
                if (isResultInvocation(unary.arg)) {
                    unary.arg = newResultReference();
                }
                super.visitUnary(unary);
            }

            private JCTree.JCIdent newResultReference() {
                return treeMaker.Ident(resultSymbol);
            }

            @Override
            public void visitConditional(JCTree.JCConditional conditional) {
                if (isResultInvocation(conditional.cond)) {
                    conditional.cond = newResultReference();
                }
                if (isResultInvocation(conditional.truepart)) {
                    conditional.truepart = newResultReference();
                }
                if (isResultInvocation(conditional.falsepart)) {
                    conditional.falsepart = newResultReference();
                }
                super.visitConditional(conditional);
            }

            @Override
            public void visitBinary(JCTree.JCBinary binary) {
                if (isResultInvocation(binary.lhs)) {
                    binary.lhs = newResultReference();
                }
                if (isResultInvocation(binary.rhs)) {
                    binary.rhs = newResultReference();
                }
                super.visitBinary(binary);
            }

            private boolean isResultInvocation(JCTree.JCExpression expr) {
                Optional<SimpleMethodInvocation> invocationOpt = TreePasser.of(expr)
                        .as(MethodInvocationTree.class)
                        .flatMapAndGet(SimpleMethodInvocation::of);

                if (invocationOpt.isPresent()) {
                    SimpleMethodInvocation invocation = invocationOpt.get();
                    return invocation.getMethodName().contentEquals("result")
                            && invocation.getFullyQualifiedClassName().contentEquals("pl.coco.api.Contract");
                }

                return false;
            }
        });

        return List.from(Arrays.asList(postcondition, postconditionAsStringLiteral));
    }
}
