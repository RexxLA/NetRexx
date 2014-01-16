#!/bin/bash
#  Test script for nre
#  Download latest nre and Make sure it is set up
#  Make some junk dir
#  cd to it
#  Save a copy of this script inside there
#  Run it

mkdir nre_test

cd nre_test

echo "------------------------------------------------------------------------"
echo "Creating eight test files using each possible extension"
echo " to test regular expression matching"
echo "------------------------------------------------------------------------"

for a in "n" "N"; do
    for b in "r" "R"; do
        for c in "x" "X"; do
            echo "say \"$a$b$c\"" > "test."$a$b$c
        done
    done
done

echo "Done"

echo "------------------------------------------------------------------------"
echo "Test Number 1"
echo "Examine processing of created test files: nre -e test.*"
echo "------------------------------------------------------------------------"
nre -e test.*
read -s -n1 -p "Press a key to continue or CRTL-C to quit"
echo ""

echo "------------------------------------------------------------------------"
echo "Test Number 2"
echo "Execute all of them: nre test.*"
echo "------------------------------------------------------------------------"
nre test.*
read -s -n1 -p "Press a key to continue or CRTL-C to quit"
echo ""

echo "------------------------------------------------------------------------"
echo "Test Number 3"
echo "Chmod and Insert shebang on all of them: nre --chmod -a test.*"
echo "------------------------------------------------------------------------"
nre --chmod -a test.*

echo "Done"

echo "------------------------------------------------------------------------"
echo "Show results: cat test.*"
echo "------------------------------------------------------------------------"
cat test.*
read -s -n1 -p "Press a key to continue or CRTL-C to quit"
echo ""

echo "------------------------------------------------------------------------"
echo "Test Number 4"
echo "Execute all: ./test.*"
echo "------------------------------------------------------------------------"
./test.*
read -s -n1 -p "Press a key to continue or CRTL-C to quit"
echo ""

echo "------------------------------------------------------------------------"
echo "Test Number 5"
echo "Chmod and Delete shebang on all of them: nre --chmod -d test.*"
echo "------------------------------------------------------------------------"
nre --chmod -d test.*

echo "Done"

echo "------------------------------------------------------------------------"
echo "Show results: cat test.*"
echo "------------------------------------------------------------------------"
cat test.*
read -s -n1 -p "Press a key to continue or CRTL-C to quit"
echo ""

echo "------------------------------------------------------------------------"
echo "Make a dir with a space in name for testing: mkdir \"dir withspace\""
mkdir "dir withspace"
echo "And copy a test file to it: cp test.nrx \"dir withspace/\""
cp test.nrx "dir withspace/"
echo "------------------------------------------------------------------------"

echo "Done"

echo "------------------------------------------------------------------------"
echo "Test Number 6"
echo "Execute file in dir with space in name: nre dir\ withspace/test.nrx"
echo "------------------------------------------------------------------------"
nre dir\ withspace/test.nrx
read -s -n1 -p "Press a key to continue or CRTL-C to quit"
echo ""

echo "------------------------------------------------------------------------"
echo "Make directory with a .nrx in name for testing: mkdir \"dirwith.nrx\""
mkdir "dirwith.nrx"
echo "And copy a test file to it: cp test.nrx \"dirwith.nrx/\""
cp test.nrx "dirwith.nrx/"
echo "------------------------------------------------------------------------"

echo "Done"

echo "------------------------------------------------------------------------"
echo "Test Number 7"
echo "Execute file in dir with .nrx in name: nre dirwith.nrx/test.nrx"
echo "------------------------------------------------------------------------"
nre dirwith.nrx/test.nrx
read -s -n1 -p "Press a key to continue or CRTL-C to quit"
echo ""

echo "------------------------------------------------------------------------"
echo "Test Number 8"
echo "Try and execute the dir with .nrx in name: nre dirwith.nrx"
echo "It looks like a valid command"
echo "------------------------------------------------------------------------"
nre dirwith.nrx
echo "An error message should be shown above"
read -s -n1 -p "Press a key to continue or CRTL-C to quit"
echo ""

echo "------------------------------------------------------------------------"
echo "Make two fake depends file for testing. We will not test NetRexx."
echo "We will just show processing is correct"
echo "say \"depends one\"" > "dependone.nrx"
echo "say \"depends two\"" > "dependtwo.nrx"
echo "Show them: ls depend*"
echo "------------------------------------------------------------------------"
ls depend*

echo "Done"

echo "------------------------------------------------------------------------"
echo "Test Number 9"
echo "Examine one .nrx file with one depend: nre -e test.nrx dependone"
echo "------------------------------------------------------------------------"
nre -e test.nrx dependone
read -s -n1 -p "Press a key to continue or CRTL-C to quit"
echo ""

echo "------------------------------------------------------------------------"
echo "Test Number 10"
echo "Examine one .nrx files with depends: nre -e test.nrx dependone dependtwo"
echo "------------------------------------------------------------------------"
nre -e test.nrx dependone dependtwo
read -s -n1 -p "Press a key to continue or CRTL-C to quit"
echo ""

echo "------------------------------------------------------------------------"
echo "Test Number 11"
echo "Examine two groups together with other files and options: nre -e"
echo " test.nrx dependone dependtwo test.nrX dependone test.Nrx -keep test.NRX"
echo "------------------------------------------------------------------------"
nre -e test.nrx dependone dependtwo test.nrX dependone test.Nrx -keep test.NRX
read -s -n1 -p "Press a key to continue or CRTL-C to quit"
echo ""

echo "------------------------------------------------------------------------"
echo "Test Number 12"
echo "Multiple files with simple NetRexxC options: nre test.nrx -diag -exec"
echo " test.NRX -savelog -keep -diag -exec"
echo "------------------------------------------------------------------------"
nre test.nrx -diag -exec test.NRX -savelog -keep -diag -exec
read -s -n1 -p "Press a key to continue or CRTL-C to quit"
echo ""

echo "------------------------------------------------------------------------"
echo "Show log: cat *.log"
echo "------------------------------------------------------------------------"
cat *.log

echo "Done"

echo "------------------------------------------------------------------------"
echo "Test Number 13"
echo "Produce error if options/data appears before first file: nre -X test.nrx"
echo "------------------------------------------------------------------------"
nre -X test.nrx
read -s -n1 -p "Press a key to continue or CRTL-C to quit"
echo ""

echo "------------------------------------------------------------------------"
echo "Test Number 14"
echo "Test: nre"
echo "Will show usage because no command given"
echo "------------------------------------------------------------------------"
nre
read -s -n1 -p "Press a key to continue or CRTL-C to quit"
echo ""

echo "------------------------------------------------------------------------"
echo "Test Number 15"
echo "Test: nre --chmod"
echo "Will show usage because it is not a compelete command"
echo "------------------------------------------------------------------------"
nre --chmod
read -s -n1 -p "Press a key to continue or CRTL-C to quit"
echo ""

echo "------------------------------------------------------------------------"
echo "Test Number 16"
echo "Test: nre --chmod -a"
echo "Will show usage because it is not a compelete command"
echo "------------------------------------------------------------------------"
nre --chmod -a
read -s -n1 -p "Press a key to continue or CRTL-C to quit"
echo ""

echo "------------------------------------------------------------------------"
echo "Test Number 17"
echo "Test: nre --chmod -d"
echo "Will show usage because it is not a compelete command"
echo "------------------------------------------------------------------------"
nre --chmod -d
read -s -n1 -p "Press a key to continue or CRTL-C to quit"
echo ""

echo "------------------------------------------------------------------------"
echo "Test Number 18"
echo "Test: nre --chmod -a junk"
echo "Will produce error"
echo "------------------------------------------------------------------------"
nre --chmod -a junk
read -s -n1 -p "Press a key to continue or CRTL-C to quit"
echo ""

echo "------------------------------------------------------------------------"
echo "Test Number 19"
echo "Test: nre --chmod -d junk"
echo "Will produce error"
echo "------------------------------------------------------------------------"
nre --chmod -d junk
read -s -n1 -p "Press a key to continue or CRTL-C to quit"
echo ""

echo "------------------------------------------------------------------------"
echo "Test Number 20"
echo "Test: nre --chmod -a junk test.nrx"
echo "Will produce error"
echo "------------------------------------------------------------------------"
nre --chmod -a junk test.nrx
read -s -n1 -p "Press a key to continue or CRTL-C to quit"
echo ""

echo "------------------------------------------------------------------------"
echo "Test Number 21"
echo "Test: nre --chmod -d junk test.nrx"
echo "Will produce error"
echo "------------------------------------------------------------------------"
nre --chmod -d junk test.nrx
read -s -n1 -p "Press a key to continue or CRTL-C to quit"
echo ""

echo "------------------------------------------------------------------------"
echo "Test Number 22"
echo "Test: nre -h"
echo "Will show help"
echo "------------------------------------------------------------------------"
nre -h
read -s -n1 -p "Press a key to continue or CRTL-C to quit"
echo ""

echo "------------------------------------------------------------------------"
echo "Test Number 23"
echo "Test: nre -v"
echo "Will show version"
echo "------------------------------------------------------------------------"
nre -v
read -s -n1 -p "Press a key to continue or CRTL-C to quit"
echo ""

echo "------------------------------------------------------------------------"
echo "Create one file that can use -arg"
echo "------------------------------------------------------------------------"
echo "say \"arg was: \" arg" > "talk.nrx"

echo "Done"

echo "------------------------------------------------------------------------"
echo "Test Number 24"
echo "Execute same file with two different args:"
echo " nre talk.nrx -arg Net talk.nrx -arg Rexx"
echo "------------------------------------------------------------------------"
nre talk.nrx -arg Net talk.nrx -arg Rexx
read -s -n1 -p "Press a key to continue or CRTL-C to quit"
echo ""

echo "TODO: test symbolic links"
echo "Cool uses:"
echo "Goto GuiApp.nrx from examples:"
echo "nre --chmod -a GuiApp.nrx"
echo "./GuiApp.nrx runs file"
echo "Do mime/type or Desktop Entries and scripts run in file manager/Desktops"
echo "simple cgi"
echo "NetRexx's parse vs. regular expressions in bash scripts"
echo "Pipes,redirection and etc......"
echo "========================================================================"
echo "========================================================================"
echo "As with all things of 0 and 1,"
echo "MAKE NO MISTAKES REGARDING SECURITY"
echo "========================================================================"
echo "========================================================================"

