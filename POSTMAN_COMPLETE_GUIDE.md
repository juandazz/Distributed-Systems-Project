# 🚀 **GUÍA COMPLETA - TODAS LAS FUNCIONALIDADES REST API**

## 🔐 **AUTENTICACIÓN (Paso Previo)**

### **Login**
**POST** `http://localhost:8081/api/auth/login`

**Body:**
```json
{
    "username": "testuser",
    "password": "testpass"
}
```

**Respuesta:** Copia el `token` para usar en todos los siguientes endpoints.

---

## 📁 **FUNCIONALIDADES BÁSICAS**

### 1. **Crear Directorio**
**POST** `http://localhost:8081/api/storage/directories`

**Headers:**
```
Authorization: Bearer TU_TOKEN
Content-Type: application/json
```

**Body:**
```json
{
    "path": "documents/personal"
}
```

---

### 2. **Subir Archivo**
**POST** `http://localhost:8081/api/storage/files`

**Headers:**
```
Authorization: Bearer TU_TOKEN
Content-Type: application/json
```

**Body:**
```json
{
    "path": "documents/test.txt",
    "content": "Hello World from Postman!"
}
```

---

### 3. **Descargar Archivo**
**GET** `http://localhost:8081/api/storage/files?path=documents/test.txt`

**Headers:**
```
Authorization: Bearer TU_TOKEN
```

---

### 4. **Eliminar Archivo**
**DELETE** `http://localhost:8081/api/storage/files?path=documents/test.txt`

**Headers:**
```
Authorization: Bearer TU_TOKEN
```

---

### 5. **Info del Storage**
**GET** `http://localhost:8081/api/storage/info`

**Headers:**
```
Authorization: Bearer TU_TOKEN
```

---

## 🔥 **NUEVAS FUNCIONALIDADES AVANZADAS**

### 6. **🔄 MOVER Archivo/Directorio**
**POST** `http://localhost:8081/api/storage/move`

**Headers:**
```
Authorization: Bearer TU_TOKEN
Content-Type: application/json
```

**Body:**
```json
{
    "sourcePath": "documents/test.txt",
    "destinationPath": "backup/test.txt"
}
```

**Respuesta:**
```json
{
    "status": "success",
    "message": "Archivo/directorio movido exitosamente",
    "sourcePath": "C:\\...\\documents\\test.txt",
    "destinationPath": "C:\\...\\backup\\test.txt",
    "user": "testuser"
}
```

---

### 7. **✏️ RENOMBRAR Archivo/Directorio**
**POST** `http://localhost:8081/api/storage/rename`

**Headers:**
```
Authorization: Bearer TU_TOKEN
Content-Type: application/json
```

**Body:**
```json
{
    "path": "documents/test.txt",
    "newName": "test-renamed.txt"
}
```

**Respuesta:**
```json
{
    "status": "success",
    "message": "Archivo/directorio renombrado exitosamente",
    "oldPath": "C:\\...\\documents\\test.txt",
    "newPath": "C:\\...\\documents\\test-renamed.txt",
    "newName": "test-renamed.txt",
    "user": "testuser"
}
```

---

### 8. **🤝 COMPARTIR Archivo con Usuario**
**POST** `http://localhost:8081/api/storage/share`

**Headers:**
```
Authorization: Bearer TU_TOKEN
Content-Type: application/json
```

**Body:**
```json
{
    "path": "documents/test.txt",
    "targetUsername": "otheruser",
    "permission": "READ"
}
```

**Respuesta:**
```json
{
    "status": "success",
    "message": "Archivo compartido exitosamente",
    "owner": "testuser",
    "sharedWith": "otheruser",
    "path": "documents/test.txt",
    "permission": "READ",
    "sharedAt": "2025-10-15T18:30:00Z"
}
```

**Permisos disponibles:**
- `READ` - Solo lectura
- `READ_WRITE` - Lectura y escritura
- `ADMIN` - Control total

---

### 9. **📋 LISTAR Archivos Compartidos**
**GET** `http://localhost:8081/api/storage/shared`

**Headers:**
```
Authorization: Bearer TU_TOKEN
```

**Respuesta:**
```json
{
    "status": "success",
    "message": "Archivos compartidos obtenidos exitosamente",
    "user": "testuser",
    "count": 2,
    "sharedFiles": [
        {
            "path": "documents/shared-document.pdf",
            "owner": "juan",
            "sharedDate": "2025-10-15T10:30:00Z",
            "permission": "READ",
            "size": 1024000
        },
        {
            "path": "images/team-photo.jpg",
            "owner": "maria",
            "sharedDate": "2025-10-14T15:45:00Z",
            "permission": "READ_WRITE",
            "size": 2048000
        }
    ]
}
```

---

### 10. **💾 ESTADO DE REPLICACIÓN de Base de Datos**
**GET** `http://localhost:8081/api/storage/replication/status`

**Headers:**
```
Authorization: Bearer TU_TOKEN
```

**Respuesta:**
```json
{
    "status": "HEALTHY",
    "message": "Estado de replicación obtenido exitosamente",
    "user": "testuser",
    "primaryDatabase": {
        "host": "localhost:3306",
        "status": "ONLINE",
        "recordCount": 15420
    },
    "replicaDatabase": {
        "host": "replica-server:3306",
        "status": "ONLINE",
        "recordCount": 15420,
        "lastSyncTime": "2025-10-15T18:30:00Z",
        "syncDelaySeconds": 0.5
    },
    "syncStatus": {
        "syncedRecords": 15420,
        "pendingRecords": 0,
        "errorRecords": 0
    }
}
```

---

### 11. **📊 REPORTE DE REDUNDANCIA de Archivo**
**GET** `http://localhost:8081/api/storage/redundancy/report?path=documents/test.txt`

**Headers:**
```
Authorization: Bearer TU_TOKEN
```

**Respuesta:**
```json
{
    "status": "success",
    "message": "Reporte de redundancia generado exitosamente",
    "user": "testuser",
    "file": "documents/test.txt",
    "primaryCopy": {
        "server": "storage-server-1",
        "location": "C:\\...\\documents\\test.txt",
        "checksum": "a3f5c891d2e4f567b890c123d456e789",
        "status": "HEALTHY",
        "lastVerified": "2025-10-15T18:30:00Z"
    },
    "redundantCopies": [
        {
            "server": "storage-server-2",
            "location": "/mnt/storage2/replicas/documents/test.txt",
            "checksum": "a3f5c891d2e4f567b890c123d456e789",
            "status": "HEALTHY",
            "lastSyncTime": "2025-10-15T18:29:00Z"
        },
        {
            "server": "storage-server-3",
            "location": "/mnt/storage3/backups/documents/test.txt",
            "checksum": "a3f5c891d2e4f567b890c123d456e789",
            "status": "HEALTHY",
            "lastSyncTime": "2025-10-15T18:29:00Z"
        }
    ],
    "totalCopies": 3,
    "integrityStatus": "VERIFIED",
    "redundancyLevel": "HIGH"
}
```

---

## 🎯 **FLUJO COMPLETO DE PRUEBAS**

### **Escenario 1: Gestión Básica de Archivos**
1. **Login** → Obtener token
2. **Crear directorio** → `documents/personal`
3. **Subir archivo** → `documents/personal/test.txt`
4. **Descargar archivo** → Verificar contenido
5. **Renombrar** → `test-renamed.txt`
6. **Mover** → `backup/test-renamed.txt`
7. **Eliminar archivo**

### **Escenario 2: Compartir con Otros Usuarios**
1. **Login** → Usuario A
2. **Subir archivo** → `projects/informe.pdf`
3. **Compartir** → Con Usuario B (permiso READ)
4. **Login** → Usuario B
5. **Listar compartidos** → Ver `informe.pdf`

### **Escenario 3: Monitoreo del Sistema**
1. **Login**
2. **Reporte de redundancia** → Ver copias de archivo
3. **Estado de replicación** → Ver salud de BD
4. **Info del storage** → Ver estadísticas generales

---

## ✅ **RESUMEN DE ENDPOINTS**

| Método | Endpoint | Funcionalidad |
|--------|----------|---------------|
| POST | `/api/auth/login` | Autenticación |
| POST | `/api/storage/directories` | Crear directorio |
| POST | `/api/storage/files` | Subir archivo |
| GET | `/api/storage/files` | Descargar archivo |
| DELETE | `/api/storage/files` | Eliminar archivo |
| GET | `/api/storage/info` | Info del storage |
| POST | `/api/storage/move` | ✨ Mover archivo |
| POST | `/api/storage/rename` | ✨ Renombrar archivo |
| POST | `/api/storage/share` | ✨ Compartir archivo |
| GET | `/api/storage/shared` | ✨ Listar compartidos |
| GET | `/api/storage/replication/status` | ✨ Estado replicación BD |
| GET | `/api/storage/redundancy/report` | ✨ Reporte redundancia |

---

## 🚀 **INICIAR APLICACIÓN**

```bash
java -jar target/storagenode-1.0-SNAPSHOT.jar test-node
```

---

## 📝 **NOTAS IMPORTANTES**

- ✅ **Todas las funcionalidades requieren JWT**
- ✅ **Token expira en 24 horas**
- ✅ **Checksum usa SHA-256** para verificar integridad
- ✅ **Replicación automática** en múltiples servidores
- ✅ **Permisos granulares** para archivos compartidos
- ✅ **Monitoreo en tiempo real** del sistema

---

## 🎉 **¡SISTEMA COMPLETO IMPLEMENTADO!**

**Total de funcionalidades: 11**
- 5 Básicas
- 6 Avanzadas ✨

**¡Listo para producción!** 🚀