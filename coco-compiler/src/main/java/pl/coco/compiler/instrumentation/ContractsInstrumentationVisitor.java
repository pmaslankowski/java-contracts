package pl.coco.compiler.instrumentation;

import java.util.List;
import java.util.Optional;

import com.sun.source.tree.ClassTree;
import com.sun.source.tree.MethodTree;
import com.sun.source.tree.StatementTree;
import com.sun.source.tree.Tree;
import com.sun.source.util.JavacTask;
import com.sun.source.util.TreeScanner;
import com.sun.tools.javac.api.BasicJavacTask;
import com.sun.tools.javac.tree.TreeMaker;
import com.sun.tools.javac.util.Context;

import pl.coco.compiler.arguments.CocoArgs;
import pl.coco.compiler.util.ContractAstUtil;
import pl.coco.compiler.util.TreePasser;

public class ContractsInstrumentationVisitor extends TreeScanner<Void, Void> {

    private final JavacTask task;
    private final TreeMaker treeMaker;
    private final CocoArgs cocoArgs;

    public ContractsInstrumentationVisitor(JavacTask task,
            CocoArgs cocoArgs) {
        this.task = task;
        this.treeMaker = TreeMaker.instance(getContext(task));
        this.cocoArgs = cocoArgs;
    }

    private static Context getContext(JavacTask task) {
        return ((BasicJavacTask) task).getContext();
    }

    @Override
    public Void visitClass(ClassTree clazz, Void aVoid) {
        List<? extends Tree> members = clazz.getMembers();
        for (Tree member : members) {
            Optional<MethodTree> method = TreePasser.of(member)
                    .as(MethodTree.class)
                    .get();

            method.ifPresent(methodDef -> processMethod(clazz, methodDef));
        }

        return super.visitClass(clazz, aVoid);
    }

    private void processMethod(ClassTree clazz, MethodTree method) {
        List<? extends StatementTree> statements = method.getBody().getStatements();
        if (containsContractInvocation(statements)) {
            ContractProcessor processor = new ContractProcessor(task, treeMaker, clazz, method);
            processor.process();
        }
    }

    private boolean containsContractInvocation(List<? extends StatementTree> statements) {
        return statements.stream()
                .map(ContractAstUtil::getContractInvocation)
                .anyMatch(Optional::isPresent);
    }
}