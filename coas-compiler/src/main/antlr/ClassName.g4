grammar ClassName;

@header {
package pl.coas.compiler.instrumentation.antlr;
}

/*
 * Parser rules
 */

classExpr    : className EOF ;

className    : typeName ;

typeName     : IDENTIFIER '.' typeName | IDENTIFIER ;

/*
 * Lexer rules
 */

fragment DIGIT : [0-9] ;
fragment ALPHA : [a-zA-Z] ;

WHITESPACE : (' ' | '\t') -> skip;

IDENTIFIER : (ALPHA | '_' | '$' | '*') (ALPHA | DIGIT | '_' | '$' | '*')* ;