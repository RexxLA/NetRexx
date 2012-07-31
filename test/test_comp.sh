#!/bin/tcsh
#without property
java org.netrexx.process.NetRexxC ./hello.nrx -replace -keepasjava
#with javac
java -Dnrx.compiler=javac org.netrexx.process.NetRexxC ./hello.nrx -replace -keepasjava
#with ecj
java -Dnrx.compiler=ecj org.netrexx.process.NetRexxC ./hello.nrx -replace -keepasjava -diag
