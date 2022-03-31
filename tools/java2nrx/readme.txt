+-------------------------------------------------------------------------------+
|  Java 1.5 parser and Abstract Syntax Tree, translation to NetRexx             |
+-------------------------------------------------------------------------------+
|  Copyright (C) 2011 Marc Remes                                                |
|  remesm@gmail.com                                                             |
|  http://kenai.org/NetRexx/contrib/java2nrx                                    |
+-------------------------------------------------------------------------------+
|  This program is free software: you can redistribute it and/or modify         |
|  it under the terms of the GNU Lesser General Public License as published by  |
|  the Free Software Foundation, either version 3 of the License, or            |
|  (at your option) any later version.                                          |
|                                                                               |
|  This program is distributed in the hope that it will be useful,              |
|  but WITHOUT ANY WARRANTY; without even the implied warranty of               |
|  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the                |
|  GNU Lesser General Public License for more details.                          |
|                                                                               |
|  You should have received a copy of the GNU Lesser General Public License     |
|  along with this program.  If not, see <http://www.gnu.org/licenses/>.        |
+-------------------------------------------------------------------------------+

This package contains a Java 1.5 translator to NetRexx
It is based on the Java 1.5 Parser with AST generation and visitor support,
available from http://code.google.com/p/javaparser/

The translator translate Java 1.5 source code to NetRexx source code, to tne
extend on what is currently supported under NetRexx.

Use java2nrx.sh for convenience, place java2nrx.sh and java2nrx.jar in the
same directory. NetRexxC and java must be available on the path.
Set CLASSPATH appropriately.

Usage : java -jar java2nrx.jar -h
i.e.    java -jar java2nrx <infile.java> [out.nrx]
   Provide Java source file as input
   Optionally followed by NetRexx output source file, if not given output goes to stdout

Or java2nrx.sh/bat [-nrc] [-stdout] [-run] [other NetRexxC options] <java-source-file>
   -nrc      runs NetRexxC compiler on output nrx file
   -stdout   prints NetRexx file on stdout
   -run      runs generated translated NetRexx output file


Version history
---------------
1.0.0 (2011-09-27)

- Not supported (not translated, in code as /* **nonrx** */):
   - Annotations : @directives
   - Type declarations : <Types>
   - Enumerations : { GREEN, YELLOW, RED }
  Will stay that way, until NetRexx supports it


- Known issues: 
   - Inner classes : are placed inline
   - Number parsing : Java number format not always compatible with NetRexx
   - Switch statement : does not flow through in absence of break
   - Switch statement : break should not be printed
   - Statements that do not translate very well:
      - Unary expressions : x = ++y;
      - Conditional expressions : w = x ? y : z;

1.0.1 (2011-10-6)
	remesm@gmail.com

- Changes:
   - Removed semi-colon after package statement
   - Removed superfluous comment statements on annotations
   - Support for minor 'inner' classes
   - Changed eclipse build properties
      
1.0.2 (2012-06-23)
	remesm@gmail.com

- Changes:
   - Changed protected modifiers to inheritable
   - Fixed VariableDeclaration as array 
   - Fixed instanceof
   - Fixed static final properties 
   - Fixed protected methods 
   - Fixed synchronized methods and properties
   - Fixed transient properties
   - Fixed if then else statement on single line
   - Tentative support for anonymous classes
   - Modified strict comparison (<< >> <<= >>=) to not strict
   - Fixed for(;;) loop instruction
   - java2nrx.sh reads .java.2nrx files if existing
   - Modified Makefile, use 'make always java2nrx.jar' to trigger ant build
 
1.0.3 (2016-03-02)
   agrellum@gmail.com remesm@gmail.com

- Changes:
   - Fixed BooleanLiteralExpr
   - Fixed ThrowStmt
   - Fixed DoubleLiteralExpr
   - Fixed LongLiteralExpr
   - Update versions
   - Support java snippets

1.0.4 (2021-10-17)
   agrellum@gmail.com remesm@gmail.com

- Changes:
   - Pad each 'end' token with a space

1.0.5 (2022-03-28)
   remesm@gmail.com

- Changes:
   - Translate foreach statement as loop over
