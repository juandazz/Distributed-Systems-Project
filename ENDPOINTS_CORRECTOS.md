# ✅ ENDPOINTS CORRECTOS - GUÍA RÁPIDA

## 🔐 AUTENTICACIÓN

### 1. Registro
```http
POST http://localhost:8081/api/auth/register
Content-Type: application/json

{
  "username": "demo",
  "email": "demo@test.com",
  "password": "demo123"
}
```

### 2. Login
```http
POST http://localhost:8081/api/auth/login
Content-Type: application/json

{
  "username": "demo",
  "password": "demo123"
}
```

---

## 📁 ALMACENAMIENTO

### 3. Crear Directorio
```http
POST http://localhost:8081/api/storage/directories
Authorization: Bearer TU_TOKEN
Content-Type: application/json

{
  "path": "/demo"
}
```

### 4. Subir Archivo (Texto)
```http
POST http://localhost:8081/api/storage/files
Authorization: Bearer TU_TOKEN
Content-Type: application/json

{
  "path": "/demo/test.txt",
  "content": "Este es el contenido del archivo de prueba"
}
```

### 5. Descargar Archivo
```http
GET http://localhost:8081/api/storage/files?path=/demo/test.txt
Authorization: Bearer TU_TOKEN
```

### 6. Eliminar Archivo
```http
DELETE http://localhost:8081/api/storage/files?path=/demo/test.txt
Authorization: Bearer TU_TOKEN
```

### 7. Información del Storage
```http
GET http://localhost:8081/api/storage/info
```

---

## 🔄 OPERACIONES AVANZADAS

### 8. Mover Archivo
```http
POST http://localhost:8081/api/storage/move
Authorization: Bearer TU_TOKEN
Content-Type: application/json

{
  "sourcePath": "/demo/test.txt",
  "destinationPath": "/demo/moved/test.txt"
}
```

### 9. Renombrar Archivo
```http
POST http://localhost:8081/api/storage/rename
Authorization: Bearer TU_TOKEN
Content-Type: application/json

{
  "path": "/demo/test.txt",
  "newName": "nuevo-nombre.txt"
}
```

### 10. Compartir Archivo
```http
POST http://localhost:8081/api/storage/share
Authorization: Bearer TU_TOKEN
Content-Type: application/json

{
  "path": "/demo/test.txt",
  "targetUsername": "otroUsuario",
  "permission": "READ"
}
```

### 11. Listar Archivos Compartidos
```http
GET http://localhost:8081/api/storage/shared
Authorization: Bearer TU_TOKEN
```

### 12. Estado de Replicación
```http
GET http://localhost:8081/api/storage/replication/status
Authorization: Bearer TU_TOKEN
```

### 13. Reporte de Redundancia
```http
GET http://localhost:8081/api/storage/redundancy/report?path=/demo/test.txt
Authorization: Bearer TU_TOKEN
```

---

## 📊 CLUSTER

### 14. Estadísticas del Cluster
```http
GET http://localhost:8081/api/cluster/stats
Authorization: Bearer TU_TOKEN
```

### 15. Listar Nodos
```http
GET http://localhost:8081/api/cluster/nodes
Authorization: Bearer TU_TOKEN
```

### 16. Info de Nodo Específico
```http
GET http://localhost:8081/api/cluster/nodes/node1
Authorization: Bearer TU_TOKEN
```

### 17. Obtener Siguiente Nodo (Balanceador)
```http
GET http://localhost:8081/api/cluster/next-node
Authorization: Bearer TU_TOKEN
```

### 18. Recargar Configuración
```http
POST http://localhost:8081/api/cluster/reload
Authorization: Bearer TU_TOKEN
```

---

## ⚠️ NOTA IMPORTANTE

**El sistema actual maneja archivos como contenido de texto en JSON**, no como multipart/form-data.

Si necesitas subir archivos binarios (imágenes, PDFs, etc.), debes usar un endpoint diferente o codificar el contenido en Base64.

Para esta prueba, usa archivos de texto simple.

---

## 🧪 PRUEBA COMPLETA PASO A PASO

### PASO 1: Login
```json
POST http://localhost:8081/api/auth/login
{
  "username": "testuser",
  "password": "testpass"
}
```
**Guarda el token**

### PASO 2: Ver Cluster
```
GET http://localhost:8081/api/cluster/stats
Authorization: Bearer TU_TOKEN
```
✅ Deberías ver 3 nodos ONLINE

### PASO 3: Crear Directorio
```json
POST http://localhost:8081/api/storage/directories
Authorization: Bearer TU_TOKEN
{
  "path": "/demo"
}
```

### PASO 4: Subir Archivo
```json
POST http://localhost:8081/api/storage/files
Authorization: Bearer TU_TOKEN
{
  "path": "/demo/prueba.txt",
  "content": "Hola desde el cluster distribuido!"
}
```

### PASO 5: Descargar Archivo
```
GET http://localhost:8081/api/storage/files?path=/demo/prueba.txt
Authorization: Bearer TU_TOKEN
```

### PASO 6: Descargar desde Otro Nodo
```
GET http://localhost:8082/api/storage/files?path=/demo/prueba.txt
Authorization: Bearer TU_TOKEN
```

### PASO 7: Ver Cluster de Nuevo
```
GET http://localhost:8081/api/cluster/stats
Authorization: Bearer TU_TOKEN
```

---

¡Usa estos endpoints correctos! 🚀
