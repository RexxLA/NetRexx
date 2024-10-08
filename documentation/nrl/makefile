CLASSPATH=.:../../build/lib/NetRexxF.jar
NRX_COMPILE_COMMAND = java -cp $(CLASSPATH) org.netrexx.process.NetRexxC
NRX_COMPILE_FLAGS = -comments -sourcedir -time -keepasjava -replace -warnexit0 -compact -nocrossref -utf8

TARGET		:= nrl
PDFTARGET	:= $(TARGET).pdf
TEX_SRC		:= $(wildcard *.tex)
NRX_SRC		:= $(wildcard *.nrx)
NRX_OBJS	:= $(NRX_SRC:.nrx=.class)
LATEX_EXE	:= xelatex -output-driver="xdvipdfmx -i dvipdfmx-unsafe.cfg -q -E" -shell-esc
RAIL_EXE	:= rail
RELEASE		:= `java org.netrexx.process.NrVersion`
DOCNAME         := "NetRexx $(RELEASE) Language Reference.pdf"


.SUFFIXES: .nrx .nry .njp .class .skel .xsl .java .pdf

.nrx.class:
	$(NRX_COMPILE_COMMAND) $< $(NRX_COMPILE_FLAGS)


all:	$(NRX_OBJS) $(PDFTARGET)

$(TARGET).pdf: $(TEX_SRC)
	$(MAKE) -k $(TARGET).bbl
#	$(RAIL_EXE) $(TARGET)
	$(LATEX_EXE) $(TARGET).tex
	@while ( grep "There were undefined references." $(TARGET).log >/dev/null ); do \
		echo '** Re-running pdfLaTeX **'; \
		$(LATEX_EXE) $(TARGET).tex; \
		makeindex $(TARGET); \
	done
	mv $(TARGET).pdf $(DOCNAME)
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
	rm -f *.idx
	rm -f *.ilg
	rm -f *.ind
	rm -f *.lof
	rm -f *.log
	rm -f *.lot
	rm -f *.out
	rm -f *.rai
	rm -f *.rao
	rm -f *.toc
	rm -f *~
	rm -f *.pdf
