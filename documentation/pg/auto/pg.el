(TeX-add-style-hook "pg"
 (lambda ()
    (LaTeX-add-labels
     "interpreted"
     "classes"
     "editors")
    (TeX-run-style-hooks
     "../boilerplate/preamble"
     "../boilerplate/bookmeta"
     "../boilerplate/series"
     "../boilerplate/conventions"
     "nruappl"
     "nruapi")))

