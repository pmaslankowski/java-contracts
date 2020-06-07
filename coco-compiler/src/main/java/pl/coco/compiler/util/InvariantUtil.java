package pl.coco.compiler.util;

import java.util.List;
import java.util.Optional;

import pl.compiler.commons.util.TreePasser;

import com.sun.source.tree.AnnotationTree;
import com.sun.source.tree.MethodTree;
import com.sun.tools.javac.code.Type;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.tree.JCTree.JCAnnotation;
import com.sun.tools.javac.tree.JCTree.JCClassDecl;
import com.sun.tools.javac.tree.JCTree.JCMethodDecl;

public class InvariantUtil {

    public static final String INVARIANT_ANNOTATION_TYPE = "pl.coco.api.code.InvariantMethod";
    private static final String INVARIANT_METHOD_NAME = "coco$invariant";

    public static boolean isInvariantMethod(MethodTree method) {
        List<? extends AnnotationTree> annotations = method.getModifiers().getAnnotations();
        return annotations.stream().anyMatch(InvariantUtil::isInvariantAnnotation);
    }

    private static boolean isInvariantAnnotation(AnnotationTree annotation) {
        Type annotationType = ((JCAnnotation) annotation).type;
        return annotationType.tsym.flatName().contentEquals(INVARIANT_ANNOTATION_TYPE);
    }

    public static Optional<JCMethodDecl> getInvariantMethod(JCClassDecl clazz) {
        return clazz.getMembers().stream()
                .filter(InvariantUtil::isSyntheticInvariant)
                .map(member -> (JCMethodDecl) member)
                .findAny();
    }

    private static boolean isSyntheticInvariant(JCTree member) {
        return TreePasser.of(member)
                .as(JCMethodDecl.class)
                .mapAndGet(method -> method.getName().contentEquals(INVARIANT_METHOD_NAME))
                .orElse(false);
    }

    public static Optional<JCMethodDecl> findInvariantMethod(JCClassDecl clazz) {
        for (JCTree member : clazz.getMembers()) {
            boolean isInvariantMethod = TreePasser.of(member)
                    .as(JCMethodDecl.class)
                    .mapAndGet(InvariantUtil::isInvariantMethod)
                    .orElse(false);

            if (isInvariantMethod) {
                return Optional.of((JCMethodDecl) member);
            }
        }

        return Optional.empty();
    }
}
