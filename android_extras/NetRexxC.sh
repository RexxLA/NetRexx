# Adding changing setup in Terminal IDE and ADDing $NRC variable
if test $# -eq 0; then
  echo 'Usage:' $0 '[-run] [other options] filename'
  echo ' '
  # Drop through to display flags list
fi

if test "$1"  = "-run"; then
  shift 1
  netrexx_run=yes
fi

java -jar $NRC org.netrexx.process.NetRexxC -classpath $CLASSPATH $*

if test $? -eq 0; then
  if test "$netrexx_run" = "yes"; then
    echo "Running $1..."
    if [ ! -f $1".class" ]; then
        echo "-run error: class file not found - do not add .nrx to name"
        exit
        fi
    dx --dex --output=$1".jar" $1".class"
    if [ ! -f $1".jar" ]; then
        echo "-run error: dex conversion failed"
        exit
        fi
    java -jar $NRC:$1".jar" $1
  fi
fi
