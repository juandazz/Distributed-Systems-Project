# Ejemplos SOAP con Autenticación JWT - Sistema de Almacenamiento

## 🔐 **IMPORTANTE: Primero debes autenticarte**

### 1. Login via SOAP (Obtener Token)

```xml
<?xml version="1.0" encoding="UTF-8"?>
<soap:Envelope xmlns:soap="http://schemas.xmlsoap.org/soap/envelope/" 
               xmlns:file="http://soap.appserver.example.com/">
   <soap:Header/>
   <soap:Body>
      <file:authenticateUser>
         <file:username>testuser</file:username>
         <file:password>testpass</file:password>
      </file:authenticateUser>
   </soap:Body>
</soap:Envelope>
```

**Respuesta esperada:**
```xml
<?xml version="1.0" encoding="UTF-8"?>
<soap:Envelope xmlns:soap="http://schemas.xmlsoap.org/soap/envelope/">
   <soap:Body>
      <ns2:authenticateUserResponse xmlns:ns2="http://soap.appserver.example.com/">
         <return>eyJhbGciOiJIUzI1NiJ9.eyJ1c2VySWQiOjEsInVzZXJuYW1lIjoidGVzdHVzZXIiLCJzdWIiOiJ0ZXN0dXNlciIsImlhdCI6MTY5NzU0MjgwMCwiZXhwIjoxNjk3NjI5MjAwfQ.TOKEN_JWT_AQUI</return>
      </ns2:authenticateUserResponse>
   </soap:Body>
</soap:Envelope>
```

---

## 📁 **Operaciones con Archivos (Requieren Token)**

### 2. Crear Directorio

```xml
<?xml version="1.0" encoding="UTF-8"?>
<soap:Envelope xmlns:soap="http://schemas.xmlsoap.org/soap/envelope/" 
               xmlns:file="http://soap.appserver.example.com/">
   <soap:Header/>
   <soap:Body>
      <file:createDirectory>
         <file:token>eyJhbGciOiJIUzI1NiJ9.eyJ1c2VySWQiOjEsInVzZXJuYW1lIjoidGVzdHVzZXIiLCJzdWIiOiJ0ZXN0dXNlciIsImlhdCI6MTY5NzU0MjgwMCwiZXhwIjoxNjk3NjI5MjAwfQ.TOKEN_JWT_AQUI</file:token>
         <file:path>documents/personal/projects</file:path>
      </file:createDirectory>
   </soap:Body>
</soap:Envelope>
```

### 3. Subir Archivo

```xml
<?xml version="1.0" encoding="UTF-8"?>
<soap:Envelope xmlns:soap="http://schemas.xmlsoap.org/soap/envelope/" 
               xmlns:file="http://soap.appserver.example.com/">
   <soap:Header/>
   <soap:Body>
      <file:uploadFile>
         <file:token>eyJhbGciOiJIUzI1NiJ9.eyJ1c2VySWQiOjEsInVzZXJuYW1lIjoidGVzdHVzZXIiLCJzdWIiOiJ0ZXN0dXNlciIsImlhdCI6MTY5NzU0MjgwMCwiZXhwIjoxNjk3NjI5MjAwfQ.TOKEN_JWT_AQUI</file:token>
         <file:path>documents/mi_archivo.txt</file:path>
         <file:data>SGVsbG8gV29ybGQgZnJvbSBKV1QgQXV0aGVudGljYXRpb24h</file:data>
      </file:uploadFile>
   </soap:Body>
</soap:Envelope>
```

### 4. Descargar Archivo

```xml
<?xml version="1.0" encoding="UTF-8"?>
<soap:Envelope xmlns:soap="http://schemas.xmlsoap.org/soap/envelope/" 
               xmlns:file="http://soap.appserver.example.com/">
   <soap:Header/>
   <soap:Body>
      <file:downloadFile>
         <file:token>eyJhbGciOiJIUzI1NiJ9.eyJ1c2VySWQiOjEsInVzZXJuYW1lIjoidGVzdHVzZXIiLCJzdWIiOiJ0ZXN0dXNlciIsImlhdCI6MTY5NzU0MjgwMCwiZXhwIjoxNjk3NjI5MjAwfQ.TOKEN_JWT_AQUI</file:token>
         <file:path>documents/test.txt</file:path>
      </file:downloadFile>
   </soap:Body>
</soap:Envelope>
```

### 5. Eliminar Archivo

```xml
<?xml version="1.0" encoding="UTF-8"?>
<soap:Envelope xmlns:soap="http://schemas.xmlsoap.org/soap/envelope/" 
               xmlns:file="http://soap.appserver.example.com/">
   <soap:Header/>
   <soap:Body>
      <file:deleteFile>
         <file:token>eyJhbGciOiJIUzI1NiJ9.eyJ1c2VySWQiOjEsInVzZXJuYW1lIjoidGVzdHVzZXIiLCJzdWIiOiJ0ZXN0dXNlciIsImlhdCI6MTY5NzU0MjgwMCwiZXhwIjoxNjk3NjI5MjAwfQ.TOKEN_JWT_AQUI</file:token>
         <file:path>documents/archivo_a_eliminar.txt</file:path>
      </file:deleteFile>
   </soap:Body>
</soap:Envelope>
```

### 6. Reporte de Almacenamiento

```xml
<?xml version="1.0" encoding="UTF-8"?>
<soap:Envelope xmlns:soap="http://schemas.xmlsoap.org/soap/envelope/" 
               xmlns:file="http://soap.appserver.example.com/">
   <soap:Header/>
   <soap:Body>
      <file:getStorageReport>
         <file:token>eyJhbGciOiJIUzI1NiJ9.eyJ1c2VySWQiOjEsInVzZXJuYW1lIjoidGVzdHVzZXIiLCJzdWIiOiJ0ZXN0dXNlciIsImlhdCI6MTY5NzU0MjgwMCwiZXhwIjoxNjk3NjI5MjAwfQ.TOKEN_JWT_AQUI</file:token>
      </file:getStorageReport>
   </soap:Body>
</soap:Envelope>
```

---

## 🔑 **Autenticación REST API**

### Login REST (Alternativa)
**URL**: `http://localhost:8081/api/auth/login`  
**Method**: `POST`  
**Content-Type**: `application/json`  

```json
{
    "username": "testuser",
    "password": "testpass"
}
```

**Respuesta:**
```json
{
    "status": "success",
    "message": "Login exitoso",
    "token": "eyJhbGciOiJIUzI1NiJ9.eyJ1c2VySWQiOjEsInVzZXJuYW1lIjoidGVzdHVzZXIiLCJzdWIiOiJ0ZXN0dXNlciIsImlhdCI6MTY5NzU0MjgwMCwiZXhwIjoxNjk3NjI5MjAwfQ.TOKEN_JWT_AQUI",
    "user": {
        "id": 1,
        "username": "testuser",
        "email": "test@example.com"
    }
}
```

### Validar Token REST
**URL**: `http://localhost:8081/api/auth/validate-token`  
**Method**: `POST`  
**Headers**: `Authorization: Bearer YOUR_JWT_TOKEN`

---

## ⚙️ **Configuración para Postman**

### URLs del Servicio:
- **SOAP Service**: `http://localhost:8081/ws/FileService`
- **SOAP Direct**: `http://localhost:8090/fileService`
- **REST API**: `http://localhost:8081/api/*`

### Headers para SOAP:
- `Content-Type: text/xml; charset=utf-8`
- `SOAPAction: ""`

### Método HTTP:
- `POST`

---

## 🛡️ **Seguridad Implementada**

### ✅ **Características de Seguridad:**

1. **Autenticación JWT** - Tokens seguros con expiración de 24 horas
2. **Validación de tokens** - Verificación automática en cada operación
3. **Aislamiento de usuarios** - Cada usuario solo accede a sus archivos
4. **Validación de rutas** - Prevención de path traversal attacks
5. **Hash de contraseñas** - SHA-256 para almacenamiento seguro

### 📋 **Flujo de Uso:**

1. **Registrar usuario** (REST API): `/api/auth/register`
2. **Hacer login** (SOAP o REST): obtener token JWT
3. **Usar token** en todas las operaciones SOAP de archivos
4. **Token expira** en 24 horas - requiere nuevo login

### ⚠️ **Notas Importantes:**

- **El token JWT debe ser real** - Reemplaza `TOKEN_JWT_AQUI` con el token obtenido del login
- **Tokens tienen expiración** - 24 horas desde la creación
- **Username es extraído del token** - No necesitas pasarlo por separado
- **Todas las operaciones requieren token válido** excepto `authenticateUser`

---

## 🚀 **¡Tu sistema de almacenamiento con autenticación JWT está listo!**