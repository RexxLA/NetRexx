COMPILE_COMMAND = nrc
COMPILE_OPTIONS =  -sourcedir -time -comments -keep -replace -binary -format -warnexit0 -utf8 -compact

NRX_SRC		:= $(wildcard *.nrx)
NRX_OBJS	:= $(NRX_SRC:.nrx=.class)

.nrx.class:
	$(COMPILE_COMMAND) $< $(COMPILE_OPTIONS)
	mv $*.java.keep $*.java


.SUFFIXES: .nrx .nry .njp .class .skel .xsl .java .u

all:: DiagX.class DiagSay.class DiagConcat.class DiagIf.class DiagStems.class DiagArrays.class DiagLoop.class DiagSelect.class DiagExpression.class DiagConstant.class DiagNop.class DiagParse.class DiagContinue.class DiagComment.class DiagInterface.class DiagForward.class DiagMutual.class DiagMutual2.class DiagMutual3.class DiagMutual4.class DiagAbstract.class DiagUTF8.class DiagIndir.class DiagMinor.class DiagRexx.class DiagAll.class
	find . -name "*.java.keep" | awk '{$$2 = " "$$1 ; sub ( /\.java.keep/,".java",$$1 ) ; print $$2 " " $$1 } ' | xargs  -n2 mv

MUTUALS := DiagMutual DiagMutual2 DiagMutual3 DiagMutual4

DiagMutual.class: DiagMutual.nrx
	$(COMPILE_COMMAND) $(COMPILE_OPTIONS) $(MUTUALS)

DiagMutual2.class: DiagMutual2.nrx
	$(COMPILE_COMMAND) $(COMPILE_OPTIONS) $(MUTUALS)

DiagMutual3.class: DiagMutual3.nrx
	$(COMPILE_COMMAND) $(COMPILE_OPTIONS) $(MUTUALS)

DiagMutual4.class: DiagMutual4.nrx
	$(COMPILE_COMMAND) $(COMPILE_OPTIONS) $(MUTUALS)

.PHONY: test
test:
	java COM.ibm.netrexx.diag.DiagAll


.PHONY: clean
clean: 
	rm -f *.class
	rm -f *.crossref
	find . -name "*.nrx" | awk '{$$2 = $$1 ; sub ( /\.nrx/,".java",$$1 ) ; print $$1 }' | xargs rm -f



