FROM debian
LABEL maintainer="rvjansen@xs4all.nl"
# Set the working directory
WORKDIR /

# Copy the current directory contents into the container at /app
ADD .therc /root

#
RUN apt-get update && \
apt-get install apt-utils -y && \
apt-get install readline-common -y && \
apt-get install git -y && \
apt-get install openjdk-8-jdk-headless -y && \
apt-get install zip -y && \
apt-get install wget -y && \
wget http://netrexx.org/files/NetRexx-3.07-BETA.zip && \
unzip NetRexx-3.07-BETA && \
apt-get install nano -y && \
apt-get install regina-rexx -y && \
apt-get install the -y && \
apt-get install the-doc && \
apt-get install zsh -y && \
chmod +x /bin/nrc && \
chmod +x /bin/NetRexxC.sh

# Make port 80 available to the world outside this container
#EXPOSE 80

# Define environment variable
ENV CLASSPATH /lib/NetRexxF.jar

# Run when the container launches
