package pl.coas.compiler.instrumentation;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import javax.inject.Inject;
import javax.inject.Singleton;

import com.sun.tools.javac.code.Flags;
import com.sun.tools.javac.code.Symbol.MethodSymbol;
import com.sun.tools.javac.code.Type.ArrayType;
import com.sun.tools.javac.code.Type.TypeVar;
import com.sun.tools.javac.code.TypeTag;
import com.sun.tools.javac.tree.JCTree.JCBlock;
import com.sun.tools.javac.tree.JCTree.JCClassDecl;
import com.sun.tools.javac.tree.JCTree.JCExpression;
import com.sun.tools.javac.tree.JCTree.JCIdent;
import com.sun.tools.javac.tree.JCTree.JCLambda;
import com.sun.tools.javac.tree.JCTree.JCMethodDecl;
import com.sun.tools.javac.tree.JCTree.JCMethodInvocation;
import com.sun.tools.javac.tree.JCTree.JCNewArray;
import com.sun.tools.javac.tree.JCTree.JCReturn;
import com.sun.tools.javac.tree.JCTree.JCStatement;
import com.sun.tools.javac.tree.JCTree.JCTypeCast;
import com.sun.tools.javac.tree.JCTree.JCVariableDecl;
import com.sun.tools.javac.tree.TreeMaker;
import com.sun.tools.javac.util.Name;
import com.sun.tools.javac.util.Names;

import pl.coas.api.AspectType;
import pl.coas.compiler.instrumentation.model.Aspect;
import pl.coas.compiler.instrumentation.model.JoinPoint;
import pl.coas.compiler.instrumentation.util.AccessBuilder;
import pl.coas.compiler.instrumentation.util.NameHelper;
import pl.compiler.commons.util.AstUtil;

// TODO: dodać walidację sprawdzającą, że advice'y zwracają Object
@Singleton
public class JoinpointInstrumenter {

    public static final String ASPECT_INSTANCE_FIELD = "coas$aspect$instance";
    private static final String JOINPOINT_API_CLASS = "pl.coas.api.JoinPoint";
    private static final String TARGET_METHOD_PREFIX = "coas$target$";
    private final AspectRegistry registry;
    private final NameHelper nameHelper;
    private final TreeMaker tm;
    private final AccessBuilder accessBuilder;
    private final Names names;

    @Inject
    public JoinpointInstrumenter(AspectRegistry registry, TreeMaker tm,
            NameHelper nameHelper,
            AccessBuilder accessBuilder, Names names) {
        this.registry = registry;
        this.tm = tm;
        this.nameHelper = nameHelper;
        this.accessBuilder = accessBuilder;
        this.names = names;
    }

    public void instrument(JCClassDecl clazz, JCMethodDecl method) {
        JoinPoint joinPoint = new JoinPoint(clazz, method);
        List<Aspect> aspects = registry.getMatchingAspects(joinPoint);

        if (!aspects.isEmpty()) {
            JCMethodDecl targetMethod = generateTargetMethod(method);
            AstUtil.addMethodToClass(targetMethod, clazz);

            JCVariableDecl instanceVar = getInstanceVar(method, clazz);
            JCVariableDecl methodVar = getMethodVar(clazz, method);
            JCVariableDecl argsArray = getArgsArray(method);
            JCLambda targetLambda = generateLastTarget(targetMethod, aspects.size() - 1);

            for (int i = aspects.size() - 1; i >= 1; i--) {
                JCExpression joinPointExpr =
                        generateNewInnerJoinPointExpr(method, methodVar, instanceVar, targetLambda,
                                i);
                Aspect aspect = aspects.get(i);
                JCMethodInvocation adviceInvocation = generateAdviceCall(aspect, joinPointExpr);

                if (AstUtil.isVoid(method)) {
                    JCBlock lambdaBody =
                            tm.Block(0, com.sun.tools.javac.util.List.of(tm.Exec(adviceInvocation),
                                    tm.Return(nullResult())));
                    targetLambda = tm.Lambda(getLambdaParams(i - 1), lambdaBody);
                } else {
                    targetLambda = tm.Lambda(getLambdaParams(i - 1), adviceInvocation);
                }
            }

            JCExpression joinPointExpr =
                    generateNewOuterJoinPointExpr(method, methodVar, instanceVar, argsArray,
                            targetLambda);
            Aspect firstAspect = aspects.get(0);
            JCMethodInvocation adviceInvocation = generateAdviceCall(firstAspect, joinPointExpr);

            if (!AstUtil.isVoid(method)) {
                JCReturn retStmt = tm.Return(tm.TypeCast(method.getReturnType(), adviceInvocation));
                method.getBody().stats =
                        com.sun.tools.javac.util.List.of(instanceVar, methodVar, argsArray,
                                retStmt);
            } else {
                method.getBody().stats =
                        com.sun.tools.javac.util.List.of(instanceVar, methodVar, argsArray,
                                tm.Exec(adviceInvocation));
            }
        }
    }

    private JCExpression nullResult() {
        return accessBuilder.build("pl.coas.api.VoidResult.INSTANCE");
    }

    private JCMethodDecl generateTargetMethod(JCMethodDecl originalMethod) {
        JCBlock targetBody = getTargetBody(originalMethod);
        MethodSymbol targetSymbol = getTargetMethodSymbol(originalMethod);
        return tm.MethodDef(targetSymbol, originalMethod.type, targetBody);
    }

    private JCBlock getTargetBody(JCMethodDecl originalMethod) {
        List<JCStatement> statements = new LinkedList<>(originalMethod.getBody().getStatements());

        if (AstUtil.isConstructor(originalMethod)) {
            statements.remove(0);
        }

        return tm.Block(0, com.sun.tools.javac.util.List.from(statements));
    }

    private MethodSymbol getTargetMethodSymbol(JCMethodDecl originalMethod) {
        Name targetName = nameHelper.getNameWithPrefix(originalMethod, TARGET_METHOD_PREFIX);

        MethodSymbol result = new MethodSymbol(originalMethod.sym.flags_field, targetName,
                originalMethod.sym.type, originalMethod.sym.owner);
        result.params = originalMethod.sym.params;
        return result;
    }

    private JCVariableDecl getInstanceVar(JCMethodDecl method, JCClassDecl clazz) {
        if ((method.getModifiers().flags & Flags.STATIC) != 0) {
            return tm.VarDef(tm.Modifiers(Flags.FINAL), names.fromString("coas$instance"),
                    tm.Ident(names.fromString("Object")), tm.Literal(TypeTag.BOT, null));

        } else {
            return tm.VarDef(tm.Modifiers(Flags.FINAL), names.fromString("coas$instance"),
                    tm.Ident(names.fromString("Object")), tm.This(clazz.type));
        }
    }

    private JCVariableDecl getMethodVar(JCClassDecl clazz, JCMethodDecl method) {
        List<JCExpression> paramClassObjects = method.getParameters().stream()
                .map(this::getParamClassObject)
                .collect(Collectors.toList());

        ArrayList<JCExpression> params = new ArrayList<>();
        params.add(getParamClassObject(clazz));
        params.add(tm.Literal(method.name.toString()));
        params.addAll(paramClassObjects);

        JCExpression getMethodAccess =
                accessBuilder.build("pl.coas.internal.MethodCache.getMethod");
        JCExpression getMethodInvocation = tm.Apply(com.sun.tools.javac.util.List.nil(),
                getMethodAccess, com.sun.tools.javac.util.List.from(params));

        return tm.VarDef(tm.Modifiers(Flags.FINAL), names.fromString("coas$method"),
                accessBuilder.build("java.lang.reflect.Method"), getMethodInvocation);
    }

    private JCVariableDecl getArgsArray(JCMethodDecl method) {
        List<JCIdent> elems = method.getParameters().stream()
                .map(var -> tm.Ident(var.getName()))
                .collect(Collectors.toList());

        JCNewArray array = tm.NewArray(tm.Ident(names.fromString("Object")),
                com.sun.tools.javac.util.List.nil(),
                com.sun.tools.javac.util.List.from(elems));

        return tm.VarDef(tm.Modifiers(0), names.fromString("coas$args"),
                tm.TypeArray(tm.Ident(names.fromString("Object"))), array);
    }

    private JCLambda generateLastTarget(JCMethodDecl target, int idx) {
        JCMethodInvocation targetInvocation = generateLastTargetInvocation(target, idx);

        com.sun.tools.javac.util.List<JCVariableDecl> lambdaParams = getLambdaParams(idx);

        if (AstUtil.isVoid(target)) {
            JCStatement targetCall = tm.Exec(targetInvocation);
            JCReturn returnNull = tm.Return(nullResult());
            JCBlock lambdaBody =
                    tm.Block(0, com.sun.tools.javac.util.List.of(targetCall, returnNull));
            return tm.Lambda(lambdaParams, lambdaBody);
        } else {
            return tm.Lambda(lambdaParams, targetInvocation);
        }
    }

    private com.sun.tools.javac.util.List<JCVariableDecl> getLambdaParams(int idx) {
        return com.sun.tools.javac.util.List.of(tm.VarDef(tm.Modifiers(0),
                names.fromString(String.format("coas$args$%d", idx)),
                tm.TypeArray(tm.Ident(names.fromString("Object"))), null));
    }

    private JCMethodInvocation generateLastTargetInvocation(JCMethodDecl target, int idx) {
        List<JCExpression> params = new ArrayList<>();
        for (int i = 0; i < target.getParameters().length(); i++) {
            JCVariableDecl targetParam = target.getParameters().get(i);
            JCTypeCast param = tm.TypeCast(targetParam.getType(), tm.Indexed(
                    tm.Ident(names.fromString(String.format("coas$args$%d", idx))), tm.Literal(i)));

            params.add(param);
        }

        return tm.Apply(com.sun.tools.javac.util.List.nil(),
                tm.Ident(target.getName()), com.sun.tools.javac.util.List.from(params));
    }

    private JCExpression generateNewOuterJoinPointExpr(JCMethodDecl method,
            JCVariableDecl methodVar,
            JCVariableDecl classVar, JCVariableDecl argsArray, JCLambda target) {

        JCExpression joinPointAccess = accessBuilder.build(JOINPOINT_API_CLASS);
        return tm.NewClass(null, com.sun.tools.javac.util.List.nil(), joinPointAccess,
                com.sun.tools.javac.util.List.of(tm.Ident(classVar.getName()),
                        tm.Ident(methodVar.getName()), tm.Ident(argsArray.getName()), target),
                null);
    }

    private JCExpression generateNewInnerJoinPointExpr(JCMethodDecl method,
            JCVariableDecl methodVar, JCVariableDecl classVar, JCLambda target, int idx) {

        JCExpression joinPointAccess = accessBuilder.build(JOINPOINT_API_CLASS);
        return tm.NewClass(null, com.sun.tools.javac.util.List.nil(), joinPointAccess,
                com.sun.tools.javac.util.List.of(tm.Ident(classVar.getName()),
                        tm.Ident(methodVar.getName()),
                        tm.Ident(names.fromString(String.format("coas$args$%d", idx - 1))), target),
                null);
    }

    private JCExpression getParamClassObject(JCVariableDecl param) {
        if (param.type instanceof TypeVar) {
            String getClassObject = ((TypeVar) param.type).bound.tsym.getQualifiedName() + ".class";
            return accessBuilder.build(getClassObject);
        } else if (param.type.isPrimitive()) {
            return tm.Select(tm.TypeIdent(param.type.getTag()), names.fromString("class"));
        } else if (param.type instanceof ArrayType) {
            Name elemTypeName = ((ArrayType) param.type).elemtype.tsym.getQualifiedName();
            JCExpression elemTypeSelect = accessBuilder.build(elemTypeName.toString());
            return tm.Select(tm.TypeArray(elemTypeSelect), names.fromString("class"));
        } else {
            String getClassObject = param.type.tsym.getQualifiedName() + ".class";
            return accessBuilder.build(getClassObject);
        }
    }

    private JCExpression getParamClassObject(JCClassDecl clazz) {
        String getClassObject = clazz.type.tsym.getQualifiedName() + ".class";
        return accessBuilder.build(getClassObject);
    }

    // TODO: dodać walidację, że klasa z aspektem ma domyślny bezargumentowy konstruktor
    private JCMethodInvocation generateAdviceCall(Aspect aspect, JCExpression joinPointExpr) {
        JCMethodDecl method = aspect.getAdvice().getMethod();
        JCExpression aspectInstance = getAspectInstance(aspect);
        return tm.Apply(com.sun.tools.javac.util.List.nil(),
                tm.Select(aspectInstance, method.getName()),
                com.sun.tools.javac.util.List.of(joinPointExpr));
    }

    private JCExpression getAspectInstance(Aspect aspect) {
        JCClassDecl clazz = aspect.getAdvice().getClazz();
        Name typeName = clazz.type.tsym.getQualifiedName();
        JCExpression aspectAccess = accessBuilder.build(typeName.toString());

        if (aspect.getType() == AspectType.PROTOTYPE) {
            return tm.NewClass(null, com.sun.tools.javac.util.List.nil(),
                    aspectAccess, com.sun.tools.javac.util.List.nil(), null);
        } else {
            return tm.Select(aspectAccess, names.fromString(ASPECT_INSTANCE_FIELD));
        }
    }
}
