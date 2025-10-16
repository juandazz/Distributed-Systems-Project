@echo off
echo ========================================
echo  INICIANDO NODO 1 - Puerto 8081
echo  SOAP Puerto: 8090
echo ========================================
echo.
java -jar target\storagenode-1.0-SNAPSHOT.jar ^
  --server.port=8081 ^
  --soap.port=8090 ^
  --rmi.port=1099 ^
  --rmi.node.port=1098 ^
  --spring.application.name=storage-node-1 ^
  node1
