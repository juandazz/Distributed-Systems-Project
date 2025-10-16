@echo off
echo ========================================
echo  INICIANDO NODO 2 - Puerto 8082
echo  SOAP Puerto: 8091
echo ========================================
echo.
java -jar target\storagenode-1.0-SNAPSHOT.jar ^
  --server.port=8082 ^
  --soap.port=8091 ^
  --rmi.port=1199 ^
  --rmi.node.port=1198 ^
  --spring.application.name=storage-node-2 ^
  node2
