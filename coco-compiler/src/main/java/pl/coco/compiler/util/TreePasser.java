package pl.coco.compiler.util;

import java.util.Optional;
import java.util.function.Function;

import com.sun.source.tree.Tree;

public class TreePasser<T extends Tree> {

    private final T node;

    private TreePasser(T node) {
        this.node = node;
    }

    public static <T extends Tree> TreePasser<T> of(T node) {
        return new TreePasser<>(node);
    }

    @SuppressWarnings("unchecked")
    public <R extends Tree> TreePasser<R> as(Class<R> requestedType) {
        if (node == null) {
            return new TreePasser<>(null);
        }

        Class<? extends Tree> actualType = node.getClass();
        if (!requestedType.isAssignableFrom(actualType)) {
            return new TreePasser<>(null);
        }

        return new TreePasser<>((R) node);
    }

    public <R extends Tree> TreePasser<R> map(Function<T, R> fun) {
        if (node == null) {
            return new TreePasser<>(null);
        }

        return new TreePasser<>(fun.apply(node));
    }

    public <R> Optional<R> mapAndGet(Function<T, R> fun) {
        if (node == null) {
            return Optional.empty();
        }

        return Optional.ofNullable(fun.apply(node));
    }

    public <R> Optional<R> flatMapAndGet(Function<T, Optional<R>> fun) {
        if (node == null) {
            return Optional.empty();
        }
        return fun.apply(node);
    }

    public Optional<T> get() {
        return Optional.ofNullable(node);
    }
}
