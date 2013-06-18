# This is the overall makefile for NetRexx on Unixlike systems
# for documentation purposes and people with habits that are hard to shake

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

.PHONY: documents
documents:
	cd documentation/ug;make -B
	cd documentation/pg;make -B	

.PHONY: package
package:
	java -jar ant/ant-launcher.jar package
	mkdir -p scratch/META-INF
	cd scratch;unzip -o ../lib/ecj-4.2.jar
	cd scratch;unzip -o ../build/lib/NetRexxC.jar
	cp minimalmanifest scratch/
	cd scratch;jar cmf minimalmanifest NetRexxF.jar *
	mv scratch/NetRexxF.jar lib
	zip NetRexx-3.02RC2V2.zip lib/NetRexxF.jar
	mv lib/NetRexxF.jar build/lib
	rm -rf scratch




