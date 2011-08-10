cd ..\net
start java SrvSockT 8000
cd ..\url
java GetHead http://w3.ibm.com/index.html
pause
Java UrlXTest http://loopback:8000/test.html
pause
Java UrlXTest http://loopback:8000/ueli.gif