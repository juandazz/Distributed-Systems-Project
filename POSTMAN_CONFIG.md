# CONFIGURACIONES PARA POSTMAN - STORAGE NODE

## Base URL
```
http://localhost:8080
```

## ENDPOINTS DISPONIBLES:

### 1. LOGIN DE USUARIO
**Método:** POST  
**URL:** `http://localhost:8080/api/auth/login`  
**Headers:**
```
Content-Type: application/json
```
**Body (raw JSON):**
```json
{
    "username": "daniel",
    "password": "daniel1234"
}
```
**Respuesta esperada:**
```json
{
    "status": "success",
    "message": "Login exitoso",
    "user": {
        "id": 2,
        "username": "daniel",
        "email": "juandanielvalencia090@gmail.com"
    }
}
```

### 2. REGISTRAR NUEVO USUARIO
**Método:** POST  
**URL:** `http://localhost:8080/api/auth/register`  
**Headers:**
```
Content-Type: application/json
```
**Body (raw JSON):**
```json
{
    "username": "nuevo_usuario",
    "email": "nuevo@example.com",
    "password": "password123"
}
```
**Respuesta esperada:**
```json
{
    "status": "success",
    "message": "Usuario registrado exitosamente",
    "user": {
        "id": 3,
        "username": "nuevo_usuario",
        "email": "nuevo@example.com"
    }
}
```

### 3. VERIFICAR SI USUARIO EXISTE
**Método:** GET  
**URL:** `http://localhost:8080/api/auth/check-user/{username}`  
**Ejemplo:** `http://localhost:8080/api/auth/check-user/daniel`  
**Respuesta esperada:**
```json
{
    "exists": true,
    "username": "daniel"
}
```

### 4. PROBAR CONEXIÓN A BASE DE DATOS
**Método:** GET  
**URL:** `http://localhost:8080/api/database/test`  
**Respuesta esperada:**
```json
{
    "userCount": 1,
    "nodeCount": 0,
    "message": "Conexión a la base de datos exitosa",
    "status": "success",
    "timestamp": "2025-10-06T03:34:08.888+00:00"
}
```

### 5. CREAR USUARIO DE PRUEBA
**Método:** POST  
**URL:** `http://localhost:8080/api/database/create-test-user`  

### 6. CREAR NODO DE PRUEBA
**Método:** POST  
**URL:** `http://localhost:8080/api/database/create-test-node`  

## OTROS SERVICIOS DISPONIBLES:

### SOAP Service
**URL:** `http://localhost:8090/fileService`  
**WSDL:** `http://localhost:8090/fileService?wsdl`

### RMI Services
**Puerto:** 1099  
**Servicios:** StorageNode, FileService

## INSTRUCCIONES PARA POSTMAN:

1. **Crear nueva Collection:** "Storage Node API"

2. **Configurar requests:**
   - En Headers siempre agregar: `Content-Type: application/json`
   - Para POST requests usar Body -> raw -> JSON

3. **Orden de pruebas recomendado:**
   1. Probar conexión DB: GET `/api/database/test`
   2. Verificar usuario existente: GET `/api/auth/check-user/daniel`
   3. Hacer login: POST `/api/auth/login`
   4. Registrar nuevo usuario: POST `/api/auth/register`
   5. Hacer login con nuevo usuario

4. **Variables de entorno (opcional):**
   - `base_url`: `http://localhost:8080`
   - `username`: `daniel`
   - `password`: `daniel1234`

## USUARIO PRE-EXISTENTE PARA PRUEBAS:
- **Username:** daniel
- **Password:** daniel1234
- **Email:** juandanielvalencia090@gmail.com

## NOTAS:
- El sistema debe estar ejecutándose con: `java -jar target/storagenode-1.0-SNAPSHOT.jar test-node`
- MySQL debe estar corriendo en puerto 3307
- Credenciales DB: user=root, password=root