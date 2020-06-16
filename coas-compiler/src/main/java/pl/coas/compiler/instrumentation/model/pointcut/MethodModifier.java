package pl.coas.compiler.instrumentation.model.pointcut;

import java.util.Set;

import javax.lang.model.element.Modifier;

public enum MethodModifier {

    PUBLIC {

        @Override
        public boolean matches(Set<Modifier> modifiers) {
            return modifiers.contains(Modifier.PUBLIC);
        }
    },

    PRIVATE {

        @Override
        public boolean matches(Set<Modifier> modifiers) {
            return modifiers.contains(Modifier.PRIVATE);
        }
    },

    PROTECTED {

        @Override
        public boolean matches(Set<Modifier> modifiers) {
            return modifiers.contains(Modifier.PROTECTED);
        }
    },

    PACKAGE_PRIVATE {

        @Override
        public boolean matches(Set<Modifier> modifiers) {
            return !modifiers.contains(Modifier.PUBLIC)
                    && !modifiers.contains(Modifier.PRIVATE)
                    && !modifiers.contains(Modifier.PROTECTED);
        }
    };

    public static MethodModifier of(String value) {
        switch (value) {
            case "public":
                return PUBLIC;
            case "private":
                return PRIVATE;
            case "protected":
                return PROTECTED;
            case "package-private":
                return PACKAGE_PRIVATE;
            default:
                throw new IllegalArgumentException(
                        "Value " + value + " is not a valid method modifier");
        }
    }

    public abstract boolean matches(Set<Modifier> modifiers);
}
