# 🧪 GUÍA DE PRUEBAS DEL CLUSTER CON POSTMAN

## ✅ ESTADO DEL CLUSTER
- **NODO 1**: http://localhost:8081 (SOAP: 8090)
- **NODO 2**: http://localhost:8082 (SOAP: 8091)
- **NODO 3**: http://localhost:8083 (SOAP: 8092)
- **Factor de Replicación**: 3 (cada archivo en los 3 nodos)
- **Estrategia**: ROUND_ROBIN

---

## 📋 PASO 1: LOGIN (Obtener Token JWT)

### Request:
```
POST http://localhost:8081/api/auth/login
Content-Type: application/json
```

### Body (raw - JSON):
```json
{
  "username": "testuser",
  "password": "password123"
}
```

### Respuesta Esperada:
```json
{
  "status": "success",
  "message": "Login exitoso",
  "token": "eyJhbGciOiJIUzI1NiJ9...",
  "username": "testuser"
}
```

**⚠️ IMPORTANTE:** Guarda el `token` de la respuesta. Lo necesitarás para todas las siguientes peticiones.

---

## 📊 PASO 2: VER ESTADÍSTICAS DEL CLUSTER

### Request:
```
GET http://localhost:8081/api/cluster/stats
Authorization: Bearer TU_TOKEN_AQUI
```

### Headers:
```
Authorization: Bearer eyJhbGciOiJIUzI1NiJ9...
```

### Respuesta Esperada:
```json
{
  "status": "success",
  "message": "Estadísticas del cluster obtenidas exitosamente",
  "user": "testuser",
  "totalNodes": 3,
  "onlineNodes": 3,
  "offlineNodes": 0,
  "totalLoad": 0,
  "strategy": "ROUND_ROBIN",
  "replicationFactor": 3,
  "nodes": [
    {
      "name": "node1",
      "url": "http://localhost:8081",
      "status": "ONLINE",
      "currentLoad": 0
    },
    {
      "name": "node2",
      "url": "http://localhost:8082",
      "status": "ONLINE",
      "currentLoad": 0
    },
    {
      "name": "node3",
      "url": "http://localhost:8083",
      "status": "ONLINE",
      "currentLoad": 0
    }
  ]
}
```

**✅ Deberías ver los 3 nodos ONLINE con carga 0**

---

## 📁 PASO 3: CREAR UN DIRECTORIO DE PRUEBA

### Request:
```
POST http://localhost:8081/api/storage/directories
Authorization: Bearer TU_TOKEN_AQUI
Content-Type: application/json
```

### Body (raw - JSON):
```json
{
  "path": "/cluster-test"
}
```

### Respuesta Esperada:
```json
{
  "status": "success",
  "message": "Directorio creado exitosamente",
  "user": "testuser",
  "path": "/cluster-test"
}
```

---

## 📤 PASO 4: SUBIR UN ARCHIVO (Prueba de Distribución)

### Request:
```
POST http://localhost:8081/api/storage/upload
Authorization: Bearer TU_TOKEN_AQUI
Content-Type: multipart/form-data
```

### Body (form-data):
```
file: [Selecciona un archivo pequeño, ej: test.txt]
path: /cluster-test/
```

### Respuesta Esperada:
```json
{
  "status": "success",
  "message": "Archivo subido exitosamente",
  "user": "testuser",
  "file": {
    "name": "test.txt",
    "path": "/cluster-test/test.txt",
    "size": 1234,
    "uploadDate": "2025-10-16T00:00:00"
  }
}
```

**🔄 Lo que sucede internamente:**
- El archivo se sube al NODO 1
- Automáticamente se replica al NODO 2 y NODO 3
- Ahora hay 3 copias del mismo archivo

---

## 📊 PASO 5: VERIFICAR LA DISTRIBUCIÓN

### Request:
```
GET http://localhost:8081/api/cluster/stats
Authorization: Bearer TU_TOKEN_AQUI
```

### Respuesta Esperada:
```json
{
  "totalNodes": 3,
  "onlineNodes": 3,
  "totalLoad": 3,  ← NOTA: Ahora la carga total es 3
  "nodes": [
    {
      "name": "node1",
      "url": "http://localhost:8081",
      "status": "ONLINE",
      "currentLoad": 1  ← +1 archivo
    },
    {
      "name": "node2",
      "url": "http://localhost:8082",
      "status": "ONLINE",
      "currentLoad": 1  ← +1 réplica
    },
    {
      "name": "node3",
      "url": "http://localhost:8083",
      "status": "ONLINE",
      "currentLoad": 1  ← +1 réplica
    }
  ]
}
```

**✅ Cada nodo ahora tiene currentLoad = 1 (el archivo replicado)**

---

## 📋 PASO 6: LISTAR ARCHIVOS EN EL DIRECTORIO

### Request:
```
GET http://localhost:8081/api/storage/list?path=/cluster-test
Authorization: Bearer TU_TOKEN_AQUI
```

### Respuesta Esperada:
```json
{
  "status": "success",
  "message": "Contenido del directorio obtenido exitosamente",
  "user": "testuser",
  "path": "/cluster-test",
  "files": [
    {
      "name": "test.txt",
      "type": "file",
      "size": 1234,
      "lastModified": "2025-10-16T00:00:00"
    }
  ]
}
```

---

## 📥 PASO 7: DESCARGAR EL ARCHIVO

### Request:
```
GET http://localhost:8081/api/storage/download?path=/cluster-test/test.txt
Authorization: Bearer TU_TOKEN_AQUI
```

**✅ Deberías recibir el archivo para descargar**

---

## 🧪 PASO 8: PROBAR DESDE OTRO NODO (Alta Disponibilidad)

Repite las pruebas pero usando el **NODO 2** o **NODO 3**:

### Listar desde NODO 2:
```
GET http://localhost:8082/api/storage/list?path=/cluster-test
Authorization: Bearer TU_TOKEN_AQUI
```

### Descargar desde NODO 3:
```
GET http://localhost:8083/api/storage/download?path=/cluster-test/test.txt
Authorization: Bearer TU_TOKEN_AQUI
```

**✅ El archivo debería estar disponible desde cualquier nodo**

---

## 🎯 PASO 9: SUBIR MÁS ARCHIVOS (Ver Balanceo)

Sube 6 archivos más al directorio `/cluster-test/`:

```
POST http://localhost:8081/api/storage/upload
(Sube: archivo1.txt, archivo2.txt, archivo3.txt, etc.)
```

Luego verifica las estadísticas:

```
GET http://localhost:8081/api/cluster/stats
```

### Con ROUND_ROBIN verás algo como:
```json
{
  "totalLoad": 21,  ← 7 archivos × 3 réplicas = 21
  "nodes": [
    {
      "name": "node1",
      "currentLoad": 7  ← 7 archivos (todos replicados)
    },
    {
      "name": "node2",
      "currentLoad": 7
    },
    {
      "name": "node3",
      "currentLoad": 7
    }
  ]
}
```

---

## 🔄 PASO 10: VER INFORMACIÓN DE NODOS ESPECÍFICOS

### Información del NODO 1:
```
GET http://localhost:8081/api/cluster/nodes/node1
Authorization: Bearer TU_TOKEN_AQUI
```

### Información del NODO 2:
```
GET http://localhost:8081/api/cluster/nodes/node2
Authorization: Bearer TU_TOKEN_AQUI
```

### Información del NODO 3:
```
GET http://localhost:8081/api/cluster/nodes/node3
Authorization: Bearer TU_TOKEN_AQUI
```

---

## 🎲 PASO 11: OBTENER SIGUIENTE NODO (Balanceador)

```
GET http://localhost:8081/api/cluster/next-node
Authorization: Bearer TU_TOKEN_AQUI
```

### Respuesta:
```json
{
  "status": "success",
  "message": "Nodo asignado exitosamente",
  "node": {
    "name": "node2",
    "url": "http://localhost:8082",
    "status": "ONLINE",
    "currentLoad": 7
  }
}
```

**Llama varias veces este endpoint y verás cómo rota entre los 3 nodos (ROUND_ROBIN)**

---

## 🔄 PASO 12: RECARGAR CONFIGURACIÓN

Si modificas `nodes.properties`, puedes recargar sin reiniciar:

```
POST http://localhost:8081/api/cluster/reload
Authorization: Bearer TU_TOKEN_AQUI
```

---

## ✅ RESUMEN DE ENDPOINTS PRINCIPALES

| Acción | Método | URL |
|--------|--------|-----|
| Login | POST | `/api/auth/login` |
| Estadísticas Cluster | GET | `/api/cluster/stats` |
| Listar Nodos | GET | `/api/cluster/nodes` |
| Nodo Específico | GET | `/api/cluster/nodes/{nombre}` |
| Siguiente Nodo | GET | `/api/cluster/next-node` |
| Recargar Config | POST | `/api/cluster/reload` |
| Crear Directorio | POST | `/api/storage/directories` |
| Subir Archivo | POST | `/api/storage/upload` |
| Listar Archivos | GET | `/api/storage/list` |
| Descargar Archivo | GET | `/api/storage/download` |

---

## 🎯 COMPORTAMIENTO ESPERADO

1. ✅ Login desde cualquier nodo
2. ✅ Token funciona en los 3 nodos
3. ✅ Archivos se replican automáticamente
4. ✅ Cada archivo está en los 3 nodos
5. ✅ Balanceador distribuye la carga
6. ✅ Alta disponibilidad: cualquier nodo puede servir el archivo

---

¡Empieza con el PASO 1 y avanza! 🚀

