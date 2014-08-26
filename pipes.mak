.nrx.class:
	$(COMPILE_COMMAND) $< $(COMPILE_FLAGS)
	mv $*.java.keep $*.java

.java.class:
	javac $*.java

NRX_SRC		:= $(wildcard *.nrx)
NRX_OBJS	:= $(NRX_SRC:.nrx=.class)
JAVA_SRC	:= $(wildcard *.java)
JAVA_OBJS	:= $(JAVA_SRC:.java=.class)
PIPESRC         := src/org/netrexx/njpipes/pipes/
STAGESRC        := src/org/netrexx/njpipes/stages/
CPL		:= ./lib/NetRexxC.jar:$(CLASSPATH)

.SUFFIXES: .nrx .nry .njp .class .skel .xsl .java .pl


all::	pipes stages pphase2

pipes::
	java org.netrexx.process.NetRexxC -binary -warnexit0 -nocompile -replace -nocrossref -keepasjava $(PIPESRC)link $(PIPESRC)pnode $(PIPESRC)pipe $(PIPESRC)_stage $(PIPESRC)stage $(PIPESRC)StageError  $(PIPESRC)utils $(PIPESRC)DString $(PIPESRC)IRange $(PIPESRC)RingBuf $(PIPESRC)ThreadPool $(PIPESRC)ThreadQ $(PIPESRC)RunnablePool
	javac -cp $(CPL):. -d build/classes -deprecation $(PIPESRC)link.java $(PIPESRC)pipe.java $(PIPESRC)_stage.java $(PIPESRC)stage.java $(PIPESRC)StageError.java $(PIPESRC)pnode.java $(PIPESRC)utils.java $(PIPESRC)DString.java $(PIPESRC)IRange.java $(PIPESRC)RingBuf.java $(PIPESRC)ThreadPool.java $(PIPESRC)ThreadQ.java $(PIPESRC)RunnablePool.java

stages::
	java -cp $(CPL):./build/classes org.netrexx.process.NetRexxC -nocompile -replace -warnexit0 -nocrossref -keepasjava $(STAGESRC)*.nrx
	javac -cp $(CPL):./build/classes -d ./build/classes $(STAGESRC)*.java

pphase2::
	java -cp $(CPL):./build/classes org.netrexx.process.NetRexxC -nocompile -replace -warnexit0 -keepasjava $(PIPESRC)compiler $(PIPESRC)filterNjp $(PIPESRC)pipe2nrx $(PIPESRC)processNjp $(PIPESRC)readconfig $(PIPESRC)readgroup $(PIPESRC)writepipe
	javac -cp $(CPL):./build/classes -d ./build/classes $(PIPESRC)compiler.java $(PIPESRC)filterNjp.java $(PIPESRC)pipe2nrx.java $(PIPESRC)processNjp.java $(PIPESRC)readconfig.java $(PIPESRC)readgroup.java $(PIPESRC)writepipe.java




