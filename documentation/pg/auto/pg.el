(TeX-add-style-hook "pg"
 (lambda ()
    (LaTeX-add-labels
     "editors")
    (TeX-run-style-hooks
     "pstricks-add"
     "pst-barcode"
     "caption"
     "listings"
     "xcolor"
     "color"
     "lingmacros"
     "xy"
     "all"
     "makeidx"
     "booktabs"
     "tabularx"
     "fontspec"
     "graphics"
     "hyperref"
     "latex2e"
     "bk10"
     "book"
     "10pt"
     "../boilerplate/netrexxformat"
     "../boilerplate/series"
     "../boilerplate/conventions")))

