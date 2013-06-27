NRX_COMPILE_COMMAND = java org.netrexx.process.NetRexxC
NRX_COMPILE_FLAGS = -comments -sourcedir -time -keepasjava -replace -warnexit0 -compact -nocrossref -nocompile -utf8

JAVA_COMPILE_COMMAND = java org.eclipse.jdt.internal.compiler.batch.Main 
JAVA_COMPILE_FLAGS = -warn:-unusedPrivate -warn:-unusedLocal -Xemacs

.nrx.class:
	$(NRX_COMPILE_COMMAND) $< $(NRX_COMPILE_FLAGS)
	$(JAVA_COMPILE_COMMAND) $(JAVA_COMPILE_FLAGS) $*.java

NRX_SRC		:= $(wildcard *.nrx)
NRX_OBJS	:= $(NRX_SRC:.nrx=.class)
JAVA_SRC	:= $(wildcard *.java)
JAVA_OBJS	:= $(JAVA_SRC:.java=.class)


.SUFFIXES: .nrx .nry .njp .class .skel .xsl .java .pdf

#
# target all compiles the netrexx and java code
#
all::	DiagInterface.class $(NRX_OBJS) $(JAVA_OBJS)

fromscratch::
	$(NRX_COMPILE_COMMAND) *.nrx $(NRX_COMPILE_FLAGS)
	$(JAVA_COMPILE_COMMAND) $(JAVA_COMPILE_FLAGS) *.java

test::
	java zAllTests
#
# target clean removes compiled products
#
.PHONY: clean
clean: 
	rm -f *.class
	rm -f *.crossref
	rm -f *.bak
	rm -f *~
	find . -name "*.nrx" | awk '{$$2 = $$1 ; sub ( /\.nrx/,".java",$$1 ) ; print $$1 }' | xargs rm -f

