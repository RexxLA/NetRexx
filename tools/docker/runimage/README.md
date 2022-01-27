# Building a Docker container which contains the NetRexx runtime you just built

This directory contains the code needed to build a runnable image. On the same level a directory `buildimage` exists, which builds a container image in which NetRexx can be built, which includes the needed tools.

The specification of the container to build is in the file named `Dockerfile`. To build this file without change, issue the following command on the commandline, in this directory.

`docker build -t rvjansen/netrexx:4.02 .`

and be sure to change `rvjansen` into your own docker userid, or leave it out (in which case pushing the container to docker.io becomes harder - it can be used locally though, or exchanged through other means). Also, make sure the tag (the part after the `:` matches the release or some other descriptor you want to give it.) The `docker build` command picks up the `Dockerfile` file without having to specify it.

## Pushing the image to docker.io
To enable other people to run your image (or use it yourself on other platforms), you can push the image to docker.io or another container repository. For this. of course, the previous step needs to have succeeded.

Push the image using the following command on the commandline in this directory:

`docker push rvjansen/netrexx:4.02`

Be sure to change the userid to your own and the descriptor matches what you just built.

## Building an image with more architecture layers

First, a local copy of the buildx tool must be created, because the standard version can only build for the current architecture.

`docker buildx create --use`

This only needs to happen once per install of Docker.

Then the commandline for building the image for multiple architectures is:

`docker buildx build --platform linux/amd64,linux/arm64,linux/arm/v7 -t rvjansen/netrexx:latest --push .`

This will in fact build three images of which the layers are put together later, but these layers are only delivered for the requested architecture, which defaults to the instruction set the machine is running.
