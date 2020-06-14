// Generated from MethodPointcut.g4 by ANTLR 4.7.2

package pl.coas.compiler.instrumentation.antlr;

import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link MethodPointcutParser}.
 */
public interface MethodPointcutListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by {@link MethodPointcutParser#method}.
	 * @param ctx the parse tree
	 */
	void enterMethod(MethodPointcutParser.MethodContext ctx);
	/**
	 * Exit a parse tree produced by {@link MethodPointcutParser#method}.
	 * @param ctx the parse tree
	 */
	void exitMethod(MethodPointcutParser.MethodContext ctx);
	/**
	 * Enter a parse tree produced by {@link MethodPointcutParser#kind}.
	 * @param ctx the parse tree
	 */
	void enterKind(MethodPointcutParser.KindContext ctx);
	/**
	 * Exit a parse tree produced by {@link MethodPointcutParser#kind}.
	 * @param ctx the parse tree
	 */
	void exitKind(MethodPointcutParser.KindContext ctx);
	/**
	 * Enter a parse tree produced by {@link MethodPointcutParser#modifiers}.
	 * @param ctx the parse tree
	 */
	void enterModifiers(MethodPointcutParser.ModifiersContext ctx);
	/**
	 * Exit a parse tree produced by {@link MethodPointcutParser#modifiers}.
	 * @param ctx the parse tree
	 */
	void exitModifiers(MethodPointcutParser.ModifiersContext ctx);
	/**
	 * Enter a parse tree produced by {@link MethodPointcutParser#modifier}.
	 * @param ctx the parse tree
	 */
	void enterModifier(MethodPointcutParser.ModifierContext ctx);
	/**
	 * Exit a parse tree produced by {@link MethodPointcutParser#modifier}.
	 * @param ctx the parse tree
	 */
	void exitModifier(MethodPointcutParser.ModifierContext ctx);
	/**
	 * Enter a parse tree produced by {@link MethodPointcutParser#returnType}.
	 * @param ctx the parse tree
	 */
	void enterReturnType(MethodPointcutParser.ReturnTypeContext ctx);
	/**
	 * Exit a parse tree produced by {@link MethodPointcutParser#returnType}.
	 * @param ctx the parse tree
	 */
	void exitReturnType(MethodPointcutParser.ReturnTypeContext ctx);
	/**
	 * Enter a parse tree produced by {@link MethodPointcutParser#className}.
	 * @param ctx the parse tree
	 */
	void enterClassName(MethodPointcutParser.ClassNameContext ctx);
	/**
	 * Exit a parse tree produced by {@link MethodPointcutParser#className}.
	 * @param ctx the parse tree
	 */
	void exitClassName(MethodPointcutParser.ClassNameContext ctx);
	/**
	 * Enter a parse tree produced by {@link MethodPointcutParser#methodName}.
	 * @param ctx the parse tree
	 */
	void enterMethodName(MethodPointcutParser.MethodNameContext ctx);
	/**
	 * Exit a parse tree produced by {@link MethodPointcutParser#methodName}.
	 * @param ctx the parse tree
	 */
	void exitMethodName(MethodPointcutParser.MethodNameContext ctx);
	/**
	 * Enter a parse tree produced by {@link MethodPointcutParser#args}.
	 * @param ctx the parse tree
	 */
	void enterArgs(MethodPointcutParser.ArgsContext ctx);
	/**
	 * Exit a parse tree produced by {@link MethodPointcutParser#args}.
	 * @param ctx the parse tree
	 */
	void exitArgs(MethodPointcutParser.ArgsContext ctx);
	/**
	 * Enter a parse tree produced by {@link MethodPointcutParser#argWildcard}.
	 * @param ctx the parse tree
	 */
	void enterArgWildcard(MethodPointcutParser.ArgWildcardContext ctx);
	/**
	 * Exit a parse tree produced by {@link MethodPointcutParser#argWildcard}.
	 * @param ctx the parse tree
	 */
	void exitArgWildcard(MethodPointcutParser.ArgWildcardContext ctx);
	/**
	 * Enter a parse tree produced by {@link MethodPointcutParser#arg}.
	 * @param ctx the parse tree
	 */
	void enterArg(MethodPointcutParser.ArgContext ctx);
	/**
	 * Exit a parse tree produced by {@link MethodPointcutParser#arg}.
	 * @param ctx the parse tree
	 */
	void exitArg(MethodPointcutParser.ArgContext ctx);
	/**
	 * Enter a parse tree produced by {@link MethodPointcutParser#exceptions}.
	 * @param ctx the parse tree
	 */
	void enterExceptions(MethodPointcutParser.ExceptionsContext ctx);
	/**
	 * Exit a parse tree produced by {@link MethodPointcutParser#exceptions}.
	 * @param ctx the parse tree
	 */
	void exitExceptions(MethodPointcutParser.ExceptionsContext ctx);
	/**
	 * Enter a parse tree produced by {@link MethodPointcutParser#exception}.
	 * @param ctx the parse tree
	 */
	void enterException(MethodPointcutParser.ExceptionContext ctx);
	/**
	 * Exit a parse tree produced by {@link MethodPointcutParser#exception}.
	 * @param ctx the parse tree
	 */
	void exitException(MethodPointcutParser.ExceptionContext ctx);
	/**
	 * Enter a parse tree produced by {@link MethodPointcutParser#typeName}.
	 * @param ctx the parse tree
	 */
	void enterTypeName(MethodPointcutParser.TypeNameContext ctx);
	/**
	 * Exit a parse tree produced by {@link MethodPointcutParser#typeName}.
	 * @param ctx the parse tree
	 */
	void exitTypeName(MethodPointcutParser.TypeNameContext ctx);
}