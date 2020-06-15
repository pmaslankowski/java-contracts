package pl.coas.util;

import pl.test.compiler.commons.JavacTestUtils;

public class CoasTestUtils {

    public static final String ASPECTS_ENABLED = "-Xplugin:coas";

    public static Object compileAndRun(String qualifiedClassName, String methodName, String code)
            throws Throwable {

        return JavacTestUtils.compileAndRun(qualifiedClassName, methodName, code, new Class<?>[] {},
                new Object[] {}, ASPECTS_ENABLED);
    }
}
