package pl.coco.compiler.annotation.util;

import javax.inject.Inject;
import javax.inject.Singleton;

import com.sun.tools.javac.tree.JCTree.JCExpression;
import com.sun.tools.javac.tree.TreeMaker;
import com.sun.tools.javac.util.Names;

@Singleton
public class SimpleAccessBuilder {

    private final TreeMaker treeMaker;
    private final Names names;

    @Inject
    public SimpleAccessBuilder(TreeMaker treeMaker, Names names) {
        this.treeMaker = treeMaker;
        this.names = names;
    }

    public JCExpression build(String fullyQualifiedPath, int position) {
        treeMaker.at(position);

        String[] accessPath = fullyQualifiedPath.split("\\.");

        String topPackageName = accessPath[0];
        JCExpression access = treeMaker.Ident(names.fromString(topPackageName));

        for (int i = 1; i < accessPath.length; i++) {
            access = treeMaker.Select(access, names.fromString(accessPath[i]));
        }

        return access;
    }
}
