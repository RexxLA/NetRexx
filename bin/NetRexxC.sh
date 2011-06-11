#!/bin/sh
#  BASH script to translate and compile a NetRexx program, and
#  optionally run the resulting class file
# -----------------------------------------------------------------
#  use as:  NetRexxC hello
#
#     which will use the NetRexx translator to translate the
#     source file hello.nrx to hello.java
#     then will use javac to compile hello.java
#
#  OPTIONS keywords may be added (with a -) before or after
#  the file specification, along with the extra flags known
#  to NetRexxC (such as -keep).   For example:
#
#     NetRexxC -keep -format -comments hello
#
#  Invoke with no parameters for a full list of flags.
#
#  To run the class after compilation, specify -run as the
#  first word of the command arguments and the name of the
#  class as the second word.  Note that the case of the
#  letters must be exactly correct for this to work, and do not
#  specify the .nrx extension.  For example:
#
#     NetRexxC -run hello
#
#  For a more flexible script for this, see NetRexxC.cmd
#  Reminder: to make this executable: chmod 751 NetRexxC.sh
# -----------------------------------------------------------------
#  2000.08.20 -- initial version derived from NetRexxC.bat

if test $# -eq 0; then
  echo 'Usage:' $0 '[-run] [other options] filename'
  echo ' '
  # Drop through to display flags list
fi

if test "$1"  = "-run"; then
  shift 1
  netrexx_run=yes
fi

java -ms4M $NETREXX_JAVA COM.ibm.netrexx.process.NetRexxC $*
if test $? -eq 0; then
  if test "$netrexx_run" = "yes"; then
    echo "Running $1..."
    java $1
  fi
fi
