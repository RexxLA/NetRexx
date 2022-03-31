You are looking at the readme for the Ant NetRexx task distributed by the Rexx Language Association.

This version of the Ant task is written in NetRexx. Both the source and binary for the task are included in the NetRexx distributions.

This version differs from the current Apache version (as of 2011/10/24) in the following ways:

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

(4) A new option "javaDir" allows the translated java files to be kept separate from the source and class files.

(5) Options "warnexit0", "java", "exec", "annotations", "address", and "arg" can be passed to the translator now.

(6) A new option "removeSourceCopies" will delete the duplicate copies of the source files made during processing.

(7) Option "destDir" is now optional and defaults to the "srcDir" value.

(8) Option "crossref" now defaults to "no". 

(9) Warning! When Ant starts, it will load the "NetRexxC" class (task) from the ant-netrexx.jar file in the directory where ant.jar is found. If you cannot or do not want to replace that copy
		with this one, you must do the following to use this version - prior to any NetRexx compiles in your build.xml, execute the following taskdef with a classpath that includes this copy
		of ant-netrexx.jar:
		
    <taskdef name="nrc" classname="org.apache.tools.ant.taskdefs.optional.NetRexx" classpath="${this.classpath}"/>
    
    Then you can use "nrc" in place of "netrexxc" to run this version of the task rather than the old version.
        
--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------    
This package structure will be built upon in the coming releases. For remarks, corrections and suggestions, please use the developers mailing list.

