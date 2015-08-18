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

@echo off
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

@rem set variable binpath to directory of current script (NetRexxC.bat)
set binpath=%~dp0
@rem set variable libpath to possible NetRexxF.jar location in sister directory "lib"
set libpath=%binpath:\bin=\lib%NetRexxF.jar
@rem if NetRexxF.jar was found, add to temporary classpath variable nrcpath, otherwise go to label compset
if exist %libpath% (set "nrcpath=%nrcpath%;%libpath%") else (goto compset)

:setcomp
@rem if environment variable netrexx_java exists jump to label potluck, otherwise set it to specify ecj compiler
if defined netrexx_java goto potluck
  set netrexx_java=-Dnrx.compiler=ecj
  goto potluck
  
:compset
@rem if NetRexxC.jar is in classpath, jump to label potluck
set "nrcpathc=%nrcpath:NetRexxC.jar=%"
if not "%nrcpathc%"=="%nrcpath%" goto potluck

@rem set variable binpath to directory of current script (NetRexxC.bat)
set binpath=%~dp0
@rem set variable libpath to possible NetRexxC.jar location in sister directory "lib"
set libpath=%binpath:\bin=\lib%NetRexxC.jar
@rem if NetRexxC.jar was found, add to temporary classpath variable nrcpath (because NetRexxF.jar is not available if running from repository checkout rather than a packaged distribution)
if exist %libpath% (set "nrcpath=%nrcpath%;%libpath%")

:potluck
@rem restore chars batch cmds cannot handle:  ? to blank , < to (, > to ) in temp classpath variable nrcpath
set "nrcpath=%nrcpath:?= %"
set "nrcpath=%nrcpath:^<=(%"
set "nrcpath=%nrcpath:^>=)%"

@rem check first operand to see if it is "-run". If not, jump to label compile, otherwise set run flag, save name of class to run, remove any trailing ".nrx" from name, erase -run option
set netrexxc.bat_run=no
if not '%1'=='-run' goto compile
  set netrexxc.bat_run=yes
  set netrexxc.runner=%2
  set netrexxc.runner=%netrexxc.runner:.nrx=%
  shift
  
:compile
@rem if no options or files specified, tell user to add "-help" for options syntax list, otherwise go to old label maywanthelp
if not -%1-==-- goto maywanthelp
echo "nrc -help" lists options
goto quit

:maywanthelp
@rem Since Windows batch files can only access 9 operands at a time, insert a loop that adds the first operand to variable nrcopts and shifts the operands left until no operands are left to add.
@rem This allows all operands to be passed in variable nrcopts to NetRexxC which does the actual compile.
set nrcopts=
:argactionstart
if -%1-==-- goto argactionend
set nrcopts=%nrcopts% %1
shift
goto argactionstart
:argactionend

:docompile
echo java -cp "%nrcpath%;." %netrexx_java% org.netrexx.process.NetRexxC %nrcopts%
@rem use java command to start NetRexxC compiler with adjusted classpath variable, netrexx_java environment variable containing java options and nrcopts variable containing netrexx files and options
java -cp "%nrcpath%;." %netrexx_java% org.netrexx.process.NetRexxC %nrcopts%
if errorlevel 2 goto quit

@rem if option "-run" was passed and requested class file exists, use java command to start the class with the adjusted classpath hopefully providing netrexx runtime classes 
if %netrexxc.bat_run%==no goto quit
echo Running %netrexxc.runner% ...
IF EXIST %netrexxc.runner%.class (java -cp "%nrcpath%;." %netrexxc.runner%) ELSE echo -run error: class file not found
:quit
