# NetRexx 5.01-RC1 []

## Language
- New INTERPRET instruction
- -crossref option now lists classes, methods and properties
- NetRexx compiler now emits class file format version 52 files (jdk 1.8), both with -ecj as with -javac 
- new -targetvm option to compile to a higher class file version, with -ecj up to jdk 15 (class version 59), with -javac not higher than run-time Java
- more complete JRS223 javax.scripting framework
- more complete and faster Classic Rexx compatible stream support 

## Fixes
- NetRexx-21 superfluous warning on crossref option ("overrides program default")
- NetRexx-57 Spurious E output removed in SPECS stage with FS specified (Marc Remes)
- NetRexx-59 Java 9 and later disallow _ (underscore) as variable name, Java compiler fails (Marc Remes)
  (_ allowed again but Java sees $_)
- NetRexx-60 Javafx jar download location changed in readme. EnzoLCD example fixed.
- NetRexx-62 emoji escape code is incorrect
- NetRexx-63 parsing special words and variables
- NetRexx-66 pipc does not honour file name extension and possibly removes existing .nrx files
- NetRexx-67 exit code is not propagated when interpreting
- NetRexx-68 Pipelines JVM 23 removed ThreadGroup.stop() fixed (Marc Remes) 
- NetRexx-72 Pipelines diskr '<' wrongly constructs absolute path filename
- NetRexx-73 Emacs 30.1 does not colour fonts; fixed in netrexx-mode.el and still good for earlier Emacs

## Examples
- xref2uml.nrx, generate PlantUML inputfiles from crossref files
- rexxtry.nrx, every rexx needs its rexxtry
- rexxcps.nrx, corrected calculating clauses per second
- jsrbindings.nrx, retrieve bindings key values


# NetRexx 4.06 GA [2 Mar 2024]

## Fixes
- NetRexx-56 Variable interpolation in address statement (Marc Remes)
- NetRexx-49 Interpreter fails in synchronised section (Marc Remes)
- NetRexx-55 Unicode literal example fails to compile, interprets OK (Marc Remes)
- NetRexx-54 Interpreter fails with module issue while looping over char array (Marc Remes)

## Documentation
- new layout for the Pipelines railroad diagrams
- old railroad diagrams and chapters about Structured Data and Threadpool are removed from NRL
- NRL Available as printed book ("The NetRexx Language")

## Pipelines
- a new SELECT stage is added by Jeff Hennick

## Examples
- chaos fractal generator in examples/chaos illustrates how easy Swing components and other Java features are manipulated in NetRexx. And the spectacular imaginary images are free;

# NetRexx 4.05 GA [10 May 2023]

## Project
- an ant build.xml (in examples) to build an Android .apk for the NetRexx translator (Jason Martin)
- Reworked NetRexx Language Reference documentation
- Reworked NetRexx Pipelines Guide and Reference documentation

## Language
- a new option -deps to list dependencies
- a new option -noimplicituses to avoid automatic addition of Classic Rexx compatible classes to 'uses' -see below
- the package org.netrexx.address is now deprecated, because its work is better done by the ADDRESS statement
- #! shebangs are now supported for systems that support them, such as
  Linux and macOS - and ignored by the translator (Marc Remes)
- the launch script nrc now can be run with no CLASSPATH present (Marc Remes)
- The Classic Rexx (and ooRexx, Regina) functions user() and
  directory() are added to aid scripting.(René Jansen)
- #48 nrws.properties can now be .nrws.properties (though support for the filename without a dot is still there)
- when c2x and c2d are called in non-oo notation, these are compatible with Classic Rexx, which accepts strings.
- a Classic Rexx compatible random() function is added.

## Pipelines
- org.netrexx.njpipes.pipes.runner now reads pipeline source from stdin
  when invoked without arguments (Marc Remes) 
- a new ALL stage (Jeff Hennick)
- a new DELAY stage (Jeff Hennick)

## Fixes
- #38: the ADDRESS environment for SYSTEM is now resolved at runtime, previously a class compiled on another platform
  could look for a shell that was not there (Marc Remes)
- the sqlselect stage has an exception message protected against nullpointers from a failing JDBC driver
- #33: fix interpreter handling constant properties
- #35: Rexx date() now standard in all modes (scripting mode and standard mode) -noimplicituses option for breakage
- adapted qtime.nrx example for #35 (René Jansen)
- #40: detect unreachable code (Marc Remes)
- #41 The Workspace for NetRexx (nrws) now tries to avoid leaving _WS_*.nrx files after ending (René Jansen)
- #42 A settings.history=off property in nrws.properties can be used to avoid writing a nrws.history file (René Jansen)
- when interpreting, wait for all threads to complete after main() returned.
- better diagnostics on failed clgMain (compile-load-go) dynamic compiles, e.g. for pipes and their stage exits
- an error in RexxTime which could lead to a recursive call has been solved
- #43: Erroneous leading zero on Date() and Date('n') corrected, now following the language reference and standard
- allow lowercase 'date()' functions
- updated ADDRESSable bin/pipe shell script
- lifted restriction on macOS where due to an Apple caused 'feature' the home directory was not scanned for packages
  on startup of the translator.
  The restriction is now limited to the $HOME/Library hidden system directory on macOS, NetRexx will not find classes
  in that location.
- #47 a related issue (but for another part of the user home directory) is solved for windows

# NetRexx 4.04 GA [12 Sep 2022]

## Project
- the NetRexx issue tracker has moved to [https://github.com/RexxLA/NetRexx/issues]
- The RexxLA/NetRexx shadow repository will be maintained to be identical in contents to the SourceForge one

## Language
- non-oo notation for scripting mode (René Vincent Jansen) enables (much more) Classic Rexx source compatible scripting
- a new nr command for scripting purposes
- releasenotes are in Markdown syntax (and have moved to the root of the development tree)
- use of color in language processor messages enabled (but optional,  opt-out for Linux and Mac, opt-in for windows)
- a bugfix on an address statement ("variable $2 already defined") with interruptedexception
- add the netrexx/lang/Sysutils class by Marc Remes
- date('j') input format gave incorrect output and is fixed. This
  version 'century windows' around 75 for compatibility with z/VM CMS Rexx; in a coming release 2-digit years
  will be deprecated on date'j' input format.
- TRACE INT (interactive trace) added by Marc Remes

## Workspace for NetRexx
- you can now exit the NetRexx Workspace with an 'exit'
- resolved an error in the startup timer of nrws; timer display is now default
- the default now is to display the command timer instead of the window number
- the default prompt changed to 'Ready;' (but both options can still be changed in an $HOME/nrws.properties file)
- the pipelines processor is now loaded by default.

## Pipelines for NetRexx
- a pipelines example that uses the ADDRESS WITH functionality in a NetRexx script
- Stage locate can be abbreviate to locat, loc, lo and l

## Documentation
- The Quickstart Guide has been corrected to make all examples
  pasteable from the PDF - eliminated all 'typographical' quotes and
  hyphens.
- The Quickstart Guide now delivers some missing information on
  getting NetRexx to run.
- The Programming Guide has been brought up to date with modern JVM
  versions, which means that some scripting examples have
  disappeared, as the supporting functionality has been removed by
  Oracle.
- New documentation features will be in Markdown only (not visible to
  the user of the documents but opening up the production of document chapters
  to non-Latex users)
- TODO error codes in Pipelines Reference
- TODO debug levels in Pipelines Reference
- The file `stages.html` is now included in the documentation directory
- The file `methods.html` is now included in the documentation directory

# NetRexx 4.03 GA [03 May 2022]

- ADDRESS WITH implemented (Marc Remes)
- a fix for NETREXX-24 (was 148): ITERATE without label is not inside LOOP construct message (Marc Remes)
- a small fix for the MOD option (Jason Martin)
- correct slightly erroneous DataIO2.nrx example from NrxRedBk (René
  Vincent Jansen) (Thank you Terry Fuller)
- a fix for JavaFX usage with the JPMS (and any other imported module) (Marc Remes) (Thank you Terry Fuller)
- a new sample how to address https by Ruurd J. Idenburg
- A Classic Rexx inspired Stream I/O implementation (René V. Jansen)
  (Thank you Leslie Turiff for testing and issue reports)
- A new example of how to use a Swing Gui by Ruurd J. Idenburg
- Start of a new organisation of the examples directory by function
- a 'scripting mode' application adds 'uses RexxDate, RexxStream' to the generated class definition
- RxJrt now compiles under Java 8 and RxJrtApi.nrx under Java >=9 (Marc Remes)
- The java2nrx tool has been upgraded to v1.05  
- The distribution package now also contains java2nrx (previously only
  in source package)
- in scripting mode, 'parse pull' can be used as an equivalent of 'parse ask' 

# NetRexx 4.02 GA [Januari 24, 2022]

- for building NetRexx 4.0.2, Java 8 is now the lowest supported version
- tested and buildable from Java 8 up to and including Java 19ea
- including a fix for building on Java >=18
- NetRexx option (no)annotate to turn off pass through annotations
- NetRexxC.jar and NetRexxF.jar now contain the ANT task for NetRexx
- The ANT task is modified for the new -annotations option
- The ANT task is modified for the new -address option
- ANT in the source code repository upgraded to 1.10 for building NetRexx
- a new chapter in the Programming Guide documenting two ANT scenarios
- new special word asknoecho to request console input without echo, e.g. for passwords
- NetRexx option MOD for generation of tailored source representations (Jason Martin)
- documentation additions and corrections (numerous)
- the base font for all documents is now an open source version of Palatino (TeX Gyre Pagella, due to font copyright issues)

Pipeline additions, improvements:
- fix for arguments on command stage
- DIGEST stage (SHA1, SHA256, SHA512, MD2, MD5 and more).
- a small CMS-compatibility improvement for stage LITERAL (Jeff Hennick)
- a new pipr script that is guaranteed to work in a Docker container (Marc Remes)

# NetRexx 4.01 GA [March 20th, 2021]

- the first NetRexx release compatible with the JDK >=9 module system, thanks to Marc Remes
- contains all 3.11 GA functionality
- design goal is to be able to compile all existing NetRexx code
- the NetRexx interpreter also has been modified and tested
- finds all Java classes in the module system under JDK >=9
- retains compatibility with - and runs under - JDK8
- a new Eclipse java compiler is included, patched for NetRexx
- Between stage: bug fix for numeric second argument
- adds the ADDRESS [environment] statement compatible with Classic Rexx
- adds the DATE and TIME functions compatible with Classic Rexx
- a pipeline from a .njp file can now be run with the pipe runner
- a pipeline (including portrait mode specifications) can be executed from a .nrx file with ADDRESS PIPE

When testing this new release with current JDK levels, please take into account
that a number of earlier deprecated classes indeed have been removed in later JDK releases.
The translator not being able to find a class, specially in the javax packages, is not necessarily
a bug in NetRexx. For example, starting with JDK 11, the whole of JEE has been removed. Every module
that could be included, has been included with NetRexx 4.01 .


# NetRexx 3.11 GA [December 23rd, 2020]

- 3.10 is skipped as a release number, to avoid confusion with 3.01
- Diskr and diskw stages:enhanced detection of Windows-type path strings
- diskr and diskw stages fixed for relative filepaths
- COLLATE stage; with tests and documentation
- cons stage: add PRefix & PRompt keywords
- added USER to sqlselect.properties for some dbms
- change stage: add TO, FROM, TOFROM optional keywords
- command stage: add isWindows and cmd processing; better error message; documentation
- Stages decode64 & encode64
- Stages q, qu, que, quer as aliases for query
- Change stage: add FROM, TO, FROMTO, FOR, RESPECTCASE keywords
- ChangeRegex stage add FROM and TO options
- changeparse stage & abbreviations
- display stage
- htmlrow stage
- htmlrows stage
(all work on CMS/NetRexx Pipelines is by Jeff Hennick)
- the native executables and the docker image have been discontinued
  in light of NetRexx 4.01 becoming available 


# NetRexx 3.09 GA [October 1st, 2020]

- fix for ANSI escapes/colors in nrws on Windows operating systems
- bump version level of included JLine implementation
- fix a compiler warning when live executing a pipeline including stage UNIQUE
- enable multiline 'Portrait-mode' entering of pipeline definitions
- add the 'source' option to the 'query' stage to bring in line with CMS
  (note that this is not documented in the CMS manuals, but behaves identical)
- stage CONSOLE can now be abbreviated as in CMS
- stage XLATE can now be used as TRANSLATE and its abbreviations as in CMS
- add CASELESS to the abbrev stage to be CMS compliant (Jeff Hennick)
- BUFFER, APPEND, TAKE and COMPARE stages and tests (Jeff Hennick)
- documentation additions and improvements; added warning about Java 8 being the highest working version
- experimental native executable versions of the compiler for X86_64, versions of macOS and Linux for use with Java versions 9-14
- Added DROP FIRST number BYTES (secondary); * (secondary); negative stage (Jeff Hennick)
- BETWEEN stage hex and binary strings and tests (Jeff Hennick)
- many SPECs CMS compliancy improvements (Jeff Hennick)
- a new njp-model.el for emacs to correctly handle specs 1-*
- AGGRC stage added
- multithreading bug with multiple CONSOLE stages in a pipeline solved
- JOIN stage improved for CMS Compatibility (add COUNT and TERMINATE) (Jeff Hennick)
- DString bug solved (Jeff Hennick)
- FROMLABEL stage added (Jeff Hennick)
- FROMTARGET stage added (Jeff Hennick)
- FANINANY NOSTRICT added (Jeff Hennick)
- FANIN streams fixed (Jeff Hennick)
- DEAL fixes (Jeff Hennick)
- SPACE stage added
- LOCATE fixed for null input (Jeff Hennick)
- <, >, >> translation bug fixed, diskr, diskw and diska will now only be subsituted when first word in space specification
- HOSTBYNAME stage (Colin K.)
- PICKPARSE stage (Jeff Hennick)
- compare stage fix (Jeff Hennick)
- COMBINE stage (Jeff Hennick)
- STRTOLABEL stage (Jeff Hennick)
- DATECONVERT stage (Jeff Hennick)
- STRIP stage (Jeff Hennick)
- SNAKE stage (Jeff Hennick)
- SQL stage (Jeff Hennick)
- SQLSELECT add options: PROPERTIES, HEADERS, NOHEADERS, SELECT; documentation (Jeff Hennick
- ZONE stage (Jeff Hennick)
- VERIFY stage (Jeff Hennick)
- REGEX stage (Jeff Hennick)
- STRFRLABLE, STRFROMLABEL, STRFRLAB, STRFRLABL (Jeff Hennick)
- removed OVER stage, renamed to VAROVER
- PICK stage (Jeff Hennick)
- OVERLAY stage (Jeff Hennick)
- PAD stage add MODULO option (Jeff Hennick)


# Netrexx 3.08 GA [September 5th, 2019]

- restore OS/2 and Java 6 compatibility
- fixes a serious translator problem introduced in 3.07 - some classes ended up in the wrong files
- suppress generics-related warnings by adding a @SuppressWarnings annotation
- added a NetRexxC.clgMain method for compile, load and go 
- the pipes.runner class now (compiles and directly) runs a pipeline
- default stage separator is now | like on zVM
- added the VM abbreviations of >,<,>> for file I/O device drivers 
- pipes compiler now reliably finds classes it needs
- cleaned up pipes examples to make sure they all compile
- fixed C2X function on specs pipe stage
- added listzip stage
- added zip stage
- added unzip stage
- added -- (comment) stage (Jeff Hennick)
- improved take stage (Jeff Hennick)
- added pipe query stage for version info
- pipes compiler command is now called pipc
- added Pipes for NetRexx docs to distribution
- display failed summaries last - Jason Martin
- squelched the remark that we found a java compiler and other tourist information
- add an experimental rexxcps.nrx from the work of Mike Cowlishaw (for wider evaluation)
- added Joy programming language interpreter in examples/languages
- added two javafx examples in examples/javafx
- resurrected Martin Lafaix' NetRexx Workspace and added to NetRexx distribution (needs java 8)
- with added:
  - pipes execution in tenths of seconds
  - (without needing to quote the specification like in an OS shell)
  - command history and editing on par with zsh
  - multiline editing, history and amends
  - command alias and shell script is called nrws
- added and updated documentation:
  - moved the chapter on programmatic use of the translator from the Quick Start Guide to the Programmers's Guide
  - added a chapter on method resolution to the Programmer's Guide
  - added a chapter on the NetRexx Workspace (nrws) to the Quick Start Guide
  - updated, rewrote and expanded the Pipelines Quick Start Guide (renamed)
  - Docker image added to standard distribution

# NetRexx 3.07 [Oct 1st, 2018]

- Rexx() constructor no more package private (for use in other languages)
- Additions to RexxIO runtime class for more flexibility in say
  - setOutputStream
  - pushOutputStream
  - popOutputStream
- one-liner File read I/O in RexxIO().File.forEeachline()
- Sqlselect stage changes for Pipelines
- soundex() method on Rexx string
- documentation corrections by Gustavo Mindreau
- fix for class file image corruption problem with ecj compiles

# NetRexx 3.06 [December 10th, 2017]

- Method level annotations will pass through to Java source.

# NetRexx 3.05 [April 27th, 2017]

- First release after Kermit Kiser's passing. He will be remembered fondly for all his work.
- NetRexx has moved to Git on SourceForge for version management
- A new level of the Eclipse Java Compiler in NetRexxF.jar
- A build error on IBM's J9 JVM's was fixed
- The org.netrexx.address package was added as a foundation for OS shell interaction
- New documentary comments in nrc.bat and NetRexxC.bat for windows (Kermit Kiser)
- Java2Nrx and sysutils tools moved from contrib repository to tools (Marc Remes)
- All examples are now versioned under the master branch
- A fix for the RexxComparator class
- Various fixes and additions to documents.

# NetRexx 3.04 [June 1st, 2015]

- a jsr 199 compliant way of calling the java compiler
    - Java source is saved on disk by the 3.04 translator if options -keep or -keepasjava are specified just as in release 3.03 of NetRexx. However NetRexx 3.04 does not write java to disk otherwise since it is no longer required in order to produce class files.
- an automatic search for a compiler : this release of the translator also finds compilers that are not on the classpath if one cannot be found
   via the classpath; for example in the directory the translator is loaded from or in any directory associated with the current JVM. This
   may reduce installation issues due to "Java compiler not found".
- integration of Pipes for NetRexx (available in the NetRexxF.jar file)
- a new level of (4.4.2) of the eclipse java compiler (available in the NetRexxF.jar file)
- a less verbose startup message when the translator is started without options
  - NetRexxC option list now requires "-help"
- new: NETREXX-120, option -javac indicates a preference for the javac compiler
- new: NETREXX-120, option -ecj indicates a preference for the ecj compiler
- new: the tools section now has support for the vim and nano editors
- NetRexx now requires JVM 1.6 to build and run the translator
- new: NETREXX-69, do binary support
- a fix for NETREXX-117, NPE in jsr223 support
- a fix for NETREXX-116, a new 'returnobject' option to return a returncode from a jsr223 script
- a fix for NETREXX-114: fixes z/OS compatibility (broken in 3.03)
- new: NETREXX-109, a parallel processing api
- new: NETREXX-100, a list processing api
- a fix for NETREXX-92, enabling negative values in interfaces
- a fix for NETREXX-115, enabling trivial subclassing of some collection classes in java 8 and up
- new: NETREXX-119, toByteArray() method on type Rexx
- a fix for NETREXX-118, error in jsr223 engine
 - a fix for NETREXX-127, unneeded import of pipes and stages packages
- an enhancement to add wildcard (*) support to the NetRexxC -classpath operand matches Java behavior and also adds support for double wildcards (**) which will cause loading of jar files from nested directories (a feature lacking in Java). In addition, the enhanced compiler search will find compilers specified in the -classpath operand and via classpath wildcards. 
- The Java system property "nrx.compiler" can be used to provide options for the Java compiler called by NetRexx. This property is set on starting the NetRexx translator as in this example:

java -Dnrx.compiler="-target 1.6" org.netrexx.process.NetRexxC myprogram

If the first option specified is "javac" or "ecj", NetRexx will use that option to prefer selection of a compiler although the "-javac" and "-ecj" translator options will override it. Other options are passed to the Java compiler unchanged. If you are using the Windows script "nrc.bat" to compile programs, you can place the system property in the Windows environment to make it automatic as in this example:

set netrexx_java=-Dnrx.compiler="ecj -source 1.6 -target 1.6"

The nrx.compiler property can also be set directly in Ant builds or via the Ant project property "ant.netrexxc.javacompiler".

- a fix for NETREXX-134 -time output references correct compiler name
- a fix for a transient NullPointer exception when looking for compilers
- a fix for NETREXX-131 : add RuntimeConstants to enable building of the translator using ecj again


The Pipes for NetRexx documentation is not ready yet, but previews will be available from the NetRexx website.


# NetRexx 3.03 [May 23th, 2014]

Changes are:

- a jsr223 engine for NetRexx that enables interoperation with JavaScript (and AppleScript on MacOSX) - documented in the Programming Guide
- Java 8 compatibility:
       	 - a fix for the class parser to enable to recognize new bytecode instructions introduced in Java 7 but now used in Java 8
         - the ecj (Eclipse Java Compiler) has been upgraded to 4.4RC1, corresponding to v20140514-2138, 3.10.0, to enable compiles under Java 8
	 - a fix for issue NETREXX-108, adding default and static methods in interfaces that Java 8 allows
- improvements to UTF-8 source code compatibility (NETREXX-6)
- removed hardcoded locations for finding classes.jar on MacOSX and z/OS. MacOSX uses Sun/Oracle conventions now. z/OS support was outdated.
- documentation additions and improvements
- a fix for issue NETREXX-7, message change
- a fix for issue NETREXX-58, add a serialVersionID to class Rexx
- a fix for issue NETREXX-68, datatype(S) with euro or dollar symbol
- a fix for issue NETREXX-83, orphan file and missing entries in Diag.nrx
- a fix for issue NETREXX-85, unqualified properties statement
- a fix for issue NETREXX-107, NetRexx documentation naming convention
- a fix for issue NETREXX-108, static and default methods in interfaces
- the compiler will now run again with its classes loaded from the filesystem instead of from a jar file
- a change to the windows command scripts to automatically add the NetRexxF.jar to the classpath environment variable, and to default
  to the ecj compiler so its classes are found.


# NetRexx 3.02 [June 25th, 2013]

Changes are:

- New methods b2d() and d2b allow converting binary strings to decimal and decimal strings to binary.

b2d([n]) Binary to decimal. Converts the string (a string of binary characters)
to a decimal number, without rounding. If string is the null string, 0 is returned.
If n is not specified, string is taken to be an unsigned number.
Examples:
```
'01110'.b2d == 14
'10000001'.b2d == 129
'111110000001'.b2d == 3969
'1111111110000001'.b2d == 65409
'1100011011110000'.b2d == 50928
```
- If n is specified, string is taken as a signed number expressed in n binary
characters. If the most significant (left-most) bit is zero then the number is
positive; otherwise it is a negative number in twos-complement form. In both
cases it is converted to a NetRexx number which may, therefore, be negative. If n
is 0, 0 is always returned.
- If necessary, string is padded on the left with '0' characters (note, not signextended),
or truncated on the left, to length n characters; (that is, as though
string.right(n, '0') had been executed.)
Examples:
```
'10000001'.b2d(8) == -127
'10000001'.b2d(16) == 129
'1111000010000001'.b2d(16) == -3967
'1111000010000001'.b2d(12) == 129
'1111000010000001'.b2d(8) == -127
'1111000010000001'.b2d(4) == 1
'0000000000110001'.b2d(0) == 0
```

- d2b([n]) Decimal to binary. Returns a string of binary characters of length as
needed or of length n, which is the binary representation of the
decimal number. The returned string will use 0 and 1 characters for binary values.
string must be a whole number, and must be non-negative unless n is specified,
or an error will result. If n is not specified, the length of the result returned is
such that there are no leading 0 characters, unless string was equal to 0 (in which
case '0' is returned).
 - If n is specified it is the length of the final result in characters; that is, after
conversion the input string will be sign-extended to the required length (negative
numbers are converted assuming twos-complement form). If the number is too
big to fit into n characters, it will be truncated on the left. n must be a nonnegative
whole number.
Examples
```:
'0'.d2b == 0
'9'.d2b == 1001
'19'.d2b == 10011
'129'.d2b == 10000001
'129'.d2b(1) == 1
'129'.d2b(8) == 10000001
'127'.d2b(12) == 000001111111
'129'.d2b(16) == 0000000010000001
'257'.d2b(8) == 00000001
'-127'.d2b(8) == 10000001
'-127'.d2b(16) == 1111111110000001
'12'.d2b(0) == 
```

- A new "-classpath" operand allows dynamic specification of the classpath used by the NetRexxC compiler without having to depend on the CLASSPATH environment variable which is no longer recommended by Java authorities.

- Subclasses of the native NetRexx datatype "Rexx" are now supported.

- The NetRexxA interpreter API has a new "exiting()" method to check if the interpreter has been terminated by a program call to "exit". This is mainly useful to IDE developers who need to restart the interpreter in such cases.

- The default imports for NetRexx programs have been changed slightly: In the "strictimports" mode, the java.lang package is now imported automatically to match the behavior of the Java compiler. If not using strictimports, the base package javax.swing is now automatically imported to allow creation and sharing of simple graphical programs without manual importing. Mods to NRL page 91/111:

In the reference implementation, the fundamental NetRexx and Java package hierarchies are
automatically imported by default, as though the instructions:
```
import netrexx.lang.
import java.lang.
import java.io.
import java.util.
import java.net.
import java.awt.
import java.applet.
import javax.swing
```
had been executed before the program begins. In addition, classes in the current (working) directory
are imported if no package instruction is specified. If a package instruction is specified then all
classes in that package are imported.

strictimport Requires that all imported packages and classes be imported explicitly using import
instructions. That is, if in effect, there will be no automatic imports (see page 90),
except those related to the package instruction and the java.lang package automatically imported by the Java compiler.
This option only has an effect as a compiler option, and applies to all programs being
compiled.

- The proxy classloader used by the NetRexx interpreter is now linked to the Java thread context classloader when possible to allow the interpreter to run in custom or dynamic classloader environments like JSP servers.

- Support has been added for the Java collections framework: The NetRexx native "Rexx" datatype contains a Java Hashtable which is part of the Collections Framework. New classes, constructors and methods have been added to implement the Java Map interface and allow better interoperation with Java.
Some of the new collections support methods include "isindexed()" to check if a Map currently exists, "size()" to determine the count of map entries and "buildmap(sequence1,sequence2)" to construct Rexx maps from arrays or Java Lists.
Other classes and methods are documented in the Java Collections Map interface Javadocs.

"isindexed()" returns 0 if no indexed values exist and 1 if there is at least one indexed value in a Rexx object.

To build a new indexed Rexx map with the buildmap method you can do this:

Rexx(default).buildMap(keys, values)

where keys and values are any arrays or Java collections framework Lists and default is the default value for the Rexx variable (using the standard Rexx constructors). All elements are converted to strings before being added to the indexed Rexx variable which is returned. Null can be passed for one of the keys or values parameters to default to a 1-n integer sequence matching the other parameter but if both parameters are provided they must have the same length. Note that arrays do not need to be string arrays and that primitive arrays such as int[] are also accepted. 


- Several problems with using Java classes from NetRexx have been fixed with some minor changes to the method resolution algorithms. A new "strictmethods" option has been added for use with legacy programs that could change behavior with the new algorithms. This is considered extremely unlikely to occur. Project issues are NETREXX-17, NETREXX-55.

NRL page 59 has this note:

Note: When a method is found in a class, superclasses of that class are not searched for methods, even though a lower-cost method may exist in a superclass.

The above information is no longer correct as NetRexx now compares the cost of using superclass methods to the cost of using subclass methods when performing inexact method searches. Here is the replacement note:

Note: When a method that is not an exact match to a call is found in a class, superclasses of that class are also searched for methods which may have a lower-cost of conversion and the method with the lowest cost, hence the closest match, is used to resolve the search. 

- Covariant Return Types
NetRexx now allows covariant return types such as have been allowed in Java since the version 1.5 release. Prior to Java 1.5, in order for a method to override or implement a method from another class, the return type of the methods had to be an exact match. Since the Java 1.5 release, methods which override a superclass method or implement an interface class method are allowed to have a return type which is a subclass of the return type of the method replaced or implemented. An exact match is no longer required.

A fix for issue NETREXX-100 in which unknown program options were silently ignored. Now, there is a warning issued, analogous to what happens with a unknown command line option.

# NetRexx 3.01 [23 Aug 2012]

The 3.01 release will mark the first official RexxLA release. Changes are:

- the package name for the translator has been changed from COM.ibm.netrexx.process to org.netrexx.process. This is the first package name change since release 0.90 (17 Dec 1996) when the translator package name was changed from netrexx.process to COM.ibm.netrexx.process; this was to comply with a short-lived naming convention for Java in which the first qualifier should be an uppercased top-level domain name; NetRexx was one of the few packages that followed this advice.

- the 'loop over' construct now also works for collection classes and behaves the same way as the 'loop over' for enumerations -NETREXX-13 (Kermit Kiser and Patric Bechtel)

- a bug in 2.05 (and probably dating back to earlier releases) in which a translator commandline option of '-nocrossref' that was overridden by a program option of 'crossref' led to a NullPointerException in RxStreamer.crossref() has been resolved (NETREXX-28). A warning for conflicting options is issued, while the crossref file object pointer is allocated.

- the option '-keepasjava' is added, which removes the necessity to rename *.java.keep files to *.java when used

- the translator displays a version number and build date on startup

- addition of several NetRexx examples from the RosettaCode.org site ( http://rosettacode.org ) and the IBM Redbook

- an example for the use of NetRexx with MS-Excel on Windows systems, using the Jacob library

- a new NetRexx Language Reference donated by Mike Cowlishaw, which incorporates the NetRexx 2 Language Reference, the NetRexx Overview and the Language Supplement

- a new version of the NetRexx User's Guide, now renamed to NetRexx Quickstart Guide

- a new API to translate (interpret and/or compile) a NetRexx source from a memory buffer instead of a file - Kermit Kiser (NETREXX-5).

- a solution for NETREXX-18, Universal build support with an ant task. NetRexx can now be built on any Java SDK in a platform independent way; bootstrap compiler is in the version repository (Patric Bechtel, Kermit Kiser)

- an updated Ant NetRexxC task (ant-netrexx.jar) is included with support for file names containing spaces and for nested classpath elements.

- The Java CharSequence class is now recognized by NetRexx and treated like the Java String class with automatic conversions to and from Rexx objects as needed.

- changed wording and addition of some license related information, in anticipation of IBM's response to our query

- added 100+ Rosettacode.org examples by Alan Sampson.

- bug fixes for java code generation for LOOP OVER for Rexx and primitive types introduced in 3.01 release candidate 2

- addition of the eclipse batch compiler, (issue NETREXX-4) an alternative for javac that enables ahead-of-time compilation on a JRE and in cases that javac cannot be located on the user system
support for the eclipse batch compiler is turned on by placing the ecj-4.2.0.jar file on the classpath and adding the -Dnrx-compiler=ecj to the compile commandline (the java org.netrexx.process.NetRexxC commandline.

This package structure will be built upon in the coming releases.


# NetRexx 3.00 [11 Jun 2011]

The 3.00 prototype release is the first build of the sourcecode by RexxLA. Changes with regard to the last IBM release (2.05) are:

- added support for some platforms in finding the Java runtime jar files
- small source changes to make the translator compilable with Java 1.5 and higher
- addition of ICU copyright notices to the source files


# NetRexx  2.02 [22 May 2001] 

This is a maintenance release; loop i=a to b until x incorrectly optimized the control variable test in some circumstances. No other changes are included.


# NetRexx 2.01 [1 Apr 2001] 

This is a maintenance release which corrects excessive memory usage when large numbers of files are imported and the -prompt option is used. No other changes are included.


# NetRexx  2.00 [26 Aug 2000] 

This is a major new release, which consolidates the changes of NetRexx 1.1 and adds the NetRexx interpreter and improved documentation. The enhancements are:
- The various installation and user documents have been consolidated into a new expanded and indexed User's Guide, available in both HTML and PDF (Acrobat) formats.
- The reference implementation now includes the NetRexx interpreter, which allows programs and classes to be run without being compiled, together with a new API which makes it easy to use the interpreter from NetRexx or Java applications.
- The new -prompt option, which lets the translator be used repeatedly without requiring re-loading. This allows sub-second compilation and interpretation of NetRexx programs.
- The structure of the NetRexx package has been revised to make installation and maintenance simpler. Shell scripts for Linux have been added. Please see the new NetRexx User's Guide for details.
- The Language Overview (quick start) has been updated and is now also available in PDF (Acrobat) format for viewing or printing.
- A warning is now given if a private method in a class is not referenced.
- The compact option for compact error messages has now been documented (see the NetRexx Supplement for details).
- The documentation was inconsistent as regards the file name generated when -nocompile was specified; the intent was that NetRexx should never leave a plain .java file on disk, as this prevents the next compilation if unprocessed. The documentation and code have been fixed to ensure that -nocompile exactly implies -keep.
Several performance optimizations have been added.

# NetRexx 1.1xx

The following changes are those which were made in NetRexx 1.1xx releases. NetRexx 1.1xx releases require Java 1.1.0 (or later).
Updates:


# NetRexx 1.160 [10 Feb 2000] 

This release has some language enhancements, along with some problem fixes and other improvements:
- The if clause in the if instruction and the when clause in the select instruction have both been enhanced to accept multiple expressions, separated by commas. These are evaluated in turn from left to right, and if the result of any evaluation is 1 (or equals the case expression for a when clause in a select case instruction) then the test has succeeded and the instruction following the associated then clause is executed.

Note that once an expression evaluation has resulted in a successful test, no further expressions in the clause are evaluated. So, for example, in:
```
-- assume name is a string
if name=null, name='' then say 'Empty'
```
then if name does not refer to an object it will compare equal to null and the say instruction will be executed without evaluating the second expression in the if clause.

Here is an example in a select case instruction:
```
select case i
  when 1 then say 'one'
  when 2 then say 'two'
  when 3, 4, 5 then say 'many'
end
```
- The select case instruction will now generate a Java switch instruction under the right conditions. See the NetRexx Supplement for details.
- The new nojava option allows Java code generation to be inhibited. This can be used to speed up a syntax checking run, when no compilation or Java source code is required.
- Invoking NetRexxC with no arguments will now display all options, not just the 'outer level' options.
- The class Exception is now treated as a Checked exception (as Java does).
- Calls to super() in dependent classes may now be qualified by parent. as well as by constructor arguments, if appropriate.
.jar files in the /lib/ext (automatic extensions in Java 2 [1.2]) are automatically added to the classpath.
- Classpaths containing multiple quoted segments are now handled correctly, and various other minor problems have been fixed.
- Several optimizations and improvements to formatting have been added.


# NetRexx 1.151 [3 Sep 1999] 

This refresh has some minor enhancements:
- The 'direct call from Java' entry points have been enhanced to allow paths with embedded blanks to be specified. See the NetRexx User's Guide (Using the translator as a Compiler).
- Several improvements in code generation when incrementing and decrementing integers.
- This release has been tested under the first Java 1.3 beta; no problems were found and no changes from earlier NetRexx 1.1 releases were necessary.

# NetRexx 1.150 [23 Jul 1999]

This release is a maintenance update with some minor enhancements:
New unused modifier on the properties instruction may be used (in conjunction with private only) to indicate that a private property is not used. This keyword will stop the compiler warning that a property is not used. For example:
```
properties private constant unused
copyrt="Copyright (C) Spel Corp., 1999"
```
New strictprops compiler option requires that references to properties, even from within the same class as the property, be qualified (either by this. or the name of the class). This can be useful for large and complex classes.
Several improvements in code generation, mostly for testing of equality.
Calls to this() and super() in minor classes will no longer attempt to refer to generated constants.
1.148
[21 Dec 1998] This release makes significant improvements in importing classes and in the select instruction:
The select instruction now adds a case keyword, which lets an expression be evaluated once and then tested in each when clause. For example:
```
i=1
select case i+1
  when 1 then say 'one'
  when 2 then say 'two'
  when 3 then say 'three'
end
```
See the NetRexx Supplement for details.
- An explicit class import will now disambiguate short references. For example, after import java.awt.List a reference to the class List would refer to that class, not the class java.util.List introduced in Java 1.2.
- Several improvements in code generation, including the treatment of small integers as, for example, byte without need for explicit casts.
- The format method in the Rexx class has been corrected to completely follow the ANSI X3-274 definition and the NetRexx specification.


# NetRexx 1.144 [21 Oct 1998]

This maintenance release primarily allows more explicit control over the compiler, for working with 'minimal' virtual machines.
- New strictimport compiler option prevents any automatic class imports (even java.lang.Object). This can be useful when compiling programs for reduced-function JVMs for embedded systems and palm-sized devices.
- The package java.math is no longer imported automatically.
- Occasional incorrect loop termination when trace is in use has been corrected.


# NetRexx 1.142 [1 Sep 1998] 
This version is a maintenance release, primarily to support changes in the Java Development Kit (JDK) introduced for Java 1.2. Please see the NetRexx User's Guide for details for additions to the class path needed to run under Java 1.2.
The other changes are:
- A type on the left hand side of an operator that could be a prefix operator (+, -, or \) is now assumed to imply a cast, rather than being an error.
For example: x=int -1
- Improved code generation for for and to loops.
- The euro character ('\u20ac') is now treated in the same way as the dollar character (that is, it may be used in the names of variables and other identifiers). Note that only UTF8-encoded source files can currently use the euro character, and a 1.1.7 (or later) version of a Java compiler is needed to generate the class files.
- The arithmetic routines have slightly improved performance, and provide accurate binary floating point conversions for constants.
- More robust handling of import, and import from classpath root segments generalized
- Improved error messages when an indirect property is initialized with a forward reference.


# NetRexx 1.140 [26 May 1998]

Three enhancements have been made to tracing:
1)The new var option on trace lets changes to named variables be traced selectively. For example:
trace var a b c
requests that whenever the variables a, b, or c are changed (either directly or using an index), the line changing them and their new values should be traced. Variables may be added to or removed from the list as required.
2) The trace instruction may now be used before the first class instruction; it then applies to all classes in a program.
3) Context is now shown while tracing – if a trace line is produced
  from a different program or thread than the preceding trace line,
  then an indicator line (prefixed with ---) is displayed.
  
- The numeric instruction may now be used before the first class instruction; it then applies to all classes in a program.
- The new -savelog NetRexxC option requests that compiler messages be written to the file NetRexxC.log in the current directory. The messages are also displayed on the console, unless -noconsole is specified.
- The new -noconsole NetRexxC option requests that compiler messages not be written to the console.
- When calling the compiler directly from NetRexx or Java, a PrintWriter can now be provided; messages are then written to that stream (see the NetRexx User's Guide for details).
- A catch clause may now specify an exception that is a subclass of an exception signalled in the body of its construct.
- The leave and iterate instructions may now be used in the catch and finally clauses of nested loops.
- Many improvements to the formatting of generated Java code have been made (plain-name labels, fewer braces, better comments handling, etc.).
- A constant indirect property may now be changed by methods in its class, though no set method for it is generated or permitted.
- Several performance improvements and optimizations have been added, improving both run time and compilation time. If you have a long CLASSPATH or many files in directories, you may see a 20% or better reduction in compile time.
- The NetRexxC.cmd and .bat files now add the value of the NETREXX_JAVA environment variable to the options passed to java.exe. For example, SET NETREXX_JAVA=-mx24M changes the maximum Java heap size to 24 MegaBytes. Try this if you see a java.lang.OutOfMemoryError while running the compiler.
- Several related problems with loading minor classes from directories and zip files have been corrected.
- Parentheses around sub-expressions were incorrectly optimized out in some situations; they are now preserved.
- A work-around for a problem caused by empty directories on the CLASSPATH in Linux has been added.

# NetRexx 1.132 [15 Apr 1998]

This version includes one major enhancement: support for Minor and Dependent classes - Java's Nested and Member (inner) classes, using simplified syntax and concepts.


# NetRexx  1.130 [8 Mar 1998]

- The new copyIndexed method on the Rexx class allows the sub-values (indexed strings) of one Rexx object to be merged into the sub-value collection of another Rexx object [available in runtime since NetRexx 1.120].
- The '$' character is now permitted in variable and other names.
- It is now an error to attempt to use a concatenate operator on an array (unless the array is of type char[]).
- The methods generated for indirect properties are no longer inhibited by methods of the same name in superclasses.
- The NetRexx Supplement has been updated to document changes since August 1997.

# NetRexx 1.128 [14 Feb 1998]

- The new linecomment example is a small command-line application that processes a text file. It demonstrates the use of Readers and Writers, and exception handling.
- A workaround for a bug in javac in JDK1.2b2 has been included.
- Retry of a failing do instruction as a loop instruction now works.
- '\1a' (EOF) characters no longer need to follow line-end sequences in order to be ignored.
- Import of package hierarchies from .zip or .jar files now works correctly (previously it only worked for the standard imports)


# NetRexx  1.125 [10 Jan 1998]

- The new sourcedir option requests that all .class files be placed in the same directory as the source file from which they are compiled. Other output files are already placed in that directory. Note that using this option will prevent -run from working unless the source directory is the current directory.
- The new explicit option indicates that all local variables must be explicitly declared (by assigning them a type but no value) before assigning any value to them.
- Indexed strings are now serializable (can be made persistent).
- Minor improvements to generated code.


# NetRexx 1.122 [27 Nov 1997]

- A workaround for a JIT bug in Java 1.1.4 (showing as an exception in an optioncheck method during compilation) has been included.
- Formatting for the Java code when the comments option is used has been improved.
- strictcase and nostrictcase programs can now be safely mixed in a single compilation.
- Minor improvements to generated code and performance.


# NetRexx 1.121 [21 Oct 1997]

- The new experimental comments option copies comments from the NetRexx source program through to the .java output file, which may be saved using the keep command option.
- Decimal addition has been updated to conform to ANSI X3-274 arithmetic and the NetRexx documentation (this is a very minor change: an addition such as 77+1E-999 now pads with zeros).
- An abstract method in an abstract class was incorrectly reported as error.
- Minor improvements to error messages, formatting, and performance.


# NetRexx 1.120 [1 Sep 1997]

- Minor improvements to error messages, signals handling, and performance.
- Redesigned web pages and improved documentation.


# NetRexx 1.113 [3 Aug 1997]

- Multiple .java files are compiled using a single call to javac, giving improved performance and interdependency resolution.
- Individual methods may be designated as binary, using the binary keyword.
- Numerous 'cosmetic' improvements in error messages, formatting, etc.


# NetRexx 1.104 [22 Jul 1997]

- Whole numbers may now be expressed in a hexadecimal or binary notation, for example: 0xbeef 2x81 8b10101010 - see the Supplement for details.
- Conversions from String to Rexx (etc.) now 'pass through' nulls, rather than raising NullPointerException.
- options symbols may be used to include debugging information (a symbol table) in the generated .class files.
- Numerous 'cosmetic' improvements in error messages, formatting, etc.


# NetRexx 1.103 [3 Jul 1997]

- A new modifier, adapter, for classes has been introduced. This makes it easy to use Java 1.1 events, without the complexity and extra nesting of Java Inner Classes. Please see the Supplement for details, and the new Scribble sample for a simple example.
- Compressed Zip files as produced by the Java 1.1 jar utility ('jar files') can now be used for class file collections. The current NetRexxC.zip file is such a file.
- The NetRexx string class, netrexx.lang.Rexx, is now serializable.
- The compiler now uses the Java 1.1 Writer and Reader classes for reading and writing text files; this means that the text code page in use on your machine will be automatically translated to and from Unicode for use by the compiler.
- Associated with the previous change, options utf8 must now be consistent with the options passed to the compiler (see the Supplement for details).
- The NetRexxC.properties (error messages) file is now included as a resource in the NetRexxC.zip file. The copy in the \lib directory is no longer needed, nor is the NETREXX_HOME environment variable (if you needed to use that before).
- The Pinger and Spectrum sample applications have been updated to use the Java 1.1 event model; Pinger has also had some other minor improvements.
- Performance improvements reduce start-up time when compiling with a long CLASSPATH or with class directories with large numbers of files.


# NetRexx 1.0x

- This release is the reference implementation for NetRexx 1.00, as published in The NetRexx Language Definition, and later updates. NetRexx 1.0x updates will run on Java 1.0.1 or any later releases, though certain new features may require a Java 1.1 compiler to compile the generated Java code.
Updates:


# NetRexx 1.02 [25 Jun 1997]

- You can now add the shared keyword to the method or properties instructions to indicate that the method or a following property has shared access (that is, is accessible to other classes in the same package, but not to other classes). This corresponds to the Java 1.1 'default access' visibility. Please see the NetRexx Supplement for details.
- The new sourceline special name may be used to return the line number of the current clause in the program. Please see the NetRexx Supplement for details.
Array initializers have been added. These allow arrays to be created and assigned an initial value, for example: x=['one','two','three'] Note that Java 1.1 is needed to use this enhancement. Please see the NetRexx Supplement for details.
- The property and method access rules have been enforced according to the current Java specification, along with enhanced error messages when the rules are infringed.


# NetRexx 1.01 [15 Jun 1997]

- The NetRexx Supplement has been added. This documents language enhancements and the netrexx.lang package.
- NetRexxC now displays a warning when it encounters any deprecated (out-of-date or no longer recommended) class, method, or property for the first time in a program.
- Note that under Java 1.1, the javac compiler always displays at least one message if any deprecated fields or classes are encountered. The invitation to 'Recompile with "-deprecation" for details' can be ignored.
- You can now add the deprecated keyword to the class, method, or properties instructions to indicate that the following class, method, or properties are deprecated. You have to run with a Java 1.1 compiler for this to be reflected in the .class file.
- Methods and properties with the same name are now permitted (and can be accessed).
- An import of one of the standard packages (for example, java.io) no longer causes the classpath to be searched. This makes redundant standard imports much faster.


# NetRexx  1.00 [24 May 1997] 

Cosmetic changes:
- Methods listed during compilation now have their argument types listed (if any)
- Methods generated from Indirect Properties are now listed.
- The installation instructions now include instructions for using NetRexx with Visual J++.
- A reference to java.awt.image.ImageObserver treated java.awt.image
  as a class reference rather than as a package name; it will now
  correctly refer to the ImageObserver class.
  
[6 May 1997]
- Multiple file concurrent compilation: when two or more programs are specifed on the NetRexxC command, they are all compiled with the same class context: that is, they can 'see' the classes, properties, and methods of the other programs being compiled, much as though they were all in one file. This allows mutually interdependent programs and classes to be compiled in a single operation, while maintaining their independence (the programs may have different options, import, and package instructions).
- Compiling programs together in this way also gives substantial performance improvements, as the classes for NetRexxC and the javac compiler are only loaded once for all the files being compiled. See Using the translator as a Compiler in the NetRexx User's Guide for full details.
- The warning 'Method argument not used' will now only be given if the strictargs option is specified.
- The '.crossref' and '.java.keep' files resulting from a compilation now are placed in the same directory as the source file (instead of the current directory). The multiple compilation support also requires that the source directory be writeable.
- import of a package (with no trailing period) was not accepted by the compiler; this should now work correctly.

[15 Apr 1997]
- Preliminary, experimental, support for JavaBeans is now available in the NetRexxC compiler. It is described in the NetRexx Supplement.
- Checking has been added for the use of Java reserved words as externally visible names (such properties, method, and class names cannot be accessed by people writing in the Java language).
- The translator phase of the compiler has numerous performance improvements, and now runs 35% faster than the first (January) 1.00 release.
Forward references from property initialization expressions to methods in the current class are now permitted, providing they are not circular.
Several improvements have been made to error and progress messages.

[13 Mar 1997]
- The source and documentation for the Tablet (navigation tabs) applet have been added to the package.
- Forward references involving default constructors now work correctly.
- The .equals method was not being used for '=' and '\=' comparisons of subclassed objects.
- options nodecimal may be used to report the use of decimal arithmetic as an error, for performance-critical applications.

[18 Feb 1997]
- Minor improvements to the compiler for error messages, localization, and Java 1.1.
- The Say instruction can now handle all expressions that evaluate to null.

[6 Feb 1997]
- LOOP OVER did not correctly snapshot indexed strings with 'hidden' elements.
- Some unused method arguments were not being reported as unused.
- Minor improvements to error messages, progress messages, and code generation.

[3 Jan 1997]
- Minor cosmetic and performance improvements over 0.90.
- NetRexxC.bat and nrc.bat have been added to the NetRexx package.


# NetRexx 0.90 [17 Dec 1996]

This release is the 'gamma' release prior to NetRexx 1.00. The main changes are in packaging and installation.

- The compiler name and classes have been moved from the netrexx.process package to the COM.ibm.netrexx.process package, to comply with the Java language recommendations. The compiler name is therefore now COM.ibm.netrexx.process.NetRexxC.
- The compiler and runtime classes are now packaged in a Zip file that lets them be used directly by Java without unzipping the individual classes. To use the Zip file in this way, it must be included in the CLASSPATH setting - see the User's Guide.
- A similar Zip file, with just the runtime classes, is also included, for those who need to distribute these with some other program.
- The cross-reference listing is now written to a separate file (xxx.crossref) instead of to the standard output stream. This makes it easier to see errors and warnings.
Reports of variables that are set but not used are now reported as Warnings, rather than as incidental information at the end of the cross-reference listing.
- A defined entry-point in NetRexxC allows the compiler to be invoked directly from NetRexx or Java programs.
- Several cosmetic improvements, notably to tracing of loop instructions.


# NetRexx 0.88 [1 Dec 1996]

With this release the full function intended for NetRexx 1.00 is available.

- The power operator (for both the Rexx class and primitive types) is now implemented.
- The format, insert, overlay, and trunc methods of the Rexx class are now implemented.
- Conversion from double to a string now provides 15 digits of precision instead of the 6 digits supplied by Java.
- Tracing of loop instructions has been improved, and other cosmetic changes have been made to messages and other compiler output.
- Forward references from the class instruction to other classes in a program are now permitted.
- The generated Java source code no longer includes import instructions (i.e., all class references are now qualified).
- options compact may be used to request compact warnings and error messages, intended for use by editing environments. Please consider this an 'experimental' option; the format may change.


# NetRexx 0.86 [14 Nov 1996]

The major change in this release is the new error message processing.

- The NetRexx compiler now has the completely new error message processing that has been 'in the works' for some time. Error messages should now be much more helpful, and (where useful) more than one error message may be produced during a compilation.
- Error messages are held in a separate file (NetRexxC.properties), which allows for translation. This file will be installed automatically in the Java 'lib' directory when the NetRexx compiler is installed (if you don't have access to the Java 'lib' directory, an alternative path can be identified – see the NetRexx installation document).
Interface classes should now use the implements keyword for extending other interface classes, because in the .class files, all interface classes must extend java.lang.Object. This change also allows interface classes to extend more than one other interface class. The extends keyword will continue to work for a single class (for the time being), with a warning.
- Various problems with import and qualified class references have been resolved; in particular, you should now be warned about ambiguous class references again.
- options trace1 may be used to redirect trace output to stdout instead of stderr. Please consider this an 'experimental' option.


# NetRexx 0.84 [29 Oct 1996]

- Local variables now have a defined initial value, consistent with properties (variables of primitive type are initialized to 0, all others to null).
- The Rexx class now has a new sequence(final) method for generating a sequence of characters. This serves the same purpose as the Rexx and Object Rexx xrange function.
- The compare, delstr, delword, d2x, translate, and x2d methods of the Rexx class are now implemented.
- The values of method arguments are now traced, for trace all and trace methods.
- The numeric instruction is now allowed in binary classes.
- Terms may now start with numeric symbols (for example, 12.max(i)).
- Interface classes may now be extended, and methods in Interface classes may now have Signals lists.
- The source and version special words now always return strings of class Rexx.
- Many improvements and clarifications in the documentation.


# NetRexx 0.82 [26 Sep 1996]

- Unicode characters (accented characters, non-arabic numerals, etc.) are now allowed for identifiers (variable and other names) and numeric symbols (numbers in programs). By default, characters in the source file are assumed to be in the "Latin-1" character set (the first 256 Unicode characters).
- The new utf8 option indicates that the source file is encoded with UTF-8 encoding. This allows the full range of up to 65536 Unicode characters to be used in NetRexx programs (including in literal strings and comments). To make best use of this you'll need an editor or utility that can produce files in UTF-8 format.
- The upper and lower methods of the Rexx class have been extended to allow uppercasing or lowercasing of substrings.
- The Rexx class has now got a new exists(name) method for testing
  whether an indexed variable called name exists. Also, an indexed
  variable can be 'reset' (or 'dropped') by assigning null to it, for
  example:
  ```
  fred='?'      -- fred.exists('3') is now 0
  fred[3]='abc' -- fred.exists('3') is now 1
  fred[3]=null  -- fred.exists('3') is now 0
```
- Improved error messages.


# NetRexx 0.81 [7 Sep 1996]

- The translator now parses .class files directly, so no longer uses the Agent debugging class. This gives the translator full information about classes and methods, and has allowed the removal of several restrictions.
- The translator can now track 'checked exceptions', and will automatically add those that are not caught to the signals list for each method. When this is done, the added exceptions are listed by the translator.
- options strictsignal causes checked exceptions that are not caught to be treated as an error (as in Java).
- The following methods in the Rexx class are now implemented:
```
b2x, x2b, x2c, c2x, max, min
```
- The abs method in the Rexx class no longer rounds to nine digits.
- The verbose settings have been reworked to reduce the amount of output that the translator produces (though all the previously generated information is still available at verbose4 or verbose5).
- Methods that override methods in superclasses or interfaces now list the overridden or implemented methods. Additional checking, as required for Java, reports mismatches of the return types of overridden methods, etc.
- Using a variable name that matches (hides) the name of a Type that has previously been referenced as a type by a short name now gives a warning (this will probably change to be an error, later).
- Attempts to subclass (extend) private or final classes are now reported as errors.
- 'Unreachable' statements are now reported, as in Java.
- A method called 'main' with argument type 'String[]' is assumed static. This now produces a warning.
- When '=' or '\=' are used to compare two objects that are unknown to NetRexx (i.e., not strings), the Java 'equals' method is used for the comparison. '==' and '\==' check for identical objects, as before.
- The translator now exits with a return value of 2 if an error was detected, 1 if a warning was displayed, and 0 otherwise.


# NetRexx 0.77 [26 Aug 1996]

- New uses keyword on the class instruction lets you specify one or
  more classes that will be used as a source for static methods and
  properties. For example, if your class is called Fred, then after
```
class Fred uses Math
```
you can refer to the properties and methods in the class
  java.lang.Math simply by name, without qualifying them by the class
  name. For example:
```
say sin(PI/4)
```
-Case-insensitivity is now fully implemented. It is no longer
  necessary to use the exact case for external classes and methods
  (unless you want to, see below). For example you can invoke the
  reverse() method in the Rexx class using any of:
```
'data'.reverse
'data'.Reverse
'data'.ReVerse
```
- Options strictcase can be used to enforce strict case checking (for local variables, methods, and properties as well as for external methods and properties), for those that prefer it. When in effect, all name matches must be exact.
- Improvements to Trace: for example, all assignments are traced, with changed trace tags to indicate the type of variable being set.
- New methods in the Rexx class: d2c() and c2d.


# NetRexx 0.76 [13 Aug 1996]

Changes:

- Three-pass processing allows forward references from properties and methods to later classes in the same program.
- New 'Methods for Rexx strings' section in the Language Specification
- Minor improvements to and tuning of Trace.

# NetRexx 0.75 [7 Aug 1996]

- The first draft of the NetRexx Language Specification is now available in online (HTML) format and in printable (PDF) format.
- This specification will be published by Prentice-Hall as part of a new book, "The NetRexx Language", later this year.
- NetRexx now has a new trace instruction for tracing method arguments, clauses, and results (including arguments passed to methods) during execution. Associated with this are options notrace and the trace special word.
- numeric form engineeering is now implemented.
- The static option has been added to the properties instruction, to allow for static properties that are not constant.
- The unpopular constant keyword on the method instruction has been renamed static, to align with the properties instruction. constant will continue to be accepted (with a warning) for a few months.


# NetRexx 0.67 [27 Jun 1996]

- The compiler is now entirely written in NetRexx, and so should run on all platforms that support the Java toolkit. It calls the javac compiler by direct method invocation, and typically runs about ten times faster than before (the current slow spots are (a) reading large classes.zip files and (b) loading classes).
- This level of NetRexx (0.67) should be a functional superset of the final 0.62 OS/2 translator, though there is one small language change.

- There is one language change: items of type 'char' are now treated as a single-character string, rather than as a number representing the encoding of the character. This avoids anomalies between binary and non-binary classes.
- For those programs that do need to get at the bit representation of numbers, primitive constructors are introduced, for example:
```
c=char 'M'    -- c is the character 'M'
i=int(c)      -- i has the value 77
d=char(i)     -- d is now 'M'
```
the for keywords on loop are now implemented, as in
```
loop for 3000; say 'hi'; end
loop i=n by for 10 while j<10; j=n+i; end
```
the over keyword on loop is now implemented; this allows easy
traversal through the contents of NetRexx indexed variables. For
example:
```
/* display the contents of myvar indexed Rexx variable */
do tail over myvar
  say myvar[tail]
end
```
loop over can be used with any subclass of Rexx or of Dictionary (for example, Hashtable).
Invalid do syntax is re-tried as a loop instruction, after a warning.
A list highlighting variables that are referenced only once is included in the variables cross-reference.

# NetRexx 0.54 [30 Apr 1996]

This release is a minor update to the translator, except for the addition of a fairly substantial example of a Java application written in NetRexx. See:

http://www2.hursley.ibm.com/netrexx/pinger.html
for a picture of how it looks. You can use this application without downloading the whole NetRexx package.


# NetRexx 0.50 [22 Apr 1996]

The first non-'IBM internal' release of NetRexx - the OS/2 Rexx prototype.
