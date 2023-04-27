# Compiler Implementation
##This is a compiler for the object-oriented language L. It was implemented as part of the course "Compilation" at [University Name].

##The compiler consists of four phases: lexical analysis, parsing, semantic analysis, and code generation.

#Lexical Analysis
##The lexical analyzer is implemented using the open source tool JFlex. It reads in a stream of characters and produces a sequence of tokens. 
##[implemented in ex1]

#Parsing
##The parser is implemented using the open source tool CUP. It takes the output of the lexical analyzer and produces an abstract syntax tree (AST). 
##[implemented in ex2]

#Semantic Analysis
##The semantic analyzer recursively scans the AST and checks for semantic errors. It also adds metadata to the AST, which is used by the code generator.
###[implemented in ex3]

#Code Generation
##The code generator produces MIPS assembly code from the AST and then run it.
###[implemented in ex4]
