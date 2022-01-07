FROM debian
LABEL maintainer="rvjansen@xs4all.nl"
# Set the working directory
WORKDIR /

# Copy the current directory contents into the container at /app
ADD .therc /root
ADD nrws /bin
ADD nrws.input /root

#
RUN apt-get update && \
apt-get install apt-utils -y && \
apt-get install readline-common -y && \
apt-get install git -y && \
apt-get install openjdk-11-jdk-headless -y && \
apt-get install zip -y && \
apt-get install wget -y && \
wget http://netrexx.org/files/NetRexx-4.01-GA.zip && \
unzip NetRexx-4.01-GA && \
apt-get install nano -y && \
apt-get install zsh -y && \
mv /bin/pipe /bin/pipr && \
chmod +x /bin/nrc && \
chmod +x /bin/NetRexxC.sh && \
chmod +x /bin/pipr && \
echo 'alias pipc="java org.netrexx.njpipes.pipes.compiler"' >>/root/.bashrc && \
echo 'alias pipe="java org.netrexx.njpipes.pipes.runner"' >>/root/.bashrc && \
echo 'alias pipc="java org.netrexx.njpipes.pipes.compiler"' >>/root/.zshrc && \
echo 'alias pipe="java org.netrexx.njpipes.pipes.runner"' >>/root/.zshrc 

# Make port 80 available to the world outside this container
#EXPOSE 80

# Define environment variable
ENV CLASSPATH .:/lib/NetRexxF.jar

# Run when the container launches
WORKDIR /root

