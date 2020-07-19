// Generated from Pointcut.g4 by ANTLR 4.7.2

package pl.coas.compiler.instrumentation.antlr;

import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link PointcutParser}.
 */
public interface PointcutListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by {@link PointcutParser#pointcutRule}.
	 * @param ctx the parse tree
	 */
	void enterPointcutRule(PointcutParser.PointcutRuleContext ctx);
	/**
	 * Exit a parse tree produced by {@link PointcutParser#pointcutRule}.
	 * @param ctx the parse tree
	 */
	void exitPointcutRule(PointcutParser.PointcutRuleContext ctx);
	/**
	 * Enter a parse tree produced by {@link PointcutParser#alternatives}.
	 * @param ctx the parse tree
	 */
	void enterAlternatives(PointcutParser.AlternativesContext ctx);
	/**
	 * Exit a parse tree produced by {@link PointcutParser#alternatives}.
	 * @param ctx the parse tree
	 */
	void exitAlternatives(PointcutParser.AlternativesContext ctx);
	/**
	 * Enter a parse tree produced by {@link PointcutParser#conjunctions}.
	 * @param ctx the parse tree
	 */
	void enterConjunctions(PointcutParser.ConjunctionsContext ctx);
	/**
	 * Exit a parse tree produced by {@link PointcutParser#conjunctions}.
	 * @param ctx the parse tree
	 */
	void exitConjunctions(PointcutParser.ConjunctionsContext ctx);
	/**
	 * Enter a parse tree produced by {@link PointcutParser#atom}.
	 * @param ctx the parse tree
	 */
	void enterAtom(PointcutParser.AtomContext ctx);
	/**
	 * Exit a parse tree produced by {@link PointcutParser#atom}.
	 * @param ctx the parse tree
	 */
	void exitAtom(PointcutParser.AtomContext ctx);
	/**
	 * Enter a parse tree produced by {@link PointcutParser#atomicRule}.
	 * @param ctx the parse tree
	 */
	void enterAtomicRule(PointcutParser.AtomicRuleContext ctx);
	/**
	 * Exit a parse tree produced by {@link PointcutParser#atomicRule}.
	 * @param ctx the parse tree
	 */
	void exitAtomicRule(PointcutParser.AtomicRuleContext ctx);
	/**
	 * Enter a parse tree produced by {@link PointcutParser#methodRule}.
	 * @param ctx the parse tree
	 */
	void enterMethodRule(PointcutParser.MethodRuleContext ctx);
	/**
	 * Exit a parse tree produced by {@link PointcutParser#methodRule}.
	 * @param ctx the parse tree
	 */
	void exitMethodRule(PointcutParser.MethodRuleContext ctx);
	/**
	 * Enter a parse tree produced by {@link PointcutParser#targetRule}.
	 * @param ctx the parse tree
	 */
	void enterTargetRule(PointcutParser.TargetRuleContext ctx);
	/**
	 * Exit a parse tree produced by {@link PointcutParser#targetRule}.
	 * @param ctx the parse tree
	 */
	void exitTargetRule(PointcutParser.TargetRuleContext ctx);
	/**
	 * Enter a parse tree produced by {@link PointcutParser#argsRule}.
	 * @param ctx the parse tree
	 */
	void enterArgsRule(PointcutParser.ArgsRuleContext ctx);
	/**
	 * Exit a parse tree produced by {@link PointcutParser#argsRule}.
	 * @param ctx the parse tree
	 */
	void exitArgsRule(PointcutParser.ArgsRuleContext ctx);
	/**
	 * Enter a parse tree produced by {@link PointcutParser#annotationRule}.
	 * @param ctx the parse tree
	 */
	void enterAnnotationRule(PointcutParser.AnnotationRuleContext ctx);
	/**
	 * Exit a parse tree produced by {@link PointcutParser#annotationRule}.
	 * @param ctx the parse tree
	 */
	void exitAnnotationRule(PointcutParser.AnnotationRuleContext ctx);
	/**
	 * Enter a parse tree produced by {@link PointcutParser#annotatedTypeRule}.
	 * @param ctx the parse tree
	 */
	void enterAnnotatedTypeRule(PointcutParser.AnnotatedTypeRuleContext ctx);
	/**
	 * Exit a parse tree produced by {@link PointcutParser#annotatedTypeRule}.
	 * @param ctx the parse tree
	 */
	void exitAnnotatedTypeRule(PointcutParser.AnnotatedTypeRuleContext ctx);
	/**
	 * Enter a parse tree produced by {@link PointcutParser#annotatedArgsRule}.
	 * @param ctx the parse tree
	 */
	void enterAnnotatedArgsRule(PointcutParser.AnnotatedArgsRuleContext ctx);
	/**
	 * Exit a parse tree produced by {@link PointcutParser#annotatedArgsRule}.
	 * @param ctx the parse tree
	 */
	void exitAnnotatedArgsRule(PointcutParser.AnnotatedArgsRuleContext ctx);
	/**
	 * Enter a parse tree produced by {@link PointcutParser#method}.
	 * @param ctx the parse tree
	 */
	void enterMethod(PointcutParser.MethodContext ctx);
	/**
	 * Exit a parse tree produced by {@link PointcutParser#method}.
	 * @param ctx the parse tree
	 */
	void exitMethod(PointcutParser.MethodContext ctx);
	/**
	 * Enter a parse tree produced by {@link PointcutParser#kind}.
	 * @param ctx the parse tree
	 */
	void enterKind(PointcutParser.KindContext ctx);
	/**
	 * Exit a parse tree produced by {@link PointcutParser#kind}.
	 * @param ctx the parse tree
	 */
	void exitKind(PointcutParser.KindContext ctx);
	/**
	 * Enter a parse tree produced by {@link PointcutParser#modifiers}.
	 * @param ctx the parse tree
	 */
	void enterModifiers(PointcutParser.ModifiersContext ctx);
	/**
	 * Exit a parse tree produced by {@link PointcutParser#modifiers}.
	 * @param ctx the parse tree
	 */
	void exitModifiers(PointcutParser.ModifiersContext ctx);
	/**
	 * Enter a parse tree produced by {@link PointcutParser#modifier}.
	 * @param ctx the parse tree
	 */
	void enterModifier(PointcutParser.ModifierContext ctx);
	/**
	 * Exit a parse tree produced by {@link PointcutParser#modifier}.
	 * @param ctx the parse tree
	 */
	void exitModifier(PointcutParser.ModifierContext ctx);
	/**
	 * Enter a parse tree produced by {@link PointcutParser#returnType}.
	 * @param ctx the parse tree
	 */
	void enterReturnType(PointcutParser.ReturnTypeContext ctx);
	/**
	 * Exit a parse tree produced by {@link PointcutParser#returnType}.
	 * @param ctx the parse tree
	 */
	void exitReturnType(PointcutParser.ReturnTypeContext ctx);
	/**
	 * Enter a parse tree produced by {@link PointcutParser#className}.
	 * @param ctx the parse tree
	 */
	void enterClassName(PointcutParser.ClassNameContext ctx);
	/**
	 * Exit a parse tree produced by {@link PointcutParser#className}.
	 * @param ctx the parse tree
	 */
	void exitClassName(PointcutParser.ClassNameContext ctx);
	/**
	 * Enter a parse tree produced by {@link PointcutParser#methodName}.
	 * @param ctx the parse tree
	 */
	void enterMethodName(PointcutParser.MethodNameContext ctx);
	/**
	 * Exit a parse tree produced by {@link PointcutParser#methodName}.
	 * @param ctx the parse tree
	 */
	void exitMethodName(PointcutParser.MethodNameContext ctx);
	/**
	 * Enter a parse tree produced by {@link PointcutParser#args}.
	 * @param ctx the parse tree
	 */
	void enterArgs(PointcutParser.ArgsContext ctx);
	/**
	 * Exit a parse tree produced by {@link PointcutParser#args}.
	 * @param ctx the parse tree
	 */
	void exitArgs(PointcutParser.ArgsContext ctx);
	/**
	 * Enter a parse tree produced by {@link PointcutParser#argWildcard}.
	 * @param ctx the parse tree
	 */
	void enterArgWildcard(PointcutParser.ArgWildcardContext ctx);
	/**
	 * Exit a parse tree produced by {@link PointcutParser#argWildcard}.
	 * @param ctx the parse tree
	 */
	void exitArgWildcard(PointcutParser.ArgWildcardContext ctx);
	/**
	 * Enter a parse tree produced by {@link PointcutParser#arg}.
	 * @param ctx the parse tree
	 */
	void enterArg(PointcutParser.ArgContext ctx);
	/**
	 * Exit a parse tree produced by {@link PointcutParser#arg}.
	 * @param ctx the parse tree
	 */
	void exitArg(PointcutParser.ArgContext ctx);
	/**
	 * Enter a parse tree produced by {@link PointcutParser#exceptions}.
	 * @param ctx the parse tree
	 */
	void enterExceptions(PointcutParser.ExceptionsContext ctx);
	/**
	 * Exit a parse tree produced by {@link PointcutParser#exceptions}.
	 * @param ctx the parse tree
	 */
	void exitExceptions(PointcutParser.ExceptionsContext ctx);
	/**
	 * Enter a parse tree produced by {@link PointcutParser#exception}.
	 * @param ctx the parse tree
	 */
	void enterException(PointcutParser.ExceptionContext ctx);
	/**
	 * Exit a parse tree produced by {@link PointcutParser#exception}.
	 * @param ctx the parse tree
	 */
	void exitException(PointcutParser.ExceptionContext ctx);
	/**
	 * Enter a parse tree produced by {@link PointcutParser#typeNames}.
	 * @param ctx the parse tree
	 */
	void enterTypeNames(PointcutParser.TypeNamesContext ctx);
	/**
	 * Exit a parse tree produced by {@link PointcutParser#typeNames}.
	 * @param ctx the parse tree
	 */
	void exitTypeNames(PointcutParser.TypeNamesContext ctx);
	/**
	 * Enter a parse tree produced by {@link PointcutParser#typeName}.
	 * @param ctx the parse tree
	 */
	void enterTypeName(PointcutParser.TypeNameContext ctx);
	/**
	 * Exit a parse tree produced by {@link PointcutParser#typeName}.
	 * @param ctx the parse tree
	 */
	void exitTypeName(PointcutParser.TypeNameContext ctx);
}