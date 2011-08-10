echo Starting simple HTML server
start java SrvSockT 8000

echo HTML client
java ClntSock loopback 8000 GET /