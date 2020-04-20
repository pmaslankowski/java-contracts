package pl.coco.compiler.util;

import com.sun.source.util.JavacTask;
import com.sun.tools.javac.api.JavacTaskImpl;
import com.sun.tools.javac.code.Symbol;
import com.sun.tools.javac.tree.TreeMaker;

import pl.coco.compiler.ContractMethod;

public class ArgumentsProcessorFactory {

    private final JavacTaskImpl task;
    private final TreeMaker treeMaker;
    private final Symbol resultSymbol;

    public ArgumentsProcessorFactory(JavacTask task, Symbol resultSymbol) {
        this.task = (JavacTaskImpl) task;
        this.treeMaker = TreeMaker.instance(this.task.getContext());
        this.resultSymbol = resultSymbol;
    }

    public ArgumentsProcessor newArgumentsProcessor(ContractMethod method) {
        switch (method) {
            case ENSURES:
                return new EnsuresArgumentsProcessor(task, resultSymbol);
            case REQUIRES:
                return new RequiresArgumentsProcessor(task);
            default:
                throw new IllegalArgumentException(
                        "Contract method: " + method + " is not supported in this class.");
        }
    }
}
