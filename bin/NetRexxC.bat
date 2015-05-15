@REM : Batch file to translate and compile a NetRexx program, and
@REM : optionally run the resulting class file
@REM :-----------------------------------------------------------------
@REM : use as:  NetRexxC hello
@REM :
@REM :   which will use the NetRexx translator to translate the
@REM :     source file hello.nrx to hello.java
@REM :   then will use javac to compile hello.java
@REM :
@REM : Up to eight OPTIONS keywords may be added (with a -) before or
@REM : after the file specification, along with the extra flags known
@REM : to NetRexxC (such as -keep).   For example:
@REM :
@REM :    NetRexxC -keep -format -comments hello
@REM :
@REM : Invoke with no parameters for a full list of flags.
@REM :
@REM : To run the class after compilation, specify -run as the
@REM : first word of the command arguments and the name of the
@REM : class as the second word.  Note that the case of the
@REM : letters must be exactly correct for this to work, and do not
@REM : specify the .nrx extension.  For example:
@REM :
@REM :    NetRexxC -run hello
@REM :
@REM : For a more flexible script for this, see NetRexxC.cmd
@REM :-----------------------------------------------------------------
@REM : 1996.12.28 -- initial version derived from NetRexxC.cmd
@REM : 1998.05.25 -- use NETREXX_JAVA as options to java.exe
@REM : 2011.09.01 -- use org.netrexx.process               
@REM : 2011.09.01 -- remove -xms4M
@REM : 2011.09.29 -- add error msg for -run with x.nrx name format 
@REM : 2014.05.30 -- default to ecj compiler and add NetRexxF.jar to classpath 

@echo off
set nrcpath=%CLASSPATH%;.
if not "%nrcpath:NetRexxF.jar=%"=="%nrcpath%" goto setcomp
set binpath=%~dp0
set libpath=%binpath:\bin=\lib%NetRexxF.jar
if exist %libpath% (set nrcpath="%CLASSPATH%;.;%libpath%") else (goto compset)
:setcomp
if defined netrexx_java goto potluck
  set netrexx_java=-Dnrx.compiler=ecj
  goto potluck
:compset
set nrcpath=%CLASSPATH%;.
if not "%nrcpath:NetRexxC.jar=%"=="%nrcpath%" goto potluck
set binpath=%~dp0
set libpath=%binpath:\bin=\lib%NetRexxC.jar
if exist %libpath% (set nrcpath="%CLASSPATH%;.;%libpath%")
:potluck
set netrexxc.bat_run=no
if not '%1'=='-run' goto compile
  set netrexxc.bat_run=yes
  shift
:compile
if not -%1-==-- goto maywanthelp
echo "nrc --help" lists options
goto quit
:maywanthelp
if not "%1"=="--help" goto docompile
shift
:docompile
rem echo java -cp %nrcpath% %netrexx_java% org.netrexx.process.NetRexxC %1 %2 %3 %4 %5 %6 %7 %8 %9
java -cp %nrcpath% %netrexx_java% org.netrexx.process.NetRexxC %1 %2 %3 %4 %5 %6 %7 %8 %9
if errorlevel 2 goto quit
if %netrexxc.bat_run%==no goto quit
echo Running %1...
IF EXIST %1.class (java -cp %nrcpath% %1) ELSE echo -run error: class file not found - do not add .nrx to name
:quit
