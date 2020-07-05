grammar MethodPointcut;

@header {
package pl.coas.compiler.instrumentation.antlr;
}

/*
 * Parser rules
 */

method : kind modifiers returnType className '.' methodName '(' args ')' ('throwing' exceptions)? ;

kind        : 'static' | 'non-static' | ;

modifiers   : modifier modifiers | ;

modifier    : 'public' | 'private' | 'protected' | 'package-private' ;

returnType  : typeName ;

className   : typeName ;

methodName  : IDENTIFIER ;

args        : argWildcard | arg ',' args | arg | ;

argWildcard : '..' ;

arg         : typeName ;

exceptions  : exception | exception ',' exceptions ;

exception   : typeName ;

typeName    : IDENTIFIER '.' typeName | IDENTIFIER ;

/*
 * Lexer rules
 */

fragment DIGIT : [0-9] ;
fragment ALPHA : [a-zA-Z] ;
fragment SPECIAL : '_' | '$' | '^' | '/' | '*' | '?' | '[' | ']' | '-' ;

WHITESPACE : (' ' | '\t') -> skip;

IDENTIFIER : (ALPHA | DIGIT | SPECIAL)+ ;