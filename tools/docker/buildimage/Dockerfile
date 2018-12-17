FROM debian
LABEL maintainer="rvjansen@xs4all.nl"
# Set the working directory
WORKDIR /

# Copy the current directory contents into the container at /app
ADD clone.sh /

#
RUN apt-get update && \
apt-get install apt-utils -y && \
apt-get install readline-common -y && \
apt-get install git -y && \
apt-get install openjdk-8-jdk-headless -y && \
apt-get install zip -y && \
apt-get install nano -y && \
apt-get install zsh -y && \
apt-get install make

RUN ./clone.sh
WORKDIR /netrexx-code
RUN make

WORKDIR /netrexx-code/bin
RUN chmod +x nrc
RUN chmod +x pipe
RUN chmod +x NetRexxC.sh

# Make port 80 available to the world outside this container
#EXPOSE 80

# Define environment variable
ENV CLASSPATH .:/netrexx-code/build/lib/NetRexxF.jar

WORKDIR /netrexx-code
# Run when the container launches
#CMD 'git pull'
#CMD 'make'
