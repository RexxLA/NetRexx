
CLASSPATH=.:../../build/lib/NetRexxF.jar
NRX_COMPILE_COMMAND = java -cp $(CLASSPATH) org.netrexx.process.NetRexxC
NRX_COMPILE_FLAGS = -comments -sourcedir -time -keepasjava -replace -warnexit0 -compact -nocrossref -utf8

TARGET		:= njpipes
PDFTARGET	:= $(TARGET).pdf
TEX_SRC		:= $(wildcard *.tex)
NRX_SRC		:= $(wildcard *.nrx)
NRX_OBJS	:= $(NRX_SRC:.nrx=.class)
LATEX_EXE	:= xelatex -output-driver="xdvipdfmx -i dvipdfmx-unsafe.cfg -q -E" -shell-esc
RAIL_EXE	:= rail
RELEASE		:= `java org.netrexx.process.NrVersion`
DOCNAME         := "NetRexx $(RELEASE) Pipelines Guide and Reference.pdf"

.SUFFIXES: .nrx .nry .njp .class .skel .xsl .java .pdf

.nrx.class:
	$(NRX_COMPILE_COMMAND) $< $(NRX_COMPILE_FLAGS)


all:	$(NRX_OBJS) $(PDFTARGET)

$(TARGET).pdf: $(TARGET).tex stages.pdf
#	$(MAKE) -k $(TARGET).bbl
	$(LATEX_EXE) $(TARGET).tex
	# bibtex $(TARGET)
	makeindex $(TARGET)
	$(RAIL_EXE) $(TARGET)
	@while ( grep "There were undefined references." $(TARGET).log >/dev/null ); do \
		echo '** Re-running pdfLaTeX **'; \
		$(LATEX_EXE) $(TARGET).tex; \
		makeindex $(TARGET); \
	done
	mv njpipes.pdf $(DOCNAME)

$(TARGET).bbl: $(TARGET).tex
	$(LATEX_EXE) $(TARGET).tex
#	bibtex $(TARGET)
	makeindex $(TARGET)

stages.pdf: stages.html
	wkhtmltopdf --minimum-font-size 25 --no-outline --page-width 10 -R 0 -L 3 stages.html stages-tofix.pdf
	# On fedora, the generated pdf must be 'fixed' by ghostscript
	gs -o stages.pdf -sDEVICE=pdfwrite stages-tofix.pdf

stages.html: Pipeline\ Stages.html
	cp 'Pipeline Stages.html' stages.html

#
# target clean removes compiled products
#
.PHONY: clean
clean: 
	rm -f stages.html
	rm -f *.class
	rm -f *.crossref
	rm -f *.aux
	rm -f *.out
	rm -f *.pdf

