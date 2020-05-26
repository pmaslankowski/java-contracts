package pl.coco.compiler;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.sun.source.util.JavacTask;
import com.sun.source.util.Trees;
import com.sun.tools.javac.api.JavacTaskImpl;
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
    JavacTaskImpl javacTaskImpl() {
        return (JavacTaskImpl) task;
    }

    @Provides
    CocoArgs cocoArgs() {
        return cocoArgs;
    }

    @Provides
    Names names(JavacTaskImpl taskImpl) {
        return Names.instance(taskImpl.getContext());
    }

    @Provides
    TreeMaker treeMaker(JavacTaskImpl taskImpl) {
        return TreeMaker.instance(taskImpl.getContext());
    }

    @Provides
    Resolve resolver(JavacTaskImpl taskImpl) {
        return Resolve.instance(taskImpl.getContext());
    }

    @Provides
    Trees trees(JavacTaskImpl taskImpl) {
        return Trees.instance(taskImpl);
    }

    @Provides
    Types types(JavacTaskImpl taskImpl) {
        return Types.instance(taskImpl.getContext());
    }

    @Provides
    Symtab symtab(JavacTaskImpl taskImpl) {
        return Symtab.instance(taskImpl.getContext());
    }

    @Provides
    ParserFactory parserFactory(JavacTaskImpl taskImpl) {
        return ParserFactory.instance(taskImpl.getContext());
    }
}
