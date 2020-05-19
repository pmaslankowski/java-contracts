package pl.coco.compiler.instrumentation;

import java.util.Optional;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.tree.JCTree.JCClassDecl;
import com.sun.tools.javac.tree.JCTree.JCMethodDecl;

import pl.coco.compiler.instrumentation.synthetic.InvariantMethodGenerator;
import pl.coco.compiler.util.AstUtil;
import pl.coco.compiler.util.ContractAstUtil;
import pl.coco.compiler.util.TreePasser;

@Singleton
public class ClassLevelProcessor {

    private static final Logger log = LoggerFactory.getLogger(ClassLevelProcessor.class);

    private final InvariantMethodGenerator invariantGenerator;

    @Inject
    public ClassLevelProcessor(InvariantMethodGenerator invariantGenerator) {
        this.invariantGenerator = invariantGenerator;
    }

    public void process(ClassInput input) {
        JCClassDecl clazz = input.getClazz();
        Optional<JCMethodDecl> originalInvariantMethod = findInvariantMethod(clazz);
        originalInvariantMethod.ifPresent(originalInvariant -> {
            log.debug("Found invariant method: " + originalInvariant.getName().toString());
            JCMethodDecl syntheticInvariant = invariantGenerator.generate(clazz, originalInvariant);
            log.debug("Processed invariant method:" + syntheticInvariant);
            AstUtil.addMethodToClass(syntheticInvariant, clazz);
        });
    }

    private Optional<JCMethodDecl> findInvariantMethod(JCClassDecl clazz) {
        for (JCTree member : clazz.getMembers()) {
            boolean isInvariantMethod = TreePasser.of(member)
                    .as(JCMethodDecl.class)
                    .mapAndGet(ContractAstUtil::isInvariantMethod)
                    .orElse(false);

            if (isInvariantMethod) {
                return Optional.of((JCMethodDecl) member);
            }
        }

        return Optional.empty();
    }
}
