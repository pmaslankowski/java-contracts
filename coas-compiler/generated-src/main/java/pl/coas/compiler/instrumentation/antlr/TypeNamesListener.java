// Generated from TypeNames.g4 by ANTLR 4.7.2

package pl.coas.compiler.instrumentation.antlr;

import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link TypeNamesParser}.
 */
public interface TypeNamesListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by {@link TypeNamesParser#args}.
	 * @param ctx the parse tree
	 */
	void enterArgs(TypeNamesParser.ArgsContext ctx);
	/**
	 * Exit a parse tree produced by {@link TypeNamesParser#args}.
	 * @param ctx the parse tree
	 */
	void exitArgs(TypeNamesParser.ArgsContext ctx);
	/**
	 * Enter a parse tree produced by {@link TypeNamesParser#nonEmptyArgs}.
	 * @param ctx the parse tree
	 */
	void enterNonEmptyArgs(TypeNamesParser.NonEmptyArgsContext ctx);
	/**
	 * Exit a parse tree produced by {@link TypeNamesParser#nonEmptyArgs}.
	 * @param ctx the parse tree
	 */
	void exitNonEmptyArgs(TypeNamesParser.NonEmptyArgsContext ctx);
	/**
	 * Enter a parse tree produced by {@link TypeNamesParser#arg}.
	 * @param ctx the parse tree
	 */
	void enterArg(TypeNamesParser.ArgContext ctx);
	/**
	 * Exit a parse tree produced by {@link TypeNamesParser#arg}.
	 * @param ctx the parse tree
	 */
	void exitArg(TypeNamesParser.ArgContext ctx);
	/**
	 * Enter a parse tree produced by {@link TypeNamesParser#typeName}.
	 * @param ctx the parse tree
	 */
	void enterTypeName(TypeNamesParser.TypeNameContext ctx);
	/**
	 * Exit a parse tree produced by {@link TypeNamesParser#typeName}.
	 * @param ctx the parse tree
	 */
	void exitTypeName(TypeNamesParser.TypeNameContext ctx);
}