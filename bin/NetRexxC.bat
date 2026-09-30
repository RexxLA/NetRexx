@REM : Batch file to translate and compile a NetRexx program, and
@REM : optionally run the resulting class file
@REM :-----------------------------------------------------------------
@REM : use as:  NetRexxC hello
@REM :
@REM :   which will use the NetRexx translator to translate the
@REM :     source file hello.nrx to hello.java
@REM :   then will use javac to compile hello.java
@REM :
@REM : OPTIONS keywords may be added (with a -) before or
@REM : after the file specification, along with the extra flags known
@REM : to NetRexxC (such as -keep).   For example:
@REM :
@REM :    NetRexxC -keep -format -comments hello
@REM :
@REM : Invoke with -help for a full list of flags.
@REM :
@REM : To run the class after compilation, specify -run as the
@REM : first word of the command arguments and the name of the
@REM : class as the second word.  Note that the case of the
@REM : letters must be exactly correct for this to work.  For example:
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
@REM : 2015.06.30 -- remove operand limit, allow quotes, spaces and parens in classpath, add NetRexxC.jar to classpath if found
@REM : 2015.08.17 -- document steps contained in this batch file (cannot use %* here due to "-run" parm shift)
@REM : 2026.09.30 -- setlocal; pass the arguments whole (%*; less the -run with -run), so =,; survive;
@REM :               no echo of the java command; exit with the java status;
@REM :               find the jars with quoted paths, so a folder may have blanks and brackets

@echo off
setlocal
set nrcrc=0
@rem copy CLASSPATH environment variable to nrcpath variable
set nrcpath=%CLASSPATH%;
@rem map chars batch cmds cannot handle:  " to null, blank to ?, ( to <, ) to >
set nrcpath=%nrcpath:"=%
set "nrcpath=%nrcpath: =?%"
set "nrcpath=%nrcpath:(=^<%"
set "nrcpath=%nrcpath:)=^>%"

@rem if NetRexxF.jar in classpath, jump to label setcomp
set "nrcpathf=%nrcpath:NetRexxF.jar=%"
if not "%nrcpathf%"=="%nrcpath%" goto setcomp

@rem set variable libpath to possible NetRexxF.jar location in sister directory "lib"
@rem (quoted, and no bracketed blocks: the folder may be named like "Program Files (x86)")
set "libpath=%~dp0..\lib\NetRexxF.jar"
@rem if NetRexxF.jar was found, add to temporary classpath variable nrcpath, otherwise go to label compset
if not exist "%libpath%" goto compset
set "nrcpath=%nrcpath%;%libpath%"

:setcomp
@rem if environment variable netrexx_java exists jump to label potluck, otherwise set it to specify ecj compiler
if defined netrexx_java goto potluck
  set netrexx_java=-Dnrx.compiler=ecj
  goto potluck
  
:compset
@rem if NetRexxC.jar is in classpath, jump to label potluck
set "nrcpathc=%nrcpath:NetRexxC.jar=%"
if not "%nrcpathc%"=="%nrcpath%" goto potluck

@rem set variable libpath to possible NetRexxC.jar location in sister directory "lib"
set "libpath=%~dp0..\lib\NetRexxC.jar"
@rem if NetRexxC.jar was found, add to temporary classpath variable nrcpath (because NetRexxF.jar is not available if running from repository checkout rather than a packaged distribution)
if exist "%libpath%" set "nrcpath=%nrcpath%;%libpath%"

:potluck
@rem restore chars batch cmds cannot handle:  ? to blank , < to (, > to ) in temp classpath variable nrcpath
set "nrcpath=%nrcpath:?= %"
set "nrcpath=%nrcpath:^<=(%"
set "nrcpath=%nrcpath:^>=)%"

@rem check first operand to see if it is "-run". If not, jump to label compile, otherwise set run flag, save name of class to run, remove any trailing ".nrx" from name, erase -run option
@rem if no options or files specified, tell user to add "-help" for options syntax list
if "%~1"=="" echo "nrc -help" lists options
if "%~1"=="" goto quit

@rem the arguments go to NetRexxC whole, as %*: %1, %2 ... split at = , ; as well as blanks
if /i "%~1"=="-run" goto setrun
@rem use java command to start NetRexxC compiler with adjusted classpath variable, netrexx_java environment variable containing java options, and the arguments
java -cp "%nrcpath%;." %netrexx_java% org.netrexx.process.NetRexxC %*
set nrcrc=%ERRORLEVEL%
goto quit

:setrun
@rem -run: save the name of the class to run, and pass the arguments without the -run
@rem (everything up to and including the first argument)
set netrexxc.runner=%~2
set netrexxc.runner=%netrexxc.runner:.nrx=%
set "nrcopts=%*"
call set "nrcopts=%%nrcopts:*%1=%%"
java -cp "%nrcpath%;." %netrexx_java% org.netrexx.process.NetRexxC %nrcopts%
set nrcrc=%ERRORLEVEL%
if %nrcrc% GEQ 2 goto quit

@rem if the requested class file exists, use java command to start the class with the adjusted classpath hopefully providing netrexx runtime classes
echo Running %netrexxc.runner% ...
if not exist "%netrexxc.runner%.class" goto noclass
java -cp "%nrcpath%;." %netrexxc.runner%
set nrcrc=%ERRORLEVEL%
goto quit
:noclass
echo -run error: class file not found
set nrcrc=1
:quit
exit /b %nrcrc%
