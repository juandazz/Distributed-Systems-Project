# ✅ CHECKLIST DE VERIFICACIÓN DEL CLUSTER

## 1️⃣ VERIFICAR PROCESOS JAVA
Abrir PowerShell y ejecutar:
```powershell
tasklist | findstr java
```

**Resultado esperado:** Deberías ver 3 procesos `java.exe`

---

## 2️⃣ VERIFICAR PUERTOS
```powershell
netstat -ano | findstr "8081 8082 8083"
```

**Resultado esperado:** Deberías ver los 3 puertos en LISTENING

---

## 3️⃣ PROBAR EN POSTMAN

### A. Login
```
POST http://localhost:8081/api/auth/login
Body: {"username": "testuser", "password": "testpass"}
```
✅ **Copiar el token**

### B. Ver Cluster
```
GET http://localhost:8081/api/cluster/stats
Headers: Authorization: Bearer TOKEN
```
✅ **Verificar que onlineNodes = 3**

### C. Probar Balanceador (hacer 3 veces)
```
GET http://localhost:8081/api/cluster/next-node
Headers: Authorization: Bearer TOKEN
```
✅ **Debería alternar entre node1, node2, node3**

---

## 4️⃣ INTERPRETACIÓN DE RESULTADOS

### ✅ TODO FUNCIONA SI VES:
```json
{
    "onlineNodes": 3,
    "offlineNodes": 0,
    "strategy": "ROUND_ROBIN"
}
```

### ⚠️ SOLO 1 NODO SI VES:
```json
{
    "onlineNodes": 1,
    "offlineNodes": 2
}
```
**Solución:** Inicia los otros 2 nodos en terminales separadas

### ⚠️ SOLO 2 NODOS SI VES:
```json
{
    "onlineNodes": 2,
    "offlineNodes": 1
}
```
**Solución:** Verifica qué nodo falta e inícialo

---

## 5️⃣ COMANDOS PARA INICIAR NODOS

### Terminal 1:
```powershell
cd C:\Users\juand\Desktop\storagenode
java -jar target/storagenode-1.0-SNAPSHOT.jar node1
```

### Terminal 2:
```powershell
cd C:\Users\juand\Desktop\storagenode
java -Dserver.port=8082 -jar target/storagenode-1.0-SNAPSHOT.jar node2
```

### Terminal 3:
```powershell
cd C:\Users\juand\Desktop\storagenode
java -Dserver.port=8083 -jar target/storagenode-1.0-SNAPSHOT.jar node3
```

---

## 6️⃣ PRUEBA COMPLETA DEL SISTEMA

### Paso 1: Login
```
POST http://localhost:8081/api/auth/login
```

### Paso 2: Ver estado del cluster
```
GET http://localhost:8081/api/cluster/stats
```

### Paso 3: Subir archivo
```
POST http://localhost:8081/api/storage/files
Body: {"path": "test.txt", "content": "Hola cluster"}
```

### Paso 4: Descargar archivo
```
GET http://localhost:8081/api/storage/files?path=test.txt
```

### Paso 5: Crear directorio
```
POST http://localhost:8081/api/storage/directories
Body: {"path": "nuevacarpeta"}
```

### Paso 6: Mover archivo
```
POST http://localhost:8081/api/storage/move
Body: {
    "sourcePath": "test.txt",
    "destinationPath": "nuevacarpeta/test.txt"
}
```

---

## 7️⃣ ENDPOINTS DEL CLUSTER

| Endpoint | Método | Descripción |
|----------|--------|-------------|
| `/api/cluster/stats` | GET | Estadísticas generales |
| `/api/cluster/nodes` | GET | Lista todos los nodos |
| `/api/cluster/nodes/{name}` | GET | Info de un nodo |
| `/api/cluster/next-node` | GET | Próximo nodo (balanceador) |
| `/api/cluster/reload` | POST | Recargar configuración |

---

## 8️⃣ TROUBLESHOOTING

### Problema: "onlineNodes: 0"
**Causa:** Ningún nodo está respondiendo
**Solución:** Verifica que al menos un nodo esté corriendo

### Problema: "Token inválido"
**Causa:** Token expiró o mal formateado
**Solución:** Haz login nuevamente

### Problema: "No hay nodos disponibles"
**Causa:** Todos los nodos están offline
**Solución:** Inicia al menos un nodo

### Problema: Puerto ya en uso
**Causa:** Ya hay un proceso en ese puerto
**Solución:** 
```powershell
netstat -ano | findstr "8081"
taskkill /F /PID [PID_DEL_PROCESO]
```

---

## ✅ CONFIRMACIÓN FINAL

Si al ejecutar:
```
GET http://localhost:8081/api/cluster/stats
```

Ves esto:
```json
{
    "totalNodes": 3,
    "onlineNodes": 3,
    "offlineNodes": 0
}
```

**🎉 ¡TU CLUSTER ESTÁ FUNCIONANDO PERFECTAMENTE!**