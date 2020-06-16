package pl.coas.compiler.instrumentation.model.pointcut;

import java.util.Set;

import javax.lang.model.element.Modifier;

public enum MethodKind {

    STATIC {

        @Override
        public boolean matches(Set<Modifier> modifiers) {
            return modifiers.contains(Modifier.STATIC);
        }
    },

    NON_STATIC {

        @Override
        public boolean matches(Set<Modifier> modifiers) {
            return !modifiers.contains(Modifier.STATIC);
        }
    },

    ALL {

        @Override
        public boolean matches(Set<Modifier> modifiers) {
            return true;
        }
    };

    public static MethodKind of(String value) {
        switch (value) {
            case "static":
                return STATIC;
            case "non-static":
                return NON_STATIC;
            case "":
                return ALL;
            default:
                throw new IllegalArgumentException(
                        "Value: " + value + " is not a valid method kind");
        }
    }

    public abstract boolean matches(Set<Modifier> modifiers);
}
