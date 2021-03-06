// Generated from ClassName.g4 by ANTLR 4.7.2

package pl.coas.compiler.instrumentation.antlr;

import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link ClassNameParser}.
 */
public interface ClassNameListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by {@link ClassNameParser#classExpr}.
	 * @param ctx the parse tree
	 */
	void enterClassExpr(ClassNameParser.ClassExprContext ctx);
	/**
	 * Exit a parse tree produced by {@link ClassNameParser#classExpr}.
	 * @param ctx the parse tree
	 */
	void exitClassExpr(ClassNameParser.ClassExprContext ctx);
	/**
	 * Enter a parse tree produced by {@link ClassNameParser#className}.
	 * @param ctx the parse tree
	 */
	void enterClassName(ClassNameParser.ClassNameContext ctx);
	/**
	 * Exit a parse tree produced by {@link ClassNameParser#className}.
	 * @param ctx the parse tree
	 */
	void exitClassName(ClassNameParser.ClassNameContext ctx);
	/**
	 * Enter a parse tree produced by {@link ClassNameParser#typeName}.
	 * @param ctx the parse tree
	 */
	void enterTypeName(ClassNameParser.TypeNameContext ctx);
	/**
	 * Exit a parse tree produced by {@link ClassNameParser#typeName}.
	 * @param ctx the parse tree
	 */
	void exitTypeName(ClassNameParser.TypeNameContext ctx);
}