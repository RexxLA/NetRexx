COMPILE_COMMAND = nrc
COMPILE_OPTIONS =  -sourcedir -time -comments -keep -replace -binary -format -warnexit0 -compact

NRX_SRC		:= $(wildcard *.nrx)
NRX_OBJS	:= $(NRX_SRC:.nrx=.class)

# build rules
.nrx.class:
	$(COMPILE_COMMAND) $< $(COMPILE_OPTIONS)
	mv $*.java.keep $*.java

.SUFFIXES: .nrx .nry .njp .class .skel .xsl .java .u

all::	$(NRX_OBJS)
.PHONY: clean
clean: 
	rm -f *.class
	rm -f *.crossref



