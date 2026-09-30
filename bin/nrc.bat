@rem  Alias for NetRexxC.bat
@echo off
@REM : 2015.08.17 -- switch to %* for unlimited parm pass now handled by NetRexxC.bat 
@REM : 2026.09.30 -- find NetRexxC.bat beside this file, not on PATH; keep its exit status
call "%~dp0NetRexxC.bat" %*
exit /b %ERRORLEVEL%
