# The NetRexx Language

NetRexx is a variant of the REXX programming language to run on the Java virtual machine. It supports a classic REXX syntax, with no reserved keywords, along with considerable additions to support object-oriented programming in a manner compatible with Java's object model, yet can be used as both a compiled and an interpreted language, with an option of using only data types native to the JVM or the NetRexx runtime package. Originally from IBM, NetRexx is the creation of Mike Cowlishaw, the 'Father of Rexx'. This IBM product has been open sourced in 2011.

Features
- Part of the Rexx family of languages
- Runs on the JVM
- Generates .class files from NetRexx source, or can be interpreted
- Fast, portable and friendly

# Building the translator

It is easy to build the translator from source. Prerequisites are:

1.  A Java Virtual Machine

2.  A Git client

NetRexx can be built on all platforms that it runs on. NetRexx has been
bootstrapped since 1996 and subsequently has been used to compile
itself. Every checkout of the source code contains the 'bootstrap'
compiler, which is normally the previous release version. Only the
official release branches contain the same release of the compiler - to
prove that it still can compile itself on release. Theoretically, it is
possible to break things by introducing changes that preclude the
compiler to compile itself - it is our job that these changes are not
released to a wider audience, but rolled back in time.

## Repository

The source code repository is hosted at the SourceForge Git repository.
To get the code on your system, you should register at the NetRexx
project at SourceForce and clone the repository using Git. For this
version management package there are many graphical user interfaces, but
what is shown here, is the command line version. Choose a suitable place
as working directory - you can later move it around as you please.
You can clone from this repository, or from sourceforge.
`git clone git@github.com:RexxLA/NetRexx.git`
or
`git clone git://git.code.sf.net/p/netrexx/code netrexx-code`

The master branch contains the most current version of the source code,
including the documentation, examples and test cases.

## The buildfile

The official buildfile is called `build.xml` and the `ant` utility is used for building
from source. `ant` is present in the source repository.
The `build.xml` file contains a number of tasks. To build the
translator, make sure that the top level directory that is cloned from
git is the current directory, and issue the command:

`java -jar ant/ant-launcher.jar`

This will build the translator (compiler and interpreter) from source
and create a ```build/lib``` directory in the
current directory. Here the NetRexxC and NetRexxR jars are put by the
archiving process that is started by the task. These new jars can be
used immediately, by having them (NetRexxC.jar will suffice on a system
that has a functioning Java compiler) on the classpath.
