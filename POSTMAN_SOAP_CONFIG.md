# CONFIGURACIONES POSTMAN - SERVICIO SOAP STORAGE NODE

## INFORMACIÓN GENERAL
- **SOAP Service URL**: `http://localhost:8090/fileService`
- **WSDL URL**: `http://localhost:8090/fileService?wsdl`
- **Namespace**: `http://soap.appserver.example.com/`

## CONFIGURACIÓN POSTMAN PARA SOAP:

### 1. CREAR DIRECTORIO
**Método**: POST  
**URL**: `http://localhost:8090/fileService`  
**Headers**:
```
Content-Type: text/xml; charset=utf-8
SOAPAction: ""
```

**Body** (raw XML):
```xml
<?xml version="1.0" encoding="UTF-8"?>
<soap:Envelope xmlns:soap="http://schemas.xmlsoap.org/soap/envelope/" 
               xmlns:ns="http://soap.appserver.example.com/">
   <soap:Header/>
   <soap:Body>
      <ns:createDirectory>
         <username>daniel</username>
         <path>documents</path>
      </ns:createDirectory>
   </soap:Body>
</soap:Envelope>
```

**Respuesta esperada**:
```xml
<?xml version="1.0" ?>
<S:Envelope xmlns:S="http://schemas.xmlsoap.org/soap/envelope/">
    <S:Body>
        <ns2:createDirectoryResponse xmlns:ns2="http://soap.appserver.example.com/">
            <return>Directorio creado</return>
        </ns2:createDirectoryResponse>
    </S:Body>
</S:Envelope>
```

### 2. SUBIR ARCHIVO
**Método**: POST  
**URL**: `http://localhost:8090/fileService`  
**Headers**:
```
Content-Type: text/xml; charset=utf-8
SOAPAction: ""
```

**Body** (raw XML):
```xml
<?xml version="1.0" encoding="UTF-8"?>
<soap:Envelope xmlns:soap="http://schemas.xmlsoap.org/soap/envelope/" 
               xmlns:ns="http://soap.appserver.example.com/">
   <soap:Header/>
   <soap:Body>
      <ns:uploadFile>
         <username>daniel</username>
         <path>documents/test.txt</path>
         <data>SGVsbG8gV29ybGQ=</data>
      </ns:uploadFile>
   </soap:Body>
</soap:Envelope>
```

### 3. DESCARGAR ARCHIVO
**Método**: POST  
**URL**: `http://localhost:8090/fileService`  
**Headers**:
```
Content-Type: text/xml; charset=utf-8
SOAPAction: ""
```

**Body** (raw XML):
```xml
<?xml version="1.0" encoding="UTF-8"?>
<soap:Envelope xmlns:soap="http://schemas.xmlsoap.org/soap/envelope/" 
               xmlns:ns="http://soap.appserver.example.com/">
   <soap:Header/>
   <soap:Body>
      <ns:downloadFile>
         <username>daniel</username>
         <path>documents/test.txt</path>
      </ns:downloadFile>
   </soap:Body>
</soap:Envelope>
```

### 4. ELIMINAR ARCHIVO
**Método**: POST  
**URL**: `http://localhost:8090/fileService`  
**Headers**:
```
Content-Type: text/xml; charset=utf-8
SOAPAction: ""
```

**Body** (raw XML):
```xml
<?xml version="1.0" encoding="UTF-8"?>
<soap:Envelope xmlns:soap="http://schemas.xmlsoap.org/soap/envelope/" 
               xmlns:ns="http://soap.appserver.example.com/">
   <soap:Header/>
   <soap:Body>
      <ns:deleteFile>
         <username>daniel</username>
         <path>documents/test.txt</path>
      </ns:deleteFile>
   </soap:Body>
</soap:Envelope>
```

### 5. OBTENER REPORTE DE ALMACENAMIENTO
**Método**: POST  
**URL**: `http://localhost:8090/fileService`  
**Headers**:
```
Content-Type: text/xml; charset=utf-8
SOAPAction: ""
```

**Body** (raw XML):
```xml
<?xml version="1.0" encoding="UTF-8"?>
<soap:Envelope xmlns:soap="http://schemas.xmlsoap.org/soap/envelope/" 
               xmlns:ns="http://soap.appserver.example.com/">
   <soap:Header/>
   <soap:Body>
      <ns:getStorageReport>
         <username>daniel</username>
      </ns:getStorageReport>
   </soap:Body>
</soap:Envelope>
```

## ENDPOINTS REST API (ALTERNATIVA):

### 1. CREAR DIRECTORIO VIA REST
**Método**: POST  
**URL**: `http://localhost:8080/api/storage/directories?path=daniel/documents`

### 2. INFORMACIÓN DEL STORAGE
**Método**: GET  
**URL**: `http://localhost:8080/api/storage/info`

### 3. SUBIR ARCHIVO VIA REST
**Método**: POST  
**URL**: `http://localhost:8080/api/storage/files?path=daniel/documents/test.txt`  
**Body**: Archivo binario

### 4. DESCARGAR ARCHIVO VIA REST
**Método**: GET  
**URL**: `http://localhost:8080/api/storage/files?path=daniel/documents/test.txt`

## INSTRUCCIONES PASO A PASO PARA POSTMAN:

1. **Crear nueva request**
2. **Seleccionar método POST**
3. **URL**: `http://localhost:8090/fileService`
4. **En Headers agregar**:
   - `Content-Type: text/xml; charset=utf-8`
   - `SOAPAction: ""`
5. **En Body**:
   - Seleccionar `raw`
   - Cambiar tipo a `XML`
   - Copiar y pegar el XML del ejemplo
6. **Modificar username y path** según necesites
7. **Send**

## NOTAS:
- El campo `<data>` para uploadFile debe ser Base64
- Ejemplo "Hello World" en Base64 = `SGVsbG8gV29ybGQ=`
- Usar usuario existente: `daniel` (ya creado en BD)
- Las rutas pueden ser: `documents`, `images`, `files`, etc.

## TROUBLESHOOTING:
- Si sale "Error al crear directorio", verificar que el sistema esté corriendo
- Verificar que MySQL esté en puerto 3307
- Usar usuario autenticado en la BD