# This is the overall makefile for NetRexx on Unixlike systems
# for documentation purposes and people with habits that are hard to shake
# withjavac was: compile
all:
	java -jar ant/ant-launcher.jar withjavac
	java -jar ant/ant-launcher.jar jars

.PHONY: jars
jars:
	java -jar ant/ant-launcher.jar jars

.PHONY: tests
tests:
	java -jar ant/ant-launcher.jar tests
	java -jar ant/ant-launcher.jar run.tests

.PHONY: clean
clean: 
	java -jar ant/ant-launcher.jar clean

.PHONY: javadoc
javadoc:
	java -jar ant/ant-launcher.jar apidocs

.PHONY: documents
documents:
	cd documentation/ug;make -B
	cd documentation/pg;make -B	
	cd documentation/nrl;make -B	
	cd documentation/njpipes;make -B	

.PHONY: documents-clean
documents-clean:
	cd documentation/ug;make clean
	cd documentation/pg;make clean	
	cd documentation/nrl;make clean	
	cd documentation/njpipes;make clean	

.PHONY: natives
natives:
	native-image -cp $(CLASSPATH) org.vpad.extra.workpad.Workspace
	mv org.vpad.extra.workpad.workspace nrws
#	native-image -cp $(CLASSPATH) --report-unsupported-elements-at-runtime org.netrexx.process.NetRexxC
	native-image -cp $(CLASSPATH) org.netrexx.process.NetRexxC -H:+ReportExceptionStackTraces
	mv org.netrexx.process.netrexxc nrc
#	native-image -cp $(CLASSPATH) org.netrexx.njpipes.pipes.compiler
#	mv org.netrexx.njpipes.pipes.compiler pipc
	native-image -cp $(CLASSPATH) org.netrexx.njpipes.pipes.runner -H:+ReportExceptionStackTraces
	mv org.netrexx.njpipes.pipes.runner pipe

.PHONY: package
package:
	java -jar ant/ant-launcher.jar package

.PHONY: upload
upload:
	tar --exclude='build/classes' --exclude='.git' --exclude='documentation' --exclude='examples' -zcvf netrexx-4.05-beta.tar.gz .
	scp netrexx-4.05-beta.tar.gz netrexx@rexxla.org:website/packages
	shasum -a 256 netrexx-4.05-beta.tar.gz

