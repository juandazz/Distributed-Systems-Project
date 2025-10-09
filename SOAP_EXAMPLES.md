# Ejemplos de Peticiones SOAP para FileService

## 1. Descargar Archivo (Download File)

```xml
<?xml version="1.0" encoding="UTF-8"?>
<soap:Envelope xmlns:soap="http://schemas.xmlsoap.org/soap/envelope/" 
               xmlns:file="http://soap.appserver.example.com/">
   <soap:Header/>
   <soap:Body>
      <file:downloadFile>
         <file:username>testuser</file:username>
         <file:path>documents/test.txt</file:path>
      </file:downloadFile>
   </soap:Body>
</soap:Envelope>
```

### Respuesta esperada:
```xml
<?xml version="1.0" encoding="UTF-8"?>
<soap:Envelope xmlns:soap="http://schemas.xmlsoap.org/soap/envelope/">
   <soap:Body>
      <ns2:downloadFileResponse xmlns:ns2="http://soap.appserver.example.com/">
         <return>QXJjaGl2byBkZSBwcnVlYmEgZGlzdHJpYnVpZG9z</return>
      </ns2:downloadFileResponse>
   </soap:Body>
</soap:Envelope>
```

### Mejoras implementadas en downloadFile:
- ✅ **Lectura desde almacenamiento local primero** - Mayor velocidad y eficiencia
- ✅ **Fallback a almacenamiento remoto** - Redundancia y disponibilidad  
- ✅ **Normalización de rutas** - Maneja separadores "/" y "\"
- ✅ **Validación de seguridad** - Previene acceso a rutas peligrosas
- ✅ **Manejo robusto de errores** - Mensajes detallados y logging
- ✅ **Compatibilidad con estructura de directorios** - Funciona con paths anidados

## 2. Subir Archivo (Upload File)

```xml
<?xml version="1.0" encoding="UTF-8"?>
<soap:Envelope xmlns:soap="http://schemas.xmlsoap.org/soap/envelope/" 
               xmlns:file="http://soap.appserver.example.com/">
   <soap:Header/>
   <soap:Body>
      <file:uploadFile>
         <file:username>test-user</file:username>
         <file:path>/documents/nuevo_archivo.txt</file:path>
         <file:data>SGVsbG8gV29ybGQ=</file:data> <!-- "Hello World" en Base64 -->
      </file:uploadFile>
   </soap:Body>
</soap:Envelope>
```

## 3. Crear Directorio (Create Directory)

```xml
<?xml version="1.0" encoding="UTF-8"?>
<soap:Envelope xmlns:soap="http://schemas.xmlsoap.org/soap/envelope/" 
               xmlns:file="http://soap.appserver.example.com/">
   <soap:Header/>
   <soap:Body>
      <file:createDirectory>
         <file:username>test-user</file:username>
         <file:path>/documents/nuevo_directorio</file:path>
      </file:createDirectory>
   </soap:Body>
</soap:Envelope>
```

## 4. Eliminar Archivo (Delete File)

```xml
<?xml version="1.0" encoding="UTF-8"?>
<soap:Envelope xmlns:soap="http://schemas.xmlsoap.org/soap/envelope/" 
               xmlns:file="http://soap.appserver.example.com/">
   <soap:Header/>
   <soap:Body>
      <file:deleteFile>
         <file:username>test-user</file:username>
         <file:path>/documents/archivo_a_eliminar.txt</file:path>
      </file:deleteFile>
   </soap:Body>
</soap:Envelope>
```

## 5. Obtener Reporte de Almacenamiento (Get Storage Report)

```xml
<?xml version="1.0" encoding="UTF-8"?>
<soap:Envelope xmlns:soap="http://schemas.xmlsoap.org/soap/envelope/" 
               xmlns:file="http://soap.appserver.example.com/">
   <soap:Header/>
   <soap:Body>
      <file:getStorageReport>
         <file:username>test-user</file:username>
      </file:getStorageReport>
   </soap:Body>
</soap:Envelope>
```

## Configuración para Postman

### URL del Servicio SOAP:
```
http://localhost:8081/ws/FileService
```

### Headers necesarios:
- `Content-Type: text/xml; charset=utf-8`
- `SOAPAction: ""`

### Método HTTP:
- `POST`

## Notas Importantes:

1. **Base64 Encoding**: Los datos binarios (como archivos) deben estar codificados en Base64
2. **Namespace**: Asegúrate de usar el namespace correcto: `http://soap.appserver.example.com/`
3. **Username**: Reemplaza `test-user` con un usuario válido en tu sistema
4. **Paths**: Los paths deben ser rutas válidas en tu sistema de archivos virtual

## Ejemplo de uso con curl:

```bash
curl -X POST http://localhost:8081/ws/FileService \
  -H "Content-Type: text/xml; charset=utf-8" \
  -H "SOAPAction: \"\"" \
  -d @SOAP_DOWNLOAD_EXAMPLE.xml
```