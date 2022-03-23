# Running the JavaFX examples
This directory has two samples that illustrate the usage of JavaFX user interface code using NetRexx. They focus on the new functionality in which the JavaFX libraries are packages in Java Modules, and we use the JPMS for compilation and execution. For Java 8, the JavaFX library needs to be on the CLASSPATH (for the EnzoLCD example, also the includes Enzo-0.3.6.jar)and everything will work. For the JPMS version, which works starting NetRexx 4.0.3, the following steps can be executed:

1) First, if you did not do this already, download the JavaFX SDK from JavaFX.io
(Choose the right platform (OS and architecture) from the dropdowns)
2) Unzip this directory to somewhere on your disk. Note down this location.
E.g. apps/javafx-18/
3) Include an export PATH_TO_FX=<that directory>/lib in your shell environment;
E.g. `export PATH_TO_FX=/Users/rvjansen/javafx-18/lib`; be sure to change the userid to your own
4) add `export JDK_JAVA_OPTIONS="--module-path=$PATH_TO_FX --add-modules=javafx.controls"` to your environment
5) Ensure the Enzo-0.3.6.jar library is on the classpath
6) compile the examples as usual with:

          nrc TreeViewSampleNRX

This will tell you it has picked up the JDK_JAVA_OPTIONS:

```
➜  javafx git:(master) ✗ nrc TreeViewSampleNRX
NOTE: Picked up JDK_JAVA_OPTIONS: --module-path=/Users/rvjansen/apps/javafx-sdk-18/lib --add-modules=javafx.controls
NetRexx portable processor 4.03-PRE build 59-20220128-1931
Copyright (c) RexxLA, 2011,2022.   All rights reserved.
Parts Copyright (c) IBM Corporation, 1995,2008.
Program TreeViewSampleNRX.nrx
  === class TreeViewSampleNRX ===
    function main(String[])
    method start(Stage)
      implements Application.start(Stage)
Compilation of 'TreeViewSampleNRX.nrx' successful
```
7) The same goes for the EnzoLCD sample.
8) Run the samples as usual; this needs the JDK_JAVA_OPTIONS set
9) Using other JavaFX functionality might require more modules on the `--add modules` statement. For this, leave out the `=` and separate the modules with comma's.

