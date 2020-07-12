package pl.coco.util;

import pl.test.compiler.commons.JavacTestUtils;

public class CocoTestUtils {

    public static final String CONTRACTS_ENABLED = "-Xplugin:coco";

    public static Object compileAndRun(String qualifiedClassName, String methodName, String code)
            throws Throwable {

        return JavacTestUtils.compileAndRun(qualifiedClassName, methodName, code, new Class<?>[] {},
                new Object[] {}, CONTRACTS_ENABLED);
    }

    public static byte[] compile(String qualifiedClassName, String code) {
        return JavacTestUtils.compile(qualifiedClassName, code, CONTRACTS_ENABLED);
    }
}
