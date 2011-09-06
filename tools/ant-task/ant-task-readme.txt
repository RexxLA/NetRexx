You are looking at the readme for the Ant NetRexx task distributed by the Rexx Language Association.

This version of the Ant task is written in NetRexx. Both the source and binary for the task are included in the NetRexx distributions.

This version differs from the current Apache version (as of 2011/09/06) in the following ways:

(1) Support for the new option 'keepasjava' is added, which removes the necessity to rename *.java.keep files to *.java when used. The old option "removeKeepExtension" can still be used but is not as efficient.

(2) Initial support for nested classpath elements has been added. This allows tasks to be structured as in the following example rather than pre-building a classpath operand:

  <target name="compile">
    <netrexxc srcDir="." destDir="build/classes/here" includes="*.nrx">
    <classpath>
      <pathelement location="build/classes"/>
      <fileset dir="lib">
        <include name="*.jar"/>
      </fileset>
    </classpath>
    </netrexxc>
  </target>

(3) The ant task automatically adds the destination directory to the classpath. Using "ant -verbose" will display the final classpath used by the compiler.
  
This package structure will be built upon in the coming releases. For remarks, corrections and suggestions, please use developers@netrexx.kenai.com.

