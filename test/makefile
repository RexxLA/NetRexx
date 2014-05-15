CLASSPATH = -cp .:../build/lib/NetRexxF.jar:../lib/junit-4.8.2.jar
NRX_COMPILE_COMMAND = java $(CLASSPATH) -Dnrx.compiler=ecj org.netrexx.process.NetRexxC 
NRX_COMPILE_FLAGS = -comments -sourcedir -time -keepasjava -replace -warnexit0 -compact -nocrossref -utf8 -format

.nrx.class:
	$(NRX_COMPILE_COMMAND) $(NRX_COMPILE_FLAGS) $< 

NRX_SRC		:= $(wildcard *.nrx)
NRX_OBJS	:= $(NRX_SRC:.nrx=.class)
JAVA_SRC	:= $(wildcard *.java)
JAVA_OBJS	:= $(JAVA_SRC:.java=.class)


.SUFFIXES: .nrx .nry .njp .class .skel .xsl .java .pdf

#
# target all compiles the netrexx and java code
#
all::	DiagInterface.class $(NRX_OBJS) $(JAVA_OBJS)
	java -Dnrx.compiler=ecj $(CLASSPATH) zAllTests

test::
	java $(CLASSPATH) zAllTests
#
# target clean removes compiled products
#
.PHONY: clean
clean: 
	rm -f *.class
	rm -f *.crossref
	rm -f *.bak
	rm -f *~
	rm *.java
	find . -name "*.nrx" | awk '{$$2 = $$1 ; sub ( /\.nrx/,".java",$$1 ) ; print $$1 }' | xargs rm -f

