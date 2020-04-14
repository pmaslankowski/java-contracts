package pl.coco.compiler.util;

import com.sun.source.util.JavacTask;
import com.sun.tools.javac.api.JavacTaskImpl;
import com.sun.tools.javac.code.Symbol;
import com.sun.tools.javac.tree.TreeMaker;

import pl.coco.compiler.ContractType;

public class ArgumentsProcessorFactory {

    private final JavacTaskImpl task;
    private final TreeMaker treeMaker;
    private final Symbol resultSymbol;

    public ArgumentsProcessorFactory(JavacTask task, Symbol resultSymbol) {
        this.task = (JavacTaskImpl) task;
        this.treeMaker = TreeMaker.instance(this.task.getContext());
        this.resultSymbol = resultSymbol;
    }

    public ArgumentsProcessor newArgumentsProcessor(ContractType type) {
        switch (type) {
            case ENSURES:
                return new EnsuresArgumentsProcessor(treeMaker, resultSymbol);
            case REQUIRES:
                return new RequiresArgumentsProcessor(treeMaker);
            default:
                throw new IllegalArgumentException(
                        "Type: " + type + " is not currently supported.");
        }
    }
}
