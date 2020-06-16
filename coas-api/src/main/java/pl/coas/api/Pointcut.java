package pl.coas.api;

/**
 * Class providing the imperative API for pointcut specification.
 * <br>
 * Calls to any methods in this class are rewritten by coas javac plugin.
 * <br><br>
 * Pointcut specification methods return boolean but this is only a syntactic trick. It allows to
 * easily combine pointcuts like in the pointcut definition below
 * <br>
 * 
 * <pre>
 * Pointcut.staticTarget(MyClass.class) && Pointcut.args(Integer.class)
 * </pre>
 *
 * which matches all methods in <i>MyClass</i> class that takes one integer as an argument.
 *
 * @author pmaslankowski
 * @see Aspect
 */
public class Pointcut {

    private static final String MESSAGE =
            "This method call should not be present in compiled code. " +
                    "Make sure that coas javac plugin is correctly used " +
                    "(add flag -Xplugin:coas to javac arguments)";

    /**
     * Represents pointcut matching specific method calls.
     * <br><br>
     * Supported method pointcut expressions have the following form:
     * 
     * <pre>
     *   {TYPE} {FULLY-QUALIFIED-CLASS-NAME}.{METHOD-NAME}({ARGUMENTS}) {?throws {EXCEPTIONS}}
     * </pre>
     * 
     * where:
     * <ul>
     * <li>{TYPE} is a fully qualified return type (for example: <i>void</i> or
     * <i>com.mypackage.MyClass</i>)</li>
     * <li>{FULLY-QUALIFIED-CLASS-NAME} is a fully qualified class name (for example:
     * <i>com.mypackage.MyClass</i>)</li>
     * <li>{METHOD-NAME} is the method name</li>
     * <li>{ARGUMENTS} is a list of fully qualified type names or "..".</li>
     * <li>{EXCEPTION} is a optional list of fully qualified exception names</li>
     * </ul>
     * All parts of method pointcut expression can contain wildcards (*) which match any
     * strings (including empty string).
     * <br><br>
     * Examples of valid method pointcut expressions:
     * <ul>
     * <li><i>void com.mypackage.Service handleSubmission(int, String)</i> - pointcut matching
     * method <i>void handleSubmission(int, String)</i> in class <i>com.mypackage.Service</i></i>
     * </li>
     * <li><i>void com.mypackage.*.Service.handle*(..)</i> - pointcut matching all methods which
     * name begins with <i>handle</i> prefix in all <i>Service</i> classes in any package inside
     * <i>com.mypackage</i> package. For example, if class <i>com.mypackage.package1.Service</i>
     * contains 2 methods: <i>handleSubmission(int)</i> and <i>handleApproval(int, String)</i>,
     * both methods will be matched by this pointcut.</li>
     * <li><i>void com.mypackage.*.Service.handle*(int)</i> - pointcut matching all methods which
     * name begins with <i>handle</i> prefix and which take a single integer as an argument in all
     * <i>Service</i> classes in any package inside <i>com.mypackage</i> package. For example, if
     * class <i>com.mypackage.package1.Service</i> has 2 methods: <i>handleSubmission(int)</i> and
     * <i>handleApproval(int, String), then only the former method will be matched</li>
     * </ul>
     * 
     * @param expr method pointcut expression
     */
    public static boolean method(String expr) {
        throw new IllegalStateException(MESSAGE);
    }

    /**
     * Represents pointcut matching all methods declared in a specific class.
     * <br><br>
     * Supported class pointcut expressions have form of fully qualified class names with optional
     * wildcards (*).
     * <br><br>
     * Examples:
     * <ul>
     * <li><i>com.mypackage.Service</i> - pointcut matching all methods declared in
     * <i>com.mypackage.Service</i> class</li>
     * <li><i>com.mypackage.*.Service</i> - pointcut matching all methods declared in any
     * <i>Service</i> class inside subpackages of <i>com.mypackage</i> package</li>
     * <li><i>com.mypackage.Service*</i> - pointcut matching all methods declared in all classes
     * inside <i>com.mypackage</i> package which name begins with <i>Service</i>. For example, if
     * package <i>com.mypackage</i> contains 2 classes: <i>Service1</i> and <i>Service2</i>, both
     * classes will be matched</li>
     * </ul>
     * 
     * @param expr class pointcut expression
     */
    public static boolean target(String expr) {
        throw new IllegalStateException(MESSAGE);
    }

    /**
     * Represents pointcut dynamically matching all methods declared in a specific class.
     * <br><br>
     * Difference between dynamic and static matching of pointcut expressions is described in
     * {@link Pointcut#target(String)}.
     *
     * @param type type of matching class
     */
    public static boolean target(Class<?> type) {
        throw new IllegalStateException(MESSAGE);
    }

    /**
     * Represents pointcut matching all methods with specific arguments.
     * 
     * @param types types of the method arguments
     */
    public static boolean args(Class<?>... types) {
        throw new IllegalStateException(MESSAGE);
    }

    /**
     * Represents pointcut matching all methods with specific arguments.
     * <br><br>
     * Supported argument pointcut expressions have the form of comma separated list of fully
     * qualified type names with optional wildcards (*).
     * 
     * @param expr arguments pointcut expression
     */
    public static boolean args(String expr) {
        throw new IllegalStateException(MESSAGE);
    }

    /**
     * Represents pointcut matching all methods annotated with given annotation.
     * 
     * @param annotationType type of matching annotation
     */
    public static boolean annotation(Class<?> annotationType) {
        throw new IllegalStateException(MESSAGE);
    }

    /**
     * Represents pointcut matching all methods annotated with
     * <br><br>
     * Supported annotation pointcut expressions have the form of fully qualified type name with
     * optional wildcards (*).
     * 
     * @param expr annotation pointcut expression
     */
    public static boolean annotation(String expr) {
        throw new IllegalStateException(MESSAGE);
    }

    /**
     * Represents pointcut matching all methods in annotated class.
     * 
     * @param annotationType type of matching annotation
     */
    public static boolean annotatedType(Class<?> annotationType) {
        throw new IllegalStateException(MESSAGE);
    }

    /**
     * Represents pointcut matching all methods in annotated class.
     * <br><br>
     * Supported annotation pointcut expressions have the form of fully qualified type name with
     * optional wildcards (*).
     * 
     * @param expr annotation pointcut expression
     */
    public static boolean annotatedType(String expr) {
        throw new IllegalStateException(MESSAGE);
    }

    /**
     * Represents pointcut matching all methods with arguments annotated with given annotations.
     * 
     * @param annotationTypes array of annotation types
     */
    public static boolean annotatedArgs(Class<?>... annotationTypes) {
        throw new IllegalStateException(MESSAGE);
    }

    /**
     * Represents pointcut matching all methods with arguments annotated with given annotations.
     * <br><br>
     * Supported argument annotations pointcut expressions have the form of comma separated list of
     * fully qualified type names containing optional wildcards (*).
     * 
     * @param expr argument annotations pointcut expression
     */
    public static boolean annotatedArgs(String expr) {
        throw new IllegalStateException(MESSAGE);
    }
}
