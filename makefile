# This is the overall makefile for NetRexx on Unixlike systems
# for documentation purposees and people with habits that are hard to shake

PACKAGE_NAME:=NetRexx-3.01RC1.zip

all:
	java -jar ant/ant-launcher.jar compile
	java -jar ant/ant-launcher.jar jars

.PHONY: jars
jars:
	java -jar ant/ant-launcher.jar jars


.PHONY: tests
tests:
	java -jar ant/ant-launcher.jar tests

.PHONY: clean
clean: 
	java -jar ant/ant-launcher.jar clean

.PHONY: doc
doc:
	java -jar ant/ant-launcher.jar apidocs

.PHONY: package
package:
	java -jar ant/ant-launcher.jar clean
	java -jar ant/ant-launcher.jar compile
	java -jar ant/ant-launcher.jar jars
	rm -rf package
	mkdir -p package
	mkdir -p package/bin
	cp bin/* package/bin
	mkdir -p package/lib
	mkdir -p package/runlib
	mkdir -p package/examples
	mkdir -p package/documents
	mkdir -p package/tools
	cp build/lib/NetRexxC.jar package/lib
	cp build/lib/NetRexxR.jar package/runlib
	java -jar ant/ant-launcher.jar clean
	java -jar ant/ant-launcher.jar clean
	cp documentation/ug/*.pdf package/documents
	cp documentation/ug/releasenotes.txt package/
	cp documentation/ug/readme.txt package/
	cp documentation/nrl/*.pdf package/documents
	cp tools/ant-task/* package/tools
	cp tools/emacs-mode/netrexx-mode.el package/tools
	svn export --force https://svn.kenai.com/svn/netrexx~netrexxc-repo/netrexxc/examples package/examples
	jar cvf $(PACKAGE_NAME) -C package/ .



