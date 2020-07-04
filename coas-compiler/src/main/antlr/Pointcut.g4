grammar Pointcut;

@header {
package pl.coas.compiler.instrumentation.antlr;
}

/*
 * Parser rules
 */
pointcutRule : alternatives ;

alternatives : conjunctions | conjunctions '||' alternatives ;

conjunctions : atom | atom '&&' conjunctions ;

atom : atomicRule | '(' pointcutRule ')' ;

atomicRule :
    'method(' methodRule ')' |
    'target(' targetRule ')' |
    'args(' argsRule ')' |
    'annotation(' annotationRule ')' |
    'annotatedType(' annotatedTypeRule ')' |
    'annotatedArgs(' annotatedArgsRule ')' ;

methodRule : method ;

targetRule : className ;

argsRule : args ;

annotationRule : className ;

annotatedTypeRule : className ;

annotatedArgsRule : typeNames ;

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

typeNames : typeName | typeName ',' typeNames ;

typeName    : IDENTIFIER '.' typeName | IDENTIFIER ;

/*
 * Lexer rules
 */

fragment DIGIT : [0-9] ;
fragment ALPHA : [a-zA-Z] ;

WHITESPACE : (' ' | '\t') -> skip;

IDENTIFIER : (ALPHA | '_' | '$' | '*') (ALPHA | DIGIT | '_' | '$' | '*')* ;