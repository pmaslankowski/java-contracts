// Generated from ClassPointcut.g4 by ANTLR 4.7.2

package pl.coas.compiler.instrumentation.antlr;

import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link ClassPointcutParser}.
 */
public interface ClassPointcutListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by {@link ClassPointcutParser#classExpr}.
	 * @param ctx the parse tree
	 */
	void enterClassExpr(ClassPointcutParser.ClassExprContext ctx);
	/**
	 * Exit a parse tree produced by {@link ClassPointcutParser#classExpr}.
	 * @param ctx the parse tree
	 */
	void exitClassExpr(ClassPointcutParser.ClassExprContext ctx);
	/**
	 * Enter a parse tree produced by {@link ClassPointcutParser#className}.
	 * @param ctx the parse tree
	 */
	void enterClassName(ClassPointcutParser.ClassNameContext ctx);
	/**
	 * Exit a parse tree produced by {@link ClassPointcutParser#className}.
	 * @param ctx the parse tree
	 */
	void exitClassName(ClassPointcutParser.ClassNameContext ctx);
	/**
	 * Enter a parse tree produced by {@link ClassPointcutParser#typeName}.
	 * @param ctx the parse tree
	 */
	void enterTypeName(ClassPointcutParser.TypeNameContext ctx);
	/**
	 * Exit a parse tree produced by {@link ClassPointcutParser#typeName}.
	 * @param ctx the parse tree
	 */
	void exitTypeName(ClassPointcutParser.TypeNameContext ctx);
}