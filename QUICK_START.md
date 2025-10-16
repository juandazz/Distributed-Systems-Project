# 🚀 QUICK START - PRUEBA RÁPIDA DEL CLUSTER

## ✅ VERIFICAR QUE LOS NODOS ESTÉN CORRIENDO
```powershell
netstat -ano | findstr "8081 8082 8083"
```
Deberías ver los 3 puertos en LISTENING.

---

## 📝 PRUEBA RÁPIDA (5 MINUTOS)

### 1. REGISTRAR USUARIO (solo la primera vez)
```http
POST http://localhost:8081/api/auth/register
Content-Type: application/json

{
  "username": "demo",
  "email": "demo@test.com",
  "password": "demo123"
}
```

### 2. LOGIN
```http
POST http://localhost:8081/api/auth/login
Content-Type: application/json

{
  "username": "demo",
  "password": "demo123"
}
```


**➡️ GUARDA EL TOKEN QUE RECIBES**

eyJhbGciOiJIUzI1NiJ9.eyJ1c2VySWQiOjQsInVzZXJuYW1lIjoiZGVtbyIsInN1YiI6ImRlbW8iLCJpYXQiOjE3NjA1OTYyNTgsImV4cCI6MTc2MDY4MjY1OH0._n9xlv2rBJQgVrvj_vlLl5sX3vfvoGMDao8nu2a3UXA

### 3. VER CLUSTER
```http
GET http://localhost:8081/api/cluster/stats
Authorization: Bearer TU_TOKEN
```
**✅ Deberías ver 3 nodos ONLINE**

### 4. CREAR CARPETA
```http
POST http://localhost:8081/api/storage/directories
Authorization: Bearer TU_TOKEN
Content-Type: application/json

{
  "path": "/demo"
}
```

### 5. SUBIR ARCHIVO
```http
POST http://localhost:8081/api/storage/files
Authorization: Bearer TU_TOKEN
Content-Type: application/json

{
  "path": "/demo/prueba.txt",
  "content": "Hola desde el cluster! Este es mi primer archivo de prueba."
}
```

### 6. VERIFICAR REPLICACIÓN
```http
GET http://localhost:8081/api/cluster/stats
Authorization: Bearer TU_TOKEN
```
**✅ totalLoad debería ser 3 (archivo replicado en 3 nodos)**

### 7. DESCARGAR ARCHIVO
```http
GET http://localhost:8081/api/storage/files?path=/demo/prueba.txt
Authorization: Bearer TU_TOKEN
```

eyJhbGciOiJIUzI1NiJ9.eyJ1c2VySWQiOjQsInVzZXJuYW1lIjoiZGVtbyIsInN1YiI6ImRlbW8iLCJpYXQiOjE3NjA1OTYyNTgsImV4cCI6MTc2MDY4MjY1OH0._n9xlv2rBJQgVrvj_vlLl5sX3vfvoGMDao8nu2a3UXA

### 8. DESCARGAR DESDE OTRO NODO
```http
GET http://localhost:8082/api/storage/files?path=/demo/prueba.txt
Authorization: Bearer TU_TOKEN
```
**✅ El archivo se descarga desde el NODO 2 (replicación funcionando)**

---

## 🎉 RESULTADO
- ✅ Cluster de 3 nodos funcionando
- ✅ Replicación automática
- ✅ Alta disponibilidad
- ✅ Balanceo de carga

---

## 📚 GUÍA COMPLETA
Ver `POSTMAN_CLUSTER_TEST.md` para todas las pruebas detalladas.

## ⚙️ COMANDOS ÚTILES

### Verificar nodos corriendo:
```powershell
netstat -ano | findstr "8081 8082 8083"
```

### Reiniciar nodos:
```powershell
.\start-node1.bat
.\start-node2.bat
.\start-node3.bat
```

### Ver logs de MySQL:
```powershell
netstat -ano | findstr :3307
```

---

**¡Empieza con el PASO 1!** 🚀
