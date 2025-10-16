# 🚀 **NUEVOS ENDPOINTS REST CON JWT - GUÍA POSTMAN**

## 🔥 **¡TODOS LOS ENDPOINTS FUNCIONAN CON JWT!**

Después de obtener tu token con **login**, usa estos endpoints REST (no XML):

---

## 📂 **1. CREAR DIRECTORIO**

### **POST** `http://localhost:8081/api/storage/directories`

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

**Respuesta esperada:**
```json
{
    "status": "success",
    "message": "Directorio creado exitosamente",
    "path": "C:\\Users\\juand\\Desktop\\storagenode\\storage\\documents\\personal",
    "user": "testuser"
}
```

---

## 📤 **2. SUBIR ARCHIVO**

### **POST** `http://localhost:8081/api/storage/files`

**Headers:**
```
Content-Type: application/json
Authorization: Bearer TU_TOKEN_JWT_AQUI
```

**Body (JSON):**
```json
{
    "path": "documents/test.txt",
    "content": "Hello World from Postman REST API!"
}
```

**Respuesta esperada:**
```json
{
    "status": "success",
    "message": "Archivo subido exitosamente",
    "path": "C:\\Users\\juand\\Desktop\\storagenode\\storage\\documents\\test.txt",
    "size": 35,
    "user": "testuser"
}
```

---

## 📥 **3. DESCARGAR ARCHIVO**

### **GET** `http://localhost:8081/api/storage/files?path=documents/test.txt`

**Headers:**
```
Authorization: Bearer TU_TOKEN_JWT_AQUI
```

**Respuesta esperada:**
```json
{
    "status": "success",
    "message": "Archivo descargado exitosamente",
    "path": "C:\\Users\\juand\\Desktop\\storagenode\\storage\\documents\\test.txt",
    "content": "Hello World from Postman REST API!",
    "size": 35,
    "user": "testuser"
}
```

---

## 🗑️ **4. ELIMINAR ARCHIVO**

### **DELETE** `http://localhost:8081/api/storage/files?path=documents/test.txt`

**Headers:**
```
Authorization: Bearer TU_TOKEN_JWT_AQUI
```

**Respuesta esperada:**
```json
{
    "status": "success",
    "message": "Archivo eliminado exitosamente",
    "path": "C:\\Users\\juand\\Desktop\\storagenode\\storage\\documents\\test.txt"
}
```

---

## ℹ️ **5. INFORMACIÓN DEL STORAGE**

### **GET** `http://localhost:8081/api/storage/info`

**Headers:**
```
Authorization: Bearer TU_TOKEN_JWT_AQUI
```

**Respuesta esperada:**
```json
{
    "status": "success",
    "basePath": "C:\\Users\\juand\\Desktop\\storagenode\\storage",
    "exists": true,
    "isDirectory": true,
    "filesCount": 2
}
```

---

## 🎯 **FLUJO COMPLETO EN POSTMAN:**

### **PASO A PASO:**

1. **🔑 LOGIN** → `POST /api/auth/login` → **Obtener TOKEN**

2. **📂 Crear directorio** → `POST /api/storage/directories`
   ```json
   { "path": "documents/personal" }
   ```

3. **📤 Subir archivo** → `POST /api/storage/files`
   ```json
   { 
       "path": "documents/personal/mi-archivo.txt",
       "content": "Contenido de mi archivo" 
   }
   ```

4. **📥 Descargar archivo** → `GET /api/storage/files?path=documents/personal/mi-archivo.txt`

5. **ℹ️ Ver info** → `GET /api/storage/info`

6. **🗑️ Eliminar archivo** → `DELETE /api/storage/files?path=documents/personal/mi-archivo.txt`

---

## ⚡ **VENTAJAS DE ESTOS ENDPOINTS:**

✅ **Solo JSON** - No más XML complicado  
✅ **Autenticación JWT** - Seguridad completa  
✅ **Respuestas claras** - Status y mensajes informativos  
✅ **Usuario identificado** - Cada operación sabe quién la hace  
✅ **Rutas simples** - Fácil de usar en Postman  

---

## 🔧 **CONFIGURACIÓN RÁPIDA EN POSTMAN:**

### **Para TODOS los endpoints (excepto login):**

1. **Headers** → Add:
   ```
   Authorization: Bearer TU_TOKEN_OBTENIDO_DEL_LOGIN
   Content-Type: application/json (solo para POST)
   ```

2. **Body** (solo para POST):
   - **raw** → **JSON**

3. **URL Parameters** (para GET y DELETE):
   - **Key**: `path`
   - **Value**: `documents/test.txt`

---

## 🎉 **¡LISTO PARA PROBAR!**

**Inicia tu aplicación:**
```bash
java -jar target/storagenode-1.0-SNAPSHOT.jar test-node
```

**Y prueba el flujo completo:**
1. Login → Token
2. Crear directorio
3. Subir archivo  
4. Descargar archivo
5. ¡Todo con JWT automático!

---

## 🚨 **NOTAS IMPORTANTES:**

- **Reemplaza `TU_TOKEN_JWT_AQUI`** con tu token real del login
- **El token expira en 24 horas**
- **Todos los paths son relativos** al directorio storage
- **Las respuestas incluyen el username** que hizo la operación