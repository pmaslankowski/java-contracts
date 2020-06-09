// Generated from ArgsPointcut.g4 by ANTLR 4.7.2

package pl.coas.compiler.instrumentation.antlr;

import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link ArgsPointcutParser}.
 */
public interface ArgsPointcutListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by {@link ArgsPointcutParser#args}.
	 * @param ctx the parse tree
	 */
	void enterArgs(ArgsPointcutParser.ArgsContext ctx);
	/**
	 * Exit a parse tree produced by {@link ArgsPointcutParser#args}.
	 * @param ctx the parse tree
	 */
	void exitArgs(ArgsPointcutParser.ArgsContext ctx);
	/**
	 * Enter a parse tree produced by {@link ArgsPointcutParser#nonEmptyArgs}.
	 * @param ctx the parse tree
	 */
	void enterNonEmptyArgs(ArgsPointcutParser.NonEmptyArgsContext ctx);
	/**
	 * Exit a parse tree produced by {@link ArgsPointcutParser#nonEmptyArgs}.
	 * @param ctx the parse tree
	 */
	void exitNonEmptyArgs(ArgsPointcutParser.NonEmptyArgsContext ctx);
	/**
	 * Enter a parse tree produced by {@link ArgsPointcutParser#arg}.
	 * @param ctx the parse tree
	 */
	void enterArg(ArgsPointcutParser.ArgContext ctx);
	/**
	 * Exit a parse tree produced by {@link ArgsPointcutParser#arg}.
	 * @param ctx the parse tree
	 */
	void exitArg(ArgsPointcutParser.ArgContext ctx);
	/**
	 * Enter a parse tree produced by {@link ArgsPointcutParser#typeName}.
	 * @param ctx the parse tree
	 */
	void enterTypeName(ArgsPointcutParser.TypeNameContext ctx);
	/**
	 * Exit a parse tree produced by {@link ArgsPointcutParser#typeName}.
	 * @param ctx the parse tree
	 */
	void exitTypeName(ArgsPointcutParser.TypeNameContext ctx);
}