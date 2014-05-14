# This is the overall makefile for NetRexx on Unixlike systems
# for documentation purposes and people with habits that are hard to shake

all:
	java -jar ant/ant-launcher.jar compile
	# java -jar ant/ant-launcher.jar jars

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
	rm build/classes/COM/ibm/netrexx/process/*.class
	rm build/classes/org/netrexx/process/*.class
	rm build/classes/org/netrexx/jsr223/*.class
	rm build/classes/org/apache/tools/ant/taskdefs/optional/*.class
	rm build/classes/netrexx/lang/*.class
	ecj -warn:none -source 1.5 -target 1.5 build/classes/COM/ibm/netrexx/process/*.java
	ecj -warn:none -source 1.5 -target 1.5 build/classes/netrexx/lang/*.java
	ecj -warn:none -source 1.5 -target 1.5 -cp ant/ant.jar:ant/ant-launcher.jar:lib/NetRexxC.jar build/classes/org/netrexx/jsr223/*.java
	ecj -warn:none -source 1.5 -target 1.5 -cp ant/ant.jar:ant/ant-launcher.jar:lib/NetRexxC.jar build/classes/org/apache/tools/ant/taskdefs/optional/*.java
	ecj -warn:none -source 1.5 -target 1.5 -cp $(CLASSPATH):lib/NetRexxC.jar build/classes/org/netrexx/process/*.java
	java -jar ant/ant-launcher.jar jars
	java -jar ant/ant-launcher.jar package
	mkdir -p scratch/META-INF
	cd scratch;unzip -o ../lib/ecj-I20140402-0100.jar
	cd scratch;unzip -o ../build/lib/NetRexxC.jar
	cp minimalmanifest scratch/
	cd scratch;jar cmf minimalmanifest NetRexxF.jar *
	mv scratch/NetRexxF.jar lib
	zip NetRexx-3.03RC1.zip lib/NetRexxF.jar
	mv lib/NetRexxF.jar build/lib
	rm -rf scratch




