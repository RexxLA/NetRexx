# Foreword - by Jeff Hennick
Often in programming projects, either in part or in the whole, we are faced with a collection of objects or text records where each is to be filtered and/or transformed in some way. Sometimes this is easy, many times there are special considerations to be handled.

Pipelines is specifically designed to do all the dirty work around this and by selected small already written and tested programs (called stages).  NetRexx Pipelines make this quite easy. And now Pipelines, with over 150 stages, are built into NetRexx. Custom stages are easily written in NetRexx.

The concept of pipes joining small text record processing programs had
its start in the early 1970s. In the 1980s, IBM greatly expanded the concept with stages that could have multiple input and output streams of records.  And in the 1990s, this concept was transferred to NetRexx. NetRexx Pipelines, while handling records nicely, also adds full Java objects.  NetRexx also adds some new Rexx and Java inspired stages.

\begin{shaded}
Note to users coming from IBM CMS Pipelines: While many stages and pipes are and work identically, there are some inherent differences due to the underlying operating environments. While some CMS stages are not in NetRexx (APL, CP, PUNCH, etc.), NetRexx has over 30 new stages -- many using concepts from NetRexx's two parent languages, Rexx and Java.
\end{shaded}

Pipelines can read and write NetRexx variables and files. Many stages have shorter abbreviated names also to ease command line typing.

Pipes can be written on-the-fly at the command line, or made more permanent in files. Like Rexx, these can be written on a single line or in easier to read multiple lines.

Full documentation, with all the included stages (and the CMS stages not included) is in the Pipelines Guide and Reference.

Examples:

This is a classic, as would appear in a file names count.njp,  It could be a single line, and run from the command line would need to be.  The "--" is a stage name (alias comment) so needs to be ended with a "|".  These too could be on their own lines.  The count stage has other options besides words.
\begin{lstlisting}
pipe (count)
  disk input.file | -- Read input file |
  count words | -- Count |
  console | -- Display result
\end{lstlisting}
Here is a multi-output stage example. "<" and ">" are aliases for disk read and write. "?" is the end of a pipe.  A word ending in ":" is a label.  The "/"s are used to delineate the data string.
\begin{lstlisting}
pipe (locrec)
  < input.file | Loc: locate /Sid/ | > selected.records ?
  Loc: | > discarded.records
\end{lstlisting}
In this one, I'll use the LITERAL and SPLIT stages to generate short contained input records to demonstrate it in action. Note: some systems will require this on a single line; some will require quote marks around everything but the pipe.
\begin{lstlisting}
pipe literal aa bb;bb cc;cc dd;dd ee;ee ff;gg hh | -- input data |
   split ; | -- break the single line into many |
   between /c/ /e/ | -- make the selection |
   cons | -- see the results
\end{lstlisting}
the output is
\begin{verbatim}
cc dd
dd ee
ee ff
\end{verbatim}



\emph{Jeff Hennick, Forth Worth, June 16th, 2023}
