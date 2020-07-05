grammar TypeNames;

@header {
package pl.coas.compiler.instrumentation.antlr;
}

/*
 * Parser rules
 */

args         : nonEmptyArgs? EOF ;

nonEmptyArgs : arg ',' nonEmptyArgs | arg ;

arg          : typeName ;

typeName     : IDENTIFIER '.' typeName | IDENTIFIER ;

/*
 * Lexer rules
 */

fragment DIGIT : [0-9] ;
fragment ALPHA : [a-zA-Z] ;
fragment SPECIAL : '_' | '$' | '^' | '/' | '*' | '?' | '[' | ']' | '-' ;

WHITESPACE : (' ' | '\t') -> skip;

IDENTIFIER : (ALPHA | DIGIT | SPECIAL)+ ;