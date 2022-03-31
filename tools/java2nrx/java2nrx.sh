#!/bin/bash
#  BASH script to translate a Java source file to a NetRexx program,
#  and optionally compile and run the resulting class file
# -----------------------------------------------------------------
#  use as:  java2nrx hello
#
#     which will use the java2nrx translator to translate the
#     source file hello.java to hello.nrx
#     then will use NetRexxC to compile hello.nrx
#
#  OPTIONS keywords may be added (with a -) before or after
#  the file specification, along with the extra flags known
#  to NetRexxC (such as -keep).   For example:
#
#     java2nrx -keep -format -comments hello.java hello.nrx
#
#  Invoke with no parameters for a full list of flags.
#
#  To compile the nrx file after translation, specify -nrc as the
#  first word of the command arguments
#
#     java2nrx -nrc hello
#
#  Note when using -nrc, the original java file is temporary
#  renamed with extension java2nrx, during NetRexxC translation.
#
#  To run the class after compilation, specify -run as the
#  second word of the command arguments and the name of the
#  class as the third word.
#
#  Reminder: to make this executable: chmod 751 java2nrx.sh
# -----------------------------------------------------------------
#  2011.09.20 -- initial version

if test $# -eq 0; then
  echo 'Usage:' $0 '[-nrc] [-stdout] [-run]  [other options] <filename>'
  echo ' '
  exit 2
  # Drop through to display flags list
fi

args=''

if test "$1"  = "-nrc"; then
  shift 1
  netrexx_compile=yes
fi

if test "$1"  = "-run"; then
  shift 1
  netrexx_run=yes
fi

if test "$1"  = "-stdout"; then
  shift 1
  stdout=yes
fi

while [ true ] ; do
   arg=$1

   if [[ ${arg:0:1} == "-" ]] ; then
      args=$args' '$arg
      shift 1
   else
      break
   fi
done

file=$1
file=`echo $1 | sed 's/\.java$//'`

infile="$file.java"

if [[ $stdout == "yes" ]] ; then
   outfile=''
else
   outfile="$file.nrx"
fi

if [[ ! -f "$infile" ]] ; then
   echo "Unable to read $infile"
   exit 2
fi

scriptdir=${0%/*}

java -ms4M -jar $scriptdir'/'java2nrx.jar $infile $outfile
rc=$?
if [[ $rc -ne 0 ]] ; then
   echo "java2nrx failed"
   exit $rc
fi
if [[ $stdout == "yes" ]] ; then
   exit $?
fi

if [[ $netrexx_compile == "yes" ]] ; then
   mv $file.java $file.java2nrx
   java -ms4M $NETREXX_JAVA COM.ibm.netrexx.process.NetRexxC $args $file
   rc=$?
   mv $file.java2nrx $file.java
   if [[ $rc -ne 0 ]] ; then
      echo "NetRexxC failed"
      exit $rc
   fi
   if test $? -eq 0; then
     if test "$netrexx_run" = "yes"; then
       echo "Running $file..."
       java $file
     fi
   fi
fi
