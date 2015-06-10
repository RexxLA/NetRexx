CP="-cp ../../trunk/build/lib/NetRexxF.jar"
NRX_COMPILE_COMMAND = java -Dnrx.compiler=ecj $(CP) org.netrexx.process.NetRexxC
NRX_COMPILE_FLAGS = -comments -sourcedir -time -keepasjava -replace -warnexit0 -compact -nocrossref -utf8

TARGET		:= pg
PDFTARGET	:= $(TARGET).pdf
TEX_SRC		:= $(wildcard *.tex)
NRX_SRC		:= $(wildcard *.nrx)
NRX_OBJS	:= $(NRX_SRC:.nrx=.class)
LATEX_EXE	:= xelatex -shell-esc
RAIL_EXE	:= rail
RELEASE		:= `java org.netrexx.process.NrVersion`
DOCNAME         := "NetRexx $(RELEASE) Programming Guide.pdf"

.SUFFIXES: .nrx .nry .njp .class .skel .xsl .java .pdf

.nrx.class:
	$(NRX_COMPILE_COMMAND) $< $(NRX_COMPILE_FLAGS)

all:	copy $(NRX_OBJS) $(PDFTARGET)

.PHONY copy:
	cp ../../../examples/jsr223/ListScriptingEngines.nrx .
	cp ../../../examples/jsr223/ScriptDemo2.nrx .
	cp ../../../examples/jsr223/ScriptDemo3.nrx .
	cp ../../../examples/jsr223/RunAppleScript.nrx .
	cp ../../../examples/jsr223/antscript.xml .
	cp ../../../examples/jsr223/JSONDemo.nrx .	


$(TARGET).pdf: $(TEX_SRC)
	$(MAKE) -k $(TARGET).bbl
	$(LATEX_EXE) $(TARGET).tex
	$(RAIL_EXE) $(TARGET)
	@while ( grep "There were undefined references." $(TARGET).log >/dev/null ); do \
		echo '** Re-running pdfLaTeX **'; \
		$(LATEX_EXE) $(TARGET).tex; \
		makeindex $(TARGET); \
	done
	mv pg.pdf $(DOCNAME)
$(TARGET).bbl: $(TEXSRC) $(BIBSRC)
	$(LATEX_EXE) $(TARGET).tex
#	bibtex $(TARGET)
	makeindex $(TARGET)

#
# target clean removes compiled products
#
.PHONY: clean
clean: 
	rm -f *.class
	rm -f *.crossref
	rm -f *.aux
	rm -f *~
