# 🧪 PRUEBA COMPLETA DEL CLUSTER CON POSTMAN

## 📌 PREREQUISITOS
- Los 3 nodos deben estar corriendo:
  - NODO 1: http://localhost:8081
  - NODO 2: http://localhost:8082
  - NODO 3: http://localhost:8083

---

## ✅ CHECKLIST DE PRUEBAS COMPLETAS

### 🔐 Autenticación
- [ ] **PASO 1:** Registrar nuevo usuario
- [ ] **PASO 2:** Login y obtener token JWT
- [ ] **PASO 2.1:** Verificar que el token es válido
- [ ] **PASO 2.2:** Comprobar si usuario existe

### 📊 Cluster
- [ ] **PASO 3:** Ver estadísticas del cluster (3 nodos online)

### 📁 Gestión de Archivos
- [ ] **PASO 4:** Crear directorio de prueba
- [ ] **PASO 5:** Subir archivo al cluster
- [ ] **PASO 6:** Verificar replicación (archivo en 3 nodos)
- [ ] **PASO 7:** Listar archivos en el directorio
- [ ] **PASO 8:** Descargar archivo desde los 3 nodos

### 🎯 Distribución y Balanceo
- [ ] **PASO 9:** Subir múltiples archivos y ver distribución

### 🔄 Alta Disponibilidad
- [ ] **PASO 10:** Apagar un nodo y verificar disponibilidad
- [ ] **PASO 10:** Reiniciar el nodo caído

### 🌐 Multi-Nodo
- [ ] **PASO 11:** Operar desde diferentes nodos

---

## 📝 NOTAS IMPORTANTES:
- Usa `testuser2` y `testpass2` para el nuevo usuario (o los que prefieras)
- `testuser` con `testpass` ya existe en el sistema
- El token JWT expira en 24 horas
- Todos los nodos comparten la misma base de datos
- Los archivos se replican automáticamente con factor 3

---

## � PASO 1: REGISTRAR UN NUEVO USUARIO

### Request:
```
POST http://localhost:8081/api/auth/register
Content-Type: application/json
```

### Body (raw JSON):
```json
{
  "username": "testuser2",
  "email": "testuser2@example.com",
  "password": "testpass2"
}
```

### Respuesta Esperada:
```json
{
    "status": "success",
    "message": "Usuario registrado exitosamente",
    "user": {
        "id": 2,
        "username": "testuser2",
        "email": "testuser2@example.com"
    }
}
```

**✅ Ahora tienes un nuevo usuario registrado en el sistema**

---

## �🔐 PASO 2: LOGIN Y OBTENER TOKEN

### Request:
```
POST http://localhost:8081/api/auth/login
Content-Type: application/json
```

### Body (raw JSON):
```json
{
  "username": "testuser2",
  "password": "testpass2"
}
```

### Respuesta Esperada:
```json
{
    "status": "success",
    "message": "Login exitoso",
    "token": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ0ZXN0dXNlcjIiLCJ1c2VySWQiOjIsImlhdCI6MTcyOTAzODAwMCwiZXhwIjoxNzI5MTI0NDAwfQ...",
    "user": {
        "id": 2,
        "username": "testuser2",
        "email": "testuser2@example.com"
    }
}
```

**⚠️ IMPORTANTE:** Copia el valor de `token` y úsalo en todos los siguientes requests.

---

## ✅ PASO 2.1: VERIFICAR TOKEN (Opcional)

### Request:
```
POST http://localhost:8081/api/auth/validate-token
Authorization: Bearer TU_TOKEN_AQUI
```

### Respuesta Esperada:
```json
{
    "status": "success",
    "message": "Token válido",
    "user": {
        "id": 2,
        "username": "testuser2"
    }
}
```

---

## 🔍 PASO 2.2: VERIFICAR SI USUARIO EXISTE (Opcional)

### Request:
```
GET http://localhost:8081/api/auth/check-user/testuser2
```

### Respuesta Esperada:
```json
{
    "exists": true,
    "username": "testuser2"
}
```

---

## 📊 PASO 3: VERIFICAR ESTADÍSTICAS DEL CLUSTER

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
            "currentLoad": 0,
            "freeSpace": 999999999
        },
        {
            "name": "node2",
            "url": "http://localhost:8082",
            "status": "ONLINE",
            "currentLoad": 0,
            "freeSpace": 999999999
        },
        {
            "name": "node3",
            "url": "http://localhost:8083",
            "status": "ONLINE",
            "currentLoad": 0,
            "freeSpace": 999999999
        }
    ]
}
```

**✅ Verificar:**
- `totalNodes: 3`
- `onlineNodes: 3`
- Todos los nodos tienen `status: "ONLINE"`

---

## 📁 PASO 4: CREAR DIRECTORIO DE PRUEBA

### Request:
```
POST http://localhost:8081/api/storage/directories
Authorization: Bearer TU_TOKEN_AQUI
Content-Type: application/json
```

### Body (raw JSON):
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
    "path": "/cluster-test",
    "user": "testuser"
}
```

---

## 📤 PASO 5: SUBIR ARCHIVO AL CLUSTER

### Request:
```
POST http://localhost:8081/api/storage/upload
Authorization: Bearer TU_TOKEN_AQUI
Content-Type: multipart/form-data
```

### Body (form-data):
- **Key:** `file` (tipo: File)
  - **Value:** Selecciona cualquier archivo de prueba (ej: test.txt)
- **Key:** `path` (tipo: Text)
  - **Value:** `/cluster-test/`

### Respuesta Esperada:
```json
{
    "status": "success",
    "message": "Archivo subido exitosamente",
    "filename": "test.txt",
    "path": "/cluster-test/test.txt",
    "size": 1234,
    "user": "testuser",
    "uploadTime": "2025-10-16T00:50:00"
}
```

**🔄 ¿Qué sucede internamente?**
1. El archivo se sube al NODO 1 (o el que especifiques)
2. El sistema automáticamente lo REPLICA en los otros 2 nodos
3. Ahora tienes 3 copias del archivo en diferentes nodos

---

## 📊 PASO 6: VERIFICAR REPLICACIÓN (Ver carga de nodos)

### Request:
```
GET http://localhost:8081/api/cluster/stats
Authorization: Bearer TU_TOKEN_AQUI
```

### Respuesta Esperada:
```json
{
    "status": "success",
    "totalNodes": 3,
    "onlineNodes": 3,
    "totalLoad": 3,
    "nodes": [
        {
            "name": "node1",
            "currentLoad": 1,
            "status": "ONLINE"
        },
        {
            "name": "node2",
            "currentLoad": 1,
            "status": "ONLINE"
        },
        {
            "name": "node3",
            "currentLoad": 1,
            "status": "ONLINE"
        }
    ]
}
```

**✅ Verificar:**
- `totalLoad: 3` (el archivo se replicó en los 3 nodos)
- Cada nodo tiene `currentLoad: 1`

---

## 📋 PASO 7: LISTAR ARCHIVOS

### Request:
```
GET http://localhost:8081/api/storage/list?path=/cluster-test
Authorization: Bearer TU_TOKEN_AQUI
```

### Respuesta Esperada:
```json
{
    "status": "success",
    "message": "Contenido listado exitosamente",
    "path": "/cluster-test",
    "files": [
        {
            "name": "test.txt",
            "type": "file",
            "size": 1234,
            "lastModified": "2025-10-16T00:50:00"
        }
    ],
    "user": "testuser"
}
```

---

## 📥 PASO 8: DESCARGAR ARCHIVO (Desde cada nodo)

### Descargar desde NODO 1:
```
GET http://localhost:8081/api/storage/download?path=/cluster-test/test.txt
Authorization: Bearer TU_TOKEN_AQUI
```

### Descargar desde NODO 2:
```
GET http://localhost:8082/api/storage/download?path=/cluster-test/test.txt
Authorization: Bearer TU_TOKEN_AQUI
```

### Descargar desde NODO 3:
```
GET http://localhost:8083/api/storage/download?path=/cluster-test/test.txt
Authorization: Bearer TU_TOKEN_AQUI
```

**✅ El archivo debe descargarse exitosamente desde TODOS los nodos** (comprobando la replicación)

---

## 🎯 PASO 9: SUBIR MÁS ARCHIVOS Y VER LA DISTRIBUCIÓN

Sube 3 archivos más y observa cómo el balanceador ROUND_ROBIN los distribuye:

### Archivo 2:
```
POST http://localhost:8081/api/storage/upload
Body: file=archivo2.txt, path=/cluster-test/
```

### Archivo 3:
```
POST http://localhost:8081/api/storage/upload
Body: file=archivo3.txt, path=/cluster-test/
```

### Archivo 4:
```
POST http://localhost:8081/api/storage/upload
Body: file=archivo4.txt, path=/cluster-test/
```

**Luego verifica las estadísticas:**
```
GET http://localhost:8081/api/cluster/stats
```

**Resultado esperado:**
- Cada archivo se replica en los 3 nodos
- `totalLoad` aumentará a 12 (4 archivos × 3 réplicas)

---

## 🔄 PASO 10: PROBAR TOLERANCIA A FALLOS

1. **Cierra una ventana de PowerShell** (por ejemplo, el NODO 3)
2. **Verifica el cluster:**
   ```
   GET http://localhost:8081/api/cluster/stats
   ```
   
   Deberías ver:
   ```json
   {
       "onlineNodes": 2,
       "offlineNodes": 1,
       "nodes": [
           { "name": "node1", "status": "ONLINE" },
           { "name": "node2", "status": "ONLINE" },
           { "name": "node3", "status": "OFFLINE" }
       ]
   }
   ```

3. **Intenta descargar el archivo:**
   ```
   GET http://localhost:8081/api/storage/download?path=/cluster-test/test.txt
   ```
   
   **✅ El archivo SIGUE DISPONIBLE** porque está en los otros 2 nodos

4. **Reinicia el NODO 3:**
   - Ejecuta `start-node3.bat`
   - Espera 10 segundos
   - Verifica estadísticas nuevamente
   - El nodo debería volver a `ONLINE`

---

## 🎨 PASO 11: PROBAR DESDE DIFERENTES NODOS

Puedes hacer TODAS las operaciones desde cualquier nodo:

### Login desde NODO 2:
```
POST http://localhost:8082/api/auth/login
```

### Subir archivo a NODO 3:
```
POST http://localhost:8083/api/storage/upload
```

### Listar desde NODO 1:
```
GET http://localhost:8081/api/storage/list?path=/cluster-test
```

**✅ Todos los nodos comparten la misma base de datos**, así que:
- Los usuarios son los mismos
- Los archivos son accesibles desde cualquier nodo
- Las operaciones son consistentes

---

## 📊 RESUMEN DE ENDPOINTS DEL CLUSTER

| Endpoint | Método | Descripción |
|----------|--------|-------------|
| `/api/cluster/stats` | GET | Ver estadísticas completas |
| `/api/cluster/nodes` | GET | Listar todos los nodos |
| `/api/cluster/nodes/{name}` | GET | Ver un nodo específico |
| `/api/cluster/next-node` | GET | Obtener próximo nodo (balanceador) |
| `/api/cluster/reload` | POST | Recargar configuración |

---

## 📚 RESUMEN DE TODOS LOS ENDPOINTS

### 🔐 Autenticación
| Endpoint | Método | Headers | Body | Descripción |
|----------|--------|---------|------|-------------|
| `/api/auth/register` | POST | `Content-Type: application/json` | `{"username":"...", "email":"...", "password":"..."}` | Registrar nuevo usuario |
| `/api/auth/login` | POST | `Content-Type: application/json` | `{"username":"...", "password":"..."}` | Iniciar sesión y obtener token |
| `/api/auth/validate-token` | POST | `Authorization: Bearer TOKEN` | - | Validar token JWT |
| `/api/auth/check-user/{username}` | GET | - | - | Verificar si usuario existe |

### 📊 Cluster
| Endpoint | Método | Headers | Query Params | Descripción |
|----------|--------|---------|--------------|-------------|
| `/api/cluster/stats` | GET | `Authorization: Bearer TOKEN` | - | Estadísticas completas del cluster |
| `/api/cluster/nodes` | GET | `Authorization: Bearer TOKEN` | - | Listar todos los nodos |
| `/api/cluster/nodes/{name}` | GET | `Authorization: Bearer TOKEN` | - | Info de un nodo específico |
| `/api/cluster/next-node` | GET | `Authorization: Bearer TOKEN` | - | Obtener siguiente nodo (balanceador) |
| `/api/cluster/reload` | POST | `Authorization: Bearer TOKEN` | - | Recargar configuración de nodos |

### 📁 Almacenamiento
| Endpoint | Método | Headers | Body/Params | Descripción |
|----------|--------|---------|-------------|-------------|
| `/api/storage/directories` | POST | `Authorization: Bearer TOKEN`<br>`Content-Type: application/json` | `{"path":"/ruta"}` | Crear directorio |
| `/api/storage/upload` | POST | `Authorization: Bearer TOKEN`<br>`Content-Type: multipart/form-data` | `file=...`<br>`path=/ruta/` | Subir archivo |
| `/api/storage/list` | GET | `Authorization: Bearer TOKEN` | `?path=/ruta` | Listar contenido |
| `/api/storage/download` | GET | `Authorization: Bearer TOKEN` | `?path=/ruta/archivo.txt` | Descargar archivo |

---

## 📋 COLECCIÓN DE POSTMAN

Para facilitar las pruebas, aquí tienes los requests listos para copiar:

### 1️⃣ REGISTRO
```
POST http://localhost:8081/api/auth/register
Content-Type: application/json

{
  "username": "testuser2",
  "email": "testuser2@example.com",
  "password": "testpass2"
}
```

### 2️⃣ LOGIN
```
POST http://localhost:8081/api/auth/login
Content-Type: application/json

{
  "username": "testuser2",
  "password": "testpass2"
}
```

### 3️⃣ ESTADÍSTICAS
```
GET http://localhost:8081/api/cluster/stats
Authorization: Bearer {{token}}
```

### 4️⃣ CREAR DIRECTORIO
```
POST http://localhost:8081/api/storage/directories
Authorization: Bearer {{token}}
Content-Type: application/json

{
  "path": "/cluster-test"
}
```

### 5️⃣ SUBIR ARCHIVO
```
POST http://localhost:8081/api/storage/upload
Authorization: Bearer {{token}}
Content-Type: multipart/form-data

form-data:
  - file: [selecciona archivo]
  - path: /cluster-test/
```

### 6️⃣ LISTAR ARCHIVOS
```
GET http://localhost:8081/api/storage/list?path=/cluster-test
Authorization: Bearer {{token}}
```

### 7️⃣ DESCARGAR ARCHIVO
```
GET http://localhost:8081/api/storage/download?path=/cluster-test/test.txt
Authorization: Bearer {{token}}
```

---

## 🎉 RESULTADO ESPERADO

Tu cluster distribuido con:
- ✅ 3 nodos funcionando
- ✅ Balanceo de carga ROUND_ROBIN
- ✅ Replicación automática (factor 3)
- ✅ Alta disponibilidad (tolerancia a fallos)
- ✅ Health checks automáticos
- ✅ Consistencia de datos

**¡Tu sistema de almacenamiento distribuido está completamente funcional!** 🚀

