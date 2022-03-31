#!/bin/sh
#  BASH script to execute NetRexx Ant build
# -----------------------------------------------------------------
#  Reminder: to make this executable: chmod 751 nrc
# -----------------------------------------------------------------

java -jar ant/ant-launcher.jar -nouserlib -f buildsmall.xml $*
