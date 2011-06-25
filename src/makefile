# This is the overall makefile for NetRexx on Unixlike systems


.SUFFIXES: .nrx .nry .njp .class .skel .xsl .java

RUNTIME	     = netrexx/lang/
RUNTIMENAME  = netrexx.lang
COMPILER     = COM/ibm/netrexx/process/
COMPILERNAME = COM.ibm.netrexx.process
DIAG         = COM/ibm/netrexx/diag/
DIAGNAME     = COM.ibm.netrexx.diag

DOCPATH	     = docs

all: 
	make --directory $(RUNTIME)
	make --directory $(COMPILER)
	make --directory $(DIAG)

.PHONY: jar
jar:
	echo Main-Class: COM.ibm.netrexx.process.NetRexxC > manifest
	jar cvfm NetRexxC.jar manifest
	find COM/ibm/netrexx/process -name '*.class' | xargs jar uvf NetRexxC.jar
	find COM/ibm/netrexx/process -name '*.properties' | xargs jar uvf NetRexxC.jar
	find netrexx -name '*.class' | xargs jar uvf NetRexxC.jar
	find netrexx -name '*.txt' | xargs jar uvf NetRexxC.jar

.PHONY: runtimejar
runtimejar:
	echo Main-Class: COM.ibm.netrexx.process.NetRexxC > manifest
	jar cvfm NetRexxR.jar manifest
#	find COM -name '*.properties' | xargs jar uvf NetRexxR.jar
	find netrexx -name '*.class' | xargs jar uvf NetRexxR.jar
	find netrexx -name '*.txt' | xargs jar uvf NetRexxR.jar

.PHONY: test
test:
	make --directory $(DIAG) test

.PHONY: clean
clean: 
	make --directory $(RUNTIME) clean
	make --directory $(COMPILER) clean
	make --directory $(DIAG) clean


.PHONY: cleanjars
cleanjars: 
	rm -f *.jar

.PHONY: fallback
fallback: 
	mv ~/lib/NetRexxC.jar.old ~/lib/NetRexxC.jar 

.PHONY: fallforward
fallforward: 
	mv ~/lib/NetRexxC.jar ~/lib/NetRexxC.jar.old 

.PHONY: doc
doc:
	mkdir -p $(DOCPATH)
	javadoc -source 1.4 -J-Xmx128M -classpath "$(DOCCLASSPATH)" -private -author -version -breakiterator -use -d $(DOCPATH) -bottom $(BOTTOM) -header $(HEADER) -windowtitle $(WINDOWTITLE) -doctitle $(DOCTITLE) $(RUNTIMENAME) $(COMPILERNAME)

