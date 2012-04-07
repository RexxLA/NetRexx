(TeX-add-style-hook "pg"
 (lambda ()
    (TeX-run-style-hooks
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

