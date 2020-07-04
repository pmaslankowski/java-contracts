package pl.coas.compiler.instrumentation.util;

import java.util.List;

import com.sun.source.tree.AnnotationTree;
import com.sun.source.tree.ClassTree;

public class AspectUtils {

    private static final String ASPECT_ANNOTATION_NAME = "AspectClass";

    public static boolean isAspectClass(ClassTree classTree) {
        List<? extends AnnotationTree> annotations = classTree.getModifiers().getAnnotations();
        return annotations.stream().anyMatch(AspectUtils::isAspectAnnotation);
    }

    private static boolean isAspectAnnotation(AnnotationTree annotation) {
        return annotation.getAnnotationType().toString().equals(ASPECT_ANNOTATION_NAME);
    }
}
