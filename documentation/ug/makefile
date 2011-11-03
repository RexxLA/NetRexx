TARGET		:= usersguide
PDFTARGET	:= $(TARGET).pdf
TEX_SRC		:= $(wildcard *.tex)
LATEX_EXE	:= xelatex

.SUFFIXES: .nrx .nry .njp .class .skel .xsl .java .pdf

all:	$(PDFTARGET)

$(TARGET).pdf: $(TEX_SRC)
	$(MAKE) -k $(TARGET).bbl
	$(LATEX_EXE) $(TARGET).tex
	@while ( grep "There were undefined references." $(TARGET).log >/dev/null ); do \
		echo '** Re-running pdfLaTeX **'; \
		$(LATEX_EXE) $(TARGET).tex; \
		makeindex $(TARGET); \
	done

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
