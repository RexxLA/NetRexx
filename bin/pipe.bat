@if exist .\pipes.cnf goto ok
@echo Build this directory's default pipe.cnf
@echo import org.netrexx.njpipes.pipes.  >  pipes.cnf
@echo import org.netrexx.njpipes.stages. >> pipes.cnf
@echo import org.netrexx.njpipes.tests.  >> pipes.cnf
:ok
set NJPIPES_HOME="%~dp0.."
@java -cp "%NJPIPES_HOME%/njpipesC.jar;%NETREXX_HOME%/NetRexxC.jar;%CLASSPATH%" org.netrexx.njpipes.pipes.compiler %*
