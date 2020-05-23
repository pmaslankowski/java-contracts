package pl.coco.compiler.instrumentation;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sun.tools.javac.code.Symbol;
import com.sun.tools.javac.tree.JCTree.JCBlock;
import com.sun.tools.javac.tree.JCTree.JCClassDecl;
import com.sun.tools.javac.tree.JCTree.JCMethodDecl;
import com.sun.tools.javac.tree.JCTree.JCStatement;
import com.sun.tools.javac.util.List;

import pl.coco.compiler.instrumentation.methodbody.InstrumentedMethodBodyGenerator;
import pl.coco.compiler.instrumentation.methodbody.MethodBodyGeneratorInput;
import pl.coco.compiler.instrumentation.methodbody.ResultSymbolProvider;
import pl.coco.compiler.instrumentation.synthetic.ContractSyntheticMethods;
import pl.coco.compiler.instrumentation.synthetic.ContractSyntheticMethodsGenerator;
import pl.coco.compiler.util.AstUtil;
import pl.coco.compiler.util.ContractAstUtil;

@Singleton
public class MethodLevelProcessor {

    private static final Logger log = LoggerFactory.getLogger(MethodLevelProcessor.class);

    private final ContractAnalyzer contractAnalyzer;
    private final ContractSyntheticMethodsGenerator syntheticGenerator;
    private final ResultSymbolProvider resultSymbolProvider;
    private final InstrumentedMethodBodyGenerator methodBodyGenerator;

    @Inject
    public MethodLevelProcessor(ContractAnalyzer contractAnalyzer,
            ContractSyntheticMethodsGenerator syntheticGenerator,
            ResultSymbolProvider resultSymbolProvider,
            InstrumentedMethodBodyGenerator methodBodyGenerator) {
        this.contractAnalyzer = contractAnalyzer;
        this.syntheticGenerator = syntheticGenerator;
        this.resultSymbolProvider = resultSymbolProvider;
        this.methodBodyGenerator = methodBodyGenerator;
    }

    public void process(MethodInput input) {
        if (shouldProcess(input)) {
            doProcess(input);
        }
    }

    private boolean shouldProcess(MethodInput input) {
        JCClassDecl clazz = input.getClazz();
        JCMethodDecl method = input.getMethod();

        boolean hasContractsOrInvariants =
                contractAnalyzer.hasContracts(clazz, method) || containsInvariantMethod(clazz);

        return !ContractAstUtil.isSynthetic(method) && !ContractAstUtil.isInvariantMethod(method)
                && hasContractsOrInvariants;
    }

    private boolean containsInvariantMethod(JCClassDecl clazz) {
        return ContractAstUtil.getInvariantMethod(clazz).isPresent();
    }

    private void doProcess(MethodInput input) {
        JCClassDecl clazz = input.getClazz();
        JCMethodDecl method = input.getMethod();
        JCBlock body = method.getBody();

        log.debug("Detected contracts in method {}.{}",
                clazz.sym.getQualifiedName(), method.getName());

        ContractSyntheticMethods syntheticMethods =
                syntheticGenerator.generateMethods(clazz, method);
        addSyntheticMethodsToClass(syntheticMethods, clazz);

        java.util.List<JCStatement> instrumentedStmts =
                generateInstrumentedMethodBody(clazz, method, syntheticMethods);

        body.stats = List.from(instrumentedStmts);

        log.debug("Instrumented class code: {}", clazz);
    }

    private void addSyntheticMethodsToClass(ContractSyntheticMethods methods, JCClassDecl clazz) {
        AstUtil.addMethodToClass(methods.getTarget(), clazz);
        AstUtil.addMethodToClass(methods.getPreconditions(), clazz);
        AstUtil.addMethodToClass(methods.getPostconditions(), clazz);
    }

    private java.util.List<JCStatement> generateInstrumentedMethodBody(JCClassDecl clazz,
            JCMethodDecl originalMethod, ContractSyntheticMethods syntheticMethods) {

        Symbol.VarSymbol resultSymbol =
                resultSymbolProvider.getResultSymbol(syntheticMethods.getTarget());
        MethodBodyGeneratorInput input =
                new MethodBodyGeneratorInput(syntheticMethods, clazz, originalMethod, resultSymbol);

        return methodBodyGenerator.generateBody(input);
    }
}