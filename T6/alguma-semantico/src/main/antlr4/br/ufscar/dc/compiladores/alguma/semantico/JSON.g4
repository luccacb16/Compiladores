grammar JSON;

// Regras do lexer
STRING : '"' ( '\\' (['"\\/bfnrt'] | 'u' [0-9a-fA-F] [0-9a-fA-F] [0-9a-fA-F] [0-9a-fA-F]) | ~["\\])* '"' ;
NUMBER : '-'? [0-9]+ ('.' [0-9]+)? ([eE] [+\-]? [0-9]+)? ;
TRUE: 'true' ;
FALSE: 'false' ;
NULL: 'null' ;
LCURLY : '{' ;
RCURLY : '}' ;
LSQUARE: '[' ;
RSQUARE: ']' ;
COMMA: ',' ;
COLON: ':' ;

WS: [ \t\r\n]+ -> skip ;

// Regras do parser
json: value ;
value: STRING | NUMBER | obj | array | TRUE | FALSE | NULL ;

obj: LCURLY par_chave_valor (COMMA par_chave_valor)* RCURLY | LCURLY RCURLY ;
par_chave_valor: STRING COLON value ;
array: LSQUARE value (COMMA value)* RSQUARE | LSQUARE RSQUARE ;