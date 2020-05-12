package pl.coco.compiler.instrumentation;

import static com.sun.tools.javac.tree.JCTree.JCIdent;
import static com.sun.tools.javac.tree.JCTree.JCMethodInvocation;
import static com.sun.tools.javac.tree.JCTree.JCReturn;
import static com.sun.tools.javac.tree.JCTree.JCVariableDecl;
import static java.util.stream.Collectors.toList;

import java.util.ArrayList;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sun.source.tree.MethodTree;
import com.sun.source.tree.VariableTree;
import com.sun.tools.javac.code.Symbol.VarSymbol;
import com.sun.tools.javac.tree.JCTree.JCBlock;
import com.sun.tools.javac.tree.JCTree.JCClassDecl;
import com.sun.tools.javac.tree.JCTree.JCMethodDecl;
import com.sun.tools.javac.tree.JCTree.JCStatement;
import com.sun.tools.javac.tree.TreeMaker;
import com.sun.tools.javac.util.List;
import com.sun.tools.javac.util.Names;

import pl.coco.compiler.instrumentation.invocation.MethodInvocationBuilder;
import pl.coco.compiler.instrumentation.invocation.MethodInvocationDescription;
import pl.coco.compiler.instrumentation.synthetic.ContractSyntheticMethods;
import pl.coco.compiler.instrumentation.synthetic.ContractSyntheticMethodsGenerator;
import pl.coco.compiler.instrumentation.synthetic.MethodInput;
import pl.coco.compiler.util.AstUtil;

@Singleton
public class ContractProcessor {

    private static final String RESULT_VARIABLE_NAME = "result";

    private static final Logger log = LoggerFactory.getLogger(ContractProcessor.class);

    private final TreeMaker treeMaker;
    private final Names names;
    private final ContractAnalyzer contractAnalyzer;
    private final ContractSyntheticMethodsGenerator syntheticGenerator;
    private final MethodInvocationBuilder methodInvocationBuilder;

    @Inject
    public ContractProcessor(
            TreeMaker treeMaker,
            Names names,
            ContractAnalyzer contractAnalyzer,
            ContractSyntheticMethodsGenerator syntheticGenerator,
            MethodInvocationBuilder methodInvocationBuilder) {

        this.treeMaker = treeMaker;
        this.names = names;
        this.contractAnalyzer = contractAnalyzer;
        this.syntheticGenerator = syntheticGenerator;
        this.methodInvocationBuilder = methodInvocationBuilder;
    }

    // TODO: check that Contract.result() calls occur only inside Contract.ensures() in non void
    // method
    // TODO: add Contract.ForAll and Contract.Exists methods for array and Collections
    // TODO: type checking for Contract.result() calls
    // TODO: check that Contract.result() calls occur only inside Contract.ensures()
    public void process(MethodInput input) {
        JCClassDecl clazz = (JCClassDecl) input.getClazz();
        JCMethodDecl originalMethod = (JCMethodDecl) input.getMethod();
        JCBlock body = originalMethod.getBody();

        if (contractAnalyzer.hasContracts(clazz, originalMethod)) {

            log.debug("Detected contracts in method {}.{}", clazz.sym.getQualifiedName(),
                    originalMethod.getName());

            ContractSyntheticMethods methods =
                    syntheticGenerator.generateMethods(clazz, originalMethod);
            addSyntheticMethodsToClass(methods, clazz);

            java.util.List<JCStatement> instrumentedStmts =
                    generateInstrumentedMethodBody(originalMethod, methods);

            body.stats = List.from(instrumentedStmts);

            log.debug("Instrumented class code: {}", clazz);
        }
    }

    private void addSyntheticMethodsToClass(ContractSyntheticMethods methods, JCClassDecl clazz) {
        AstUtil.addMethodToClass(methods.getTarget(), clazz);
        AstUtil.addMethodToClass(methods.getPreconditions(), clazz);
        AstUtil.addMethodToClass(methods.getPostconditions(), clazz);
    }

    private java.util.List<JCStatement> generateInstrumentedMethodBody(JCMethodDecl originalMethod,
            ContractSyntheticMethods syntheticMethods) {

        JCMethodDecl target = syntheticMethods.getTarget();
        JCMethodDecl preconditions = syntheticMethods.getPreconditions();
        JCMethodDecl postconditions = syntheticMethods.getPostconditions();

        VarSymbol resultSymbol = getResultSymbol(target);
        JCStatement targetInvocation = generateTargetInvocationStatement(target, resultSymbol);
        JCStatement preconditionInvocation = generateInvocationStatement(preconditions);
        JCStatement postconditionInvocation = generateInvocationStatement(postconditions);

        java.util.List<JCStatement> processedStatements = new ArrayList<>();

        if (AstUtil.isConstructor(originalMethod)) {
            JCStatement superStmt = originalMethod.getBody().getStatements().get(0);
            processedStatements.add(superStmt);
        }

        processedStatements.add(preconditionInvocation);
        processedStatements.add(targetInvocation);
        processedStatements.add(postconditionInvocation);

        if (!AstUtil.isConstructor(originalMethod) && !AstUtil.isVoid(target)) {
            JCReturn returnStatement = treeMaker.Return(treeMaker.Ident(resultSymbol));
            processedStatements.add(returnStatement);
        }

        return processedStatements;
    }

    private VarSymbol getResultSymbol(JCMethodDecl target) {
        return new VarSymbol(0,
                names.fromString(RESULT_VARIABLE_NAME),
                target.sym.getReturnType(),
                target.sym);
    }

    private JCStatement generateTargetInvocationStatement(JCMethodDecl target, VarSymbol result) {

        JCMethodInvocation bridgeMethodInvocation = generateMethodInvocation(target);

        if (AstUtil.isVoid(target)) {
            return treeMaker.Call(bridgeMethodInvocation);
        }

        return treeMaker.VarDef(result, bridgeMethodInvocation);
    }

    private JCStatement generateInvocationStatement(JCMethodDecl method) {
        return treeMaker.Call(generateMethodInvocation(method));
    }

    private JCMethodInvocation generateMethodInvocation(JCMethodDecl method) {

        java.util.List<JCIdent> parameters = getParameters(method);
        MethodInvocationDescription desc = new MethodInvocationDescription.Builder()
                .withMethodSymbol(method.sym)
                .withArguments(List.from(parameters))
                .build();
        return methodInvocationBuilder.build(desc);
    }

    private java.util.List<JCIdent> getParameters(MethodTree method) {
        return method.getParameters().stream()
                .map(this::toIdentifier)
                .collect(toList());
    }

    private JCIdent toIdentifier(VariableTree param) {
        JCVariableDecl variableDec = (JCVariableDecl) param;
        return treeMaker.Ident(variableDec.sym);
    }
}
