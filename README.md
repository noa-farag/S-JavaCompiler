This program will recieve a single parameter (S-java source file), the program returns:
- 0 if the code is leagal.
- 1 if the code is illegal + the error message.
- 2 in case of IO errors + the error message.

S-Java:
a simple version of Java.
- S-Java file doesnt interact with other files, each file is standalone.
- type of components: variables, methods (variables can be shared between methods, methods can call
each other), comment lines, if and while blocks.

Variables in S-Java:
- int
- double
- string
- boolean
- char

Methods in S-Java:
- can contain if and while blocks
- can be called only from another method
- may not be declered inside another method

Our program will check if the code is legal such as the compiler does, if not it will return an indicative
error message.
