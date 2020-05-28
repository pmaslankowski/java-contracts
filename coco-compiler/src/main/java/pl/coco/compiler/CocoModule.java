package pl.coco.compiler;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.sun.source.util.JavacTask;
import com.sun.source.util.Trees;
import com.sun.tools.javac.api.BasicJavacTask;
import com.sun.tools.javac.code.Symtab;
import com.sun.tools.javac.code.Types;
import com.sun.tools.javac.comp.Resolve;
import com.sun.tools.javac.parser.ParserFactory;
import com.sun.tools.javac.tree.TreeMaker;
import com.sun.tools.javac.util.Names;

import pl.coco.compiler.arguments.CocoArgs;

public class CocoModule extends AbstractModule {

    private final JavacTask task;
    private final CocoArgs cocoArgs;

    public CocoModule(JavacTask task, CocoArgs cocoArgs) {
        this.task = task;
        this.cocoArgs = cocoArgs;
    }

    @Override
    protected void configure() {

    }

    @Provides
    BasicJavacTask basicJavacTask() {
        return (BasicJavacTask) task;
    }

    @Provides
    CocoArgs cocoArgs() {
        return cocoArgs;
    }

    @Provides
    Names names(BasicJavacTask task) {
        return Names.instance(task.getContext());
    }

    @Provides
    TreeMaker treeMaker(BasicJavacTask task) {
        return TreeMaker.instance(task.getContext());
    }

    @Provides
    Resolve resolver(BasicJavacTask task) {
        return Resolve.instance(task.getContext());
    }

    @Provides
    Trees trees(BasicJavacTask task) {
        return Trees.instance(task);
    }

    @Provides
    Types types(BasicJavacTask task) {
        return Types.instance(task.getContext());
    }

    @Provides
    Symtab symtab(BasicJavacTask task) {
        return Symtab.instance(task.getContext());
    }

    @Provides
    ParserFactory parserFactory(BasicJavacTask task) {
        return ParserFactory.instance(task.getContext());
    }
}
