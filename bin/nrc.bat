@rem  Alias for NetRexxC.bat
@echo off
set nrcopts=
:argactionstart
if -%1-==-- goto argactionend
set nrcopts=%nrcopts% %1
shift
goto argactionstart
:argactionend
call netrexxc.bat %nrcopts%
rem call netrexxc.bat %1 %2 %3 %4 %5 %6 %7 %8 %9
