(TeX-add-style-hook "pg"
 (lambda ()
    (LaTeX-add-index-entries
     "compiling,from another program"
     "completion codes, from translator"
     "return codes, from translator"
     "PrintWriter stream for capturing translator output"
     "capturing translator output")
    (LaTeX-add-labels
     "interpreted"
     "classes"
     "events"
     "editors")
    (TeX-run-style-hooks
     "../boilerplate/preamble"
     "../boilerplate/bookmeta"
     "../boilerplate/series"
     "../boilerplate/conventions"
     "../railroad/loop"
     "../ug/options"
     "nruappl"
     "mqtt"
     "nruapi"
     "../railroad/java2nrx1"
     "../railroad/java2nrx2"
     "eclipseguide"
     "resolution")))

