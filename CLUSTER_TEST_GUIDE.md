# =================================================
# GUÍA DE VERIFICACIÓN DEL CLUSTER
# =================================================

## ✅ ESTADO ACTUAL
- NODO 1: http://localhost:8081 (SOAP: 8090)
- NODO 2: http://localhost:8082 (SOAP: 8091)
- NODO 3: http://localhost:8083 (SOAP: 8092)

## 🔍 PASOS PARA PROBAR EL SISTEMA

### PASO 1: Hacer Login (desde cualquier nodo)
POST http://localhost:8081/api/auth/login
Content-Type: application/json

{
  "username": "testuser",
  "password": "password123"
}

Guarda el TOKEN que recibes en la respuesta.

---

### PASO 2: Ver Estadísticas del Cluster
GET http://localhost:8081/api/cluster/stats
Authorization: Bearer TU_TOKEN

Deberías ver:
- totalNodes: 3
- onlineNodes: 3
- Los 3 nodos listados

---

### PASO 3: Subir un Archivo de Prueba
POST http://localhost:8081/api/storage/upload
Authorization: Bearer TU_TOKEN
Content-Type: multipart/form-data

Form data:
- file: [selecciona un archivo]
- path: /test-cluster/

El sistema lo distribuirá automáticamente entre los 3 nodos con replicación.

---

### PASO 4: Listar Archivos
GET http://localhost:8081/api/storage/list?path=/test-cluster
Authorization: Bearer TU_TOKEN

---

### PASO 5: Ver en qué nodos se replicó
GET http://localhost:8081/api/cluster/stats
Authorization: Bearer TU_TOKEN

Verás la carga (currentLoad) de cada nodo.

---

## 🎯 RESULTADO ESPERADO

Con replication.factor=3 y 3 nodos:
- Cada archivo se guardará en los 3 nodos
- Puedes apagar cualquier nodo y el archivo seguirá accesible
- El balanceador distribuirá nuevos archivos entre los nodos disponibles

---

## 🛠️ COMANDOS ÚTILES

### Ver puertos activos:
```powershell
netstat -ano | findstr "8081 8082 8083"
```

### Detener todos los nodos:
Cierra las 3 ventanas de PowerShell

### Reiniciar un nodo:
Ejecuta el script correspondiente:
- start-node1.bat
- start-node2.bat  
- start-node3.bat

---

¡Tu cluster está listo para usar! 🚀
