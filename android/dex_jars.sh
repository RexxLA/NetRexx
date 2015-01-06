# Script to dex needed files EXPORTS and VARS NEED FIXING
export SDK=$SDK/home/agrellum/android-sdks/build-tools/18.1.1

export TOOLS=$TOOLS/home/agrellum/android-sdks/tools

mkdir dex

# create dex file for the NetRexx compiler

$SDK/dx -JXms1024m -JXmx1024m --dex --output=dex/temp.jar build/lib/NetRexxC.jar

$TOOLS/zipalign 4 dex/temp.jar dex/NetRexxC.jar

rm dex/temp.jar

# create dex file for the NetRexx runtime

$SDK/dx -JXms1024m -JXmx1024m --dex --output=dex/temp.jar build/lib/NetRexxR.jar

$TOOLS/zipalign 4 dex/temp.jar dex/NetRexxR.jar

rm dex/temp.jar

# create dex file for the NetRexx ant tasks jar

$SDK/dx -JXms1024m -JXmx1024m --dex --output=dex/temp.jar build/lib/ant-netrexx.jar

$TOOLS/zipalign 4 dex/temp.jar dex/ant-netrexx.jar

rm dex/temp.jar

# create dex file for the ECJ compiler

$SDK/dx -JXms1024m -JXmx1024m --dex --output=dex/temp.jar lib/ecj-4.2.jar

$TOOLS/zipalign 4 dex/temp.jar dex/ecj-4.2.jar

rm dex/temp.jar
