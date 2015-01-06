" Vim compiler file
" Compiler:     NetRexx
" Maintainer:   Jason Martin <agrellum@gmail.com>
" Last Change:  2014 Jan 02

if exists("current_compiler")
  finish
endif
let current_compiler = "nrc"

if exists(":CompilerSet") != 2		" older Vim always used :setlocal
  command -nargs=* CompilerSet setlocal <args>
endif

CompilerSet makeprg=nrc\ -exec\ %

CompilerSet errorformat=%E%f:%l:\ %m,%-Z%p^,%-C%.%#,%-G%.%#
