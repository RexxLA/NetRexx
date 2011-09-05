You are looking at the readme for the first release candidate of the NetRexx 3.01 package.

This is RC1 and not the released package. Specifically, the following is incomplete:

- starter scripts for the translator
- User Guide and other documentation is incomplete or missing, respectively
- there is no building procedure for all the examples
- addition of the open sourced njPipes package, which implements CMS/TSO (Hartmann) Pipelines in NetRexx and Java.

This RC1 is meant for experienced users. The NetRexxC.jar and NetRexxR.jar files have been tested and are fit for use. New features might be changed and/or repealed until the official version is issued.

The 3.01 will mark the first official RexxLA release. Changes are:

- the package name for the translator has been changed from COM.ibm.netrexx.process to org.netrexx.process. This is the first package name change since release 0.90 (17 Dec 1996) when the translator package name was changed from netrexx.process to COM.ibm.netrexx.process; this was to comply with a short-lived naming convention for Java in which the first qualifier should be an uppercased top-level domain name; NetRexx was one of the few packages that followed this advice.

- the 'loop over' construct now also works for collection classes and behaves the same way as the 'loop over' for enumerations -NETREXX-13 (Kermit Kiser and Patric Bechtel)

- a bug in 2.05 (and probably dating back to earlier releases) which a translator commandline option of '-nocrossref' that was overridden by a program option of 'crossref' led to a NullPointerException in RxStreamer.crossref() has been resolved (NETREXX-28). A warning for conflicting options is issued, while the crossref file object pointer is allocated.

- the translator displays a build date on startup

- addition of several NetRexx examples from the RosettaCode.org site ( http://rosettacode.org ) and the IBM Redbook

- an example for the use of NetRexx with MS-Excel on Windows systems, using the Jacob library

- a new NetRexx Language Reference (nrl3) donated by Mike Cowlishaw, which incorporates the NetRexx 2 Language Reference, the NetRexx Overview and the Language Supplement

- a new version of the NetRexx User's Guide

- a new API to translate (interpret and/or compile) a NetRexx source from a memory buffer instead of a file - Kermit Kiser (NETREXX-5).

- a solution for NETREXX-18, Universal build support with an ant task. NetRexx can now be built on any Java SDK in a platform independent way; bootstrap compiler is in the version repository (Patric Bechtel, Kermit Kiser)

This package structure will be built upon in the coming releases. For remarks, corrections and suggestions, please use developers@netrexx.kenai.com.

