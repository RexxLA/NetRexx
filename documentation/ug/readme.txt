You are looking at the readme for the NetRexx 3.03RC3 package - NetRexx-3.03RC3.zip

If you are new to NetRexx, please read the contents of the file
read.me.first, and the NetRexx Quick Start Guide (in a pdf in this package).

This is release candidate 3 for 3.03 and not the GA package.

Changes with respect to release candidate 1 include:

- functional: a fix for issue NETREXX-108, adding default and static
  methods in interfaces that Java 8 allows
- a new level of the ecj compiler, as the previous level proved defective
- dropped the requirement for JVM 8 to build the compiler from source

Changes with respect to release candidate 2 include:

- a change to the windows command scripts to automatically add the
  NetRexxF.jar to the classpath environment variable, and to default
  to the ecj compiler so its classes are found.
