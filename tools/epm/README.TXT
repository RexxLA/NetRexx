This package contains a file, epmkwds.nrx, with syntax
colour-highlighting information for the NetRexx language in the
EPM editor.  To use this file, just copy it to the directory
where EPM is installed.  If you already have a version of EPM
which supports syntax highlighting, there will be other epmkwds
files such as epmkwds.c present there.  The file will be used
automatically when you activate syntax highlighting for a file
with a .nrx extension.

The file highlights all Netrexx keywords, various special
characters, comments, etc., although as with C, /*...*/ comments
are not handled correctly unless they are only a single line.  It
also highlights the Rexx class name, and all of the methods for
the Rexx class, and all of the classes in the 1.01 Java SDK
distribution.  The Java classes will be highlighted in both their
fully qualified and partially qualified forms.  Thus, both Frame
and java.awt.Frame will be highlighted.  The class names are
highlighted in a different colour from keywords.  This part of
the file could easily be extracted and used for syntax
highlighting the classnames when writing native Java code as
well.

-- Rob McDermid
(mcdermid@hcl.com)
