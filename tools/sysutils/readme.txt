
sysutils.nrx is the NetRexx implemention of the well known RexxUtils package
available in REXX and ObjectREXX, see
http://www.oorexx.org/docs/rexxref/c27535.htm

It may attract Rexx users to the NetRexx platform, and it will certainly ease
migration from classic - or Object- Rexx to NetRexx, next to showing how
nicely NetRexx integrates with native Java.

Rexx users will recognise all well-known functions, all cross-platform
functions are covered, except SysGetKey - which has no obvious API in Java.
The semaphore functions use the Java 'semaphores' implementation, and thus
stay within the JVM, a JNI interface to externalize the semaphores to the OS
level is left to the reader.
Also added is rxExec, rcCmd functions to easily call external programs from
NetRexx, far reminiscent to - if I'm not mistaking - earlier LANUTILS.

The code is tested on Linux and Windows, and provided under the IBM License
Agreement for IBM Employee-Written Software, as sample code.

Marc Remes

mremes@be.ibm.com


