# NetRexx Docker Images

Docker is a container technology. It is available for Mac, Windows and Linux. Information can be found at http://docker.com. The Community Edition can be used free of charge. In short, a container is combined from several images and runs a Linux OS that is mapped (or emulated) to calls for the host OS. This enables running NetRexx without installing it. 

Docker images are available for NetRexx since NetRexx 3.07. This has several advantages:

New releases can be tested without impacting the current installation of NetRexx
* Easy testing on multiple JDK / JRE versions (Oracle, Open JDK, IBM J9, etc)
* No JDK or JRE is required on your desktop / laptop / work machine to develop and run NetRexx programs
* No installation of NetRexx and its required path and classpath 
* An image is delivered with a Java version that is tested with the NetRexx release - so it is known to work
* The eclipse batch compiler will not be required
* NetRexx, its batchfiles and its classpath will be setup already
* When your app needs native calls, only one version needs to be produced and maintained

The Docker image will insulate against incompatible JVM changes where NetRexx development needs to catch up; is also enables its users to run different JDK releases in every docker container.

It will be the start of a distribution mechanism for NetRexx applications cq. libraries

Two ways of using the image are foreseen:

## A shell within the image
Working with a bind mounted directory in the shell of your local machine

As producing data within the image generally is not recommended this also involves a bind-mounted directory, but you will work inside of the shell in the docker container and you can use all the tools provided in the image. 

A suitable command line would be:

`docker run --rm -it -v "$PWD":/nrx -w /nrx rvjansen/netrexx:4.02 zsh`

If you want to keep changes in the container (for example, when you added tools or configuration that are useful and need to go into a new image, based on this image), do not use the --rm switch. The docker documentation explains how to commit this container and tag its new image. 
The -it switcher are needed for an interactive terminal session. the -v switch bind mounts the current directory into a directory /nrx in the image. You can find the files from your host OS (most of the time your source code) in the /nrx directory within the container. 

Next is the name of the image. The rvjansen/netrexx:4.02 will be downloaded once from the docker hub, when it is not on the local machine yet. Docker will know it has been downloaded the next time you start this image. Also, it will detect when the image has been updated.

zsh is the name of the shell to start. You could also start sh or bash. The example image is built on debian and OpenJDK 8.

The available tools within the image are being worked on, and documentation will be produced later. (Note: do not substitute your own userid for ‘rvjansen’ - that would be needed most of the time in examples, but this time it is in a (public) docker hub repository called rvjansen - we will make this a bit more ‘official’ going forward).
## Compile or exec from a shell on your host machine
The term 'host machine' is used here to indicate the fact that the docker image runs a guest OS. 

A suitable command line would be: (assuming you want to compile a class called RSAnrx in the local directory)

`docker run --rm -v "$PWD":/nrx -w /nrx rvjansen/netrexx:4.02 nrc RSAnrx`

Here, --rm will make sure the container is not kept, the -v tells docker to bind mount the current directory to a directory /nrx within the container, and -w sets this as the working directory. The rvjansen/netrexx:4.02 will be downloaded once from the docker hub, when it is not on the local machine yet. It will know it has been downloaded the next time you start this image. Of course, in most shells is it possible to alias this command, or start a batchfile, c.q. a shell script containing this.

The docker images will be updated from time to time, but the basic workings will stay the same.
