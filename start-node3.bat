@echo off
echo ========================================
echo  INICIANDO NODO 3 - Puerto 8083
echo  SOAP Puerto: 8092
echo ========================================
echo.
java -jar target\storagenode-1.0-SNAPSHOT.jar ^
  --server.port=8083 ^
  --soap.port=8092 ^
  --rmi.port=1299 ^
  --rmi.node.port=1298 ^
  --spring.application.name=storage-node-3 ^
  node3
