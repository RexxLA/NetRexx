@REM :  Batch file to translate a Java source file to a NetRexx program,
@REM :  and optionally compile and run the resulting class file
@REM : -----------------------------------------------------------------
@REM :  use as:  java2nrx hello
@REM :
@REM :     which will use the java2nrx translator to translate the
@REM :     source file hello.java to hello.nrx
@REM :     then will use NetRexxC to compile hello.nrx
@REM :
@REM :  NetRexx OPTIONS keywords may be added (with a -) before or after
@REM :  the file specification, along with the extra flags known
@REM :  to NetRexxC (such as -keep).   For example:
@REM :
@REM :     java2nrx -keep -format -comments hello.java hello.nrx
@REM :
@REM :  Invoke with no parameters for a full list of flags.
@REM :
@REM :  To compile the nrx file after translation, specify -nrc as the
@REM :  first word of the command arguments
@REM :
@REM :     java2nrx -nrc hello
@REM :
@REM :  Note when using -nrc, the original java file is temporary
@REM :  renamed with extension java2nrx, during NetRexxC translation.
@REM :
@REM :  To run the class after compilation, specify -run as the
@REM :  second word of the command arguments and the name of the
@REM :  class as the third word.
@REM :
@REM :     java2nrx -nrc -run hello
@REM :
@REM : -----------------------------------------------------------------
@REM :  2011.09.20 -- initial version


@REM :-----------------------------------------------------------------
@REM : use as:  java2nrx hello
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
@echo off
set java2nrx.bat_run=no
set java2nrx.bat_nrc=no
set java2nrx.bat_stdout=no

set scriptdir=%~d0%~p0


if not '%1'=='-nrc' goto run 
  set java2nrx.bat_nrc=yes
  shift

:run
if not '%1'=='-run' goto stdout
  set java2nrx.bat_run=yes
  shift 

:stdout
if not '%1'=='-stdout' goto translate
  set java2nrx.bat_stdout=yes
  shift



:translate
rem set file=%~d1%~p1%~n1
set file=%~n1
if "%file%"=="" echo Usage: java2nrx [-nrc] [-stdout] [-run]  [other options] ^<filename^> && exit /B 2

set infile=%file%.java
set outfile=%file%.nrx

if %java2nrx.bat_stdout%==yes java -ms4M -jar %scriptdir%\java2nrx.jar %infile% 
if %java2nrx.bat_stdout%==no  java -ms4M -jar %scriptdir%\java2nrx.jar %infile% %outfile%   

if %java2nrx.bat_nrc%==no goto quit

:compile

move %infile% %file%.java2nrx 

java -ms4M %netrexx_java% COM.ibm.netrexx.process.NetRexxC %outfile% %2 %3 %4 %5 %6 %7 %8 %9
set rc=%errorlevel%

move %file%.java2nrx %infile%

if %rc%==2 goto quit

if %java2nrx.bat_run%==no goto quit
echo Running %1...
java %file%
:quit
