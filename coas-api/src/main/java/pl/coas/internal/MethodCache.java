package pl.coas.internal;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import pl.coas.api.AspectException;

public class MethodCache {

    private static final Map<MethodKey, Method> methods = new HashMap<>();

    public static Method getMethod(Class<?> clazz, String methodName, Class<?>... parameterTypes) {
        return methods.computeIfAbsent(new MethodKey(clazz, methodName, parameterTypes),
                MethodCache::doGetMethod);
    }

    private static Method doGetMethod(MethodKey key) {
        try {
            return key.clazz.getDeclaredMethod(key.methodName, key.parameterTypes);
        } catch (NoSuchMethodException e) {
            throw new AspectException("An internal exception during aspect evaluation occurred.",
                    e);
        }
    }

    private static class MethodKey {

        private final Class<?> clazz;
        private final String methodName;
        private final Class<?>[] parameterTypes;

        MethodKey(Class<?> clazz, String methodName, Class<?>[] parameterTypes) {
            this.clazz = clazz;
            this.methodName = methodName;
            this.parameterTypes = parameterTypes;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o)
                return true;
            if (o == null || getClass() != o.getClass())
                return false;
            MethodKey methodKey = (MethodKey) o;
            return Objects.equals(clazz, methodKey.clazz) &&
                    Objects.equals(methodName, methodKey.methodName) &&
                    Arrays.equals(parameterTypes, methodKey.parameterTypes);
        }

        @Override
        public int hashCode() {
            int result = Objects.hash(clazz, methodName);
            result = 31 * result + Arrays.hashCode(parameterTypes);
            return result;
        }

        @Override
        public String toString() {
            return "Key{" +
                    "clazz=" + clazz +
                    ", methodName='" + methodName + '\'' +
                    ", parameterTypes=" + Arrays.toString(parameterTypes) +
                    '}';
        }
    }
}
