# 🚀 Guía Completa de Postman - Sistema de Almacenamiento JWT

## 📍 **URLs del Sistema (Puerto 8081)**
- **Base URL REST**: `http://localhost:8081/api/`
- **Base URL SOAP**: `http://localhost:8081/ws/FileService`

---

## 🔐 **PASO 1: REGISTRAR USUARIO**

### **POST** `http://localhost:8081/api/auth/register`

**Headers:**
```
Content-Type: application/json
```

**Body (JSON):**
```json
{
    "username": "testuser",
    "email": "test@example.com",
    "password": "testpass"
}
```

**Respuesta esperada:**
```json
{
    "status": "success",
    "message": "Usuario registrado exitosamente",
    "user": {
        "id": 1,
        "username": "testuser",
        "email": "test@example.com"
    }
}
```

---

## 🔑 **PASO 2: LOGIN Y OBTENER TOKEN JWT**

### **POST** `http://localhost:8081/api/auth/login`

**Headers:**
```
Content-Type: application/json
```

**Body (JSON):**
```json
{
    "username": "testuser",
    "password": "testpass"
}
```

**Respuesta esperada:**
```json
{
    "status": "success",
    "message": "Login exitoso",
    "token": "eyJhbGciOiJIUzI1NiJ9.eyJ1c2VySWQiOjEsInVzZXJuYW1lIjoidGVzdHVzZXIiLCJzdWIiOiJ0ZXN0dXNlciIsImlhdCI6MTY5NzU0MjgwMCwiZXhwIjoxNjk3NjI5MjAwfQ.TOKEN_COMPLETO_AQUI",
    "user": {
        "id": 1,
        "username": "testuser",
        "email": "test@example.com"
    }
}
```

**🔥 ¡IMPORTANTE!** Copia el **token** de la respuesta, lo necesitarás para todos los siguientes pasos.

---

## 🛡️ **PASO 3: VALIDAR TOKEN (Opcional)**

### **POST** `http://localhost:8081/api/auth/validate-token`

**Headers:**
```
Authorization: Bearer TU_TOKEN_JWT_AQUI
```

**Respuesta esperada:**
```json
{
    "status": "success",
    "message": "Token válido",
    "user": {
        "id": 1,
        "username": "testuser"
    }
}
```

---

## 📁 **OPERACIONES CON ARCHIVOS USANDO TOKEN**

### 📂 **CREAR DIRECTORIO**

#### **Opción A: REST API (Necesita ser actualizada)**
**POST** `http://localhost:8081/api/files/create-directory`

**Headers:**
```
Content-Type: application/json
Authorization: Bearer TU_TOKEN_JWT_AQUI
```

**Body (JSON):**
```json
{
    "path": "documents/personal"
}
```

#### **Opción B: SOAP (YA FUNCIONA)**
**POST** `http://localhost:8081/ws/FileService`

**Headers:**
```
Content-Type: text/xml; charset=utf-8
SOAPAction: ""
```

**Body (XML):**
```xml
<?xml version="1.0" encoding="UTF-8"?>
<soap:Envelope xmlns:soap="http://schemas.xmlsoap.org/soap/envelope/" 
               xmlns:file="http://soap.appserver.example.com/">
   <soap:Header/>
   <soap:Body>
      <file:createDirectory>
         <file:token>TU_TOKEN_JWT_AQUI</file:token>
         <file:path>documents/personal</file:path>
      </file:createDirectory>
   </soap:Body>
</soap:Envelope>
```

---

### 📤 **SUBIR ARCHIVO**

#### **Opción A: SOAP (Recomendado)**
**POST** `http://localhost:8081/ws/FileService`

**Headers:**
```
Content-Type: text/xml; charset=utf-8
SOAPAction: ""
```

**Body (XML):**
```xml
<?xml version="1.0" encoding="UTF-8"?>
<soap:Envelope xmlns:soap="http://schemas.xmlsoap.org/soap/envelope/" 
               xmlns:file="http://soap.appserver.example.com/">
   <soap:Header/>
   <soap:Body>
      <file:uploadFile>
         <file:token>TU_TOKEN_JWT_AQUI</file:token>
         <file:path>documents/test.txt</file:path>
         <file:data>SGVsbG8gV29ybGQgZnJvbSBQb3N0bWFuIQ==</file:data>
      </file:uploadFile>
   </soap:Body>
</soap:Envelope>
```

**💡 Nota:** `SGVsbG8gV29ybGQgZnJvbSBQb3N0bWFuIQ==` es "Hello World from Postman!" en Base64.

---

### 📥 **DESCARGAR ARCHIVO**

#### **SOAP (Funciona con JWT)**
**POST** `http://localhost:8081/ws/FileService`

**Headers:**
```
Content-Type: text/xml; charset=utf-8
SOAPAction: ""
```

**Body (XML):**
```xml
<?xml version="1.0" encoding="UTF-8"?>
<soap:Envelope xmlns:soap="http://schemas.xmlsoap.org/soap/envelope/" 
               xmlns:file="http://soap.appserver.example.com/">
   <soap:Header/>
   <soap:Body>
      <file:downloadFile>
         <file:token>TU_TOKEN_JWT_AQUI</file:token>
         <file:path>documents/test.txt</file:path>
      </file:downloadFile>
   </soap:Body>
</soap:Envelope>
```

---

## 🔧 **CONFIGURACIÓN EN POSTMAN**

### **Para requests REST:**
1. **Method**: POST/GET según corresponda
2. **Headers** → Add:
   - `Content-Type: application/json`
   - `Authorization: Bearer TU_TOKEN_AQUI`
3. **Body** → raw → JSON

### **Para requests SOAP:**
1. **Method**: POST
2. **Headers** → Add:
   - `Content-Type: text/xml; charset=utf-8`
   - `SOAPAction: ""`
3. **Body** → raw → XML

---

## 🎯 **FLUJO COMPLETO EN POSTMAN:**

1. **📝 Registro**: `POST /api/auth/register`
2. **🔑 Login**: `POST /api/auth/login` → Obtener TOKEN
3. **📂 Crear directorio**: SOAP `createDirectory` con token
4. **📤 Subir archivo**: SOAP `uploadFile` con token  
5. **📥 Descargar archivo**: SOAP `downloadFile` con token

---

## ⚠️ **NOTAS IMPORTANTES:**

- **El token expira en 24 horas** - necesitarás hacer login nuevamente
- **Reemplaza `TU_TOKEN_JWT_AQUI`** con el token real obtenido del login
- **SOAP funciona completamente** con autenticación JWT
- **REST APIs** pueden necesitar actualizarse para usar JWT (te ayudo si quieres)

---

## 🚀 **¡TU SISTEMA ESTÁ LISTO!**

**Endpoints funcionando:**
✅ Registro de usuarios  
✅ Login con JWT  
✅ Validación de tokens  
✅ SOAP con autenticación completa  
✅ Base de datos MySQL  
✅ Almacenamiento de archivos  

**¡Prueba el flujo completo en Postman!** 🎉