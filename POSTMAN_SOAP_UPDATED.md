# CONFIGURACIÓN POSTMAN - SOAP CON @WebParam

## ✅ **SISTEMA ACTUALIZADO CON @WebParam**

Ahora todos los métodos SOAP tienen parámetros nombrados correctamente.

---

## 🧼 **SOAP SERVICES - CONFIGURACIÓN POSTMAN**

### **BASE CONFIGURATION**
- **URL**: `http://localhost:8090/fileService`
- **Method**: POST
- **Headers**:
  ```
  Content-Type: text/xml; charset=utf-8
  SOAPAction: ""
  ```

---

### **1. CREAR DIRECTORIO**
**Body** (raw XML):
```xml
<?xml version="1.0" encoding="UTF-8"?>
<soap:Envelope xmlns:soap="http://schemas.xmlsoap.org/soap/envelope/" 
               xmlns:ns="http://soap.appserver.example.com/">
   <soap:Header/>
   <soap:Body>
      <ns:createDirectory>
         <ns:username>daniel</ns:username>
         <ns:path>documents</ns:path>
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

---

### **2. SUBIR ARCHIVO**
**Body** (raw XML):
```xml
<?xml version="1.0" encoding="UTF-8"?>
<soap:Envelope xmlns:soap="http://schemas.xmlsoap.org/soap/envelope/" 
               xmlns:ns="http://soap.appserver.example.com/">
   <soap:Header/>
   <soap:Body>
      <ns:uploadFile>
         <ns:username>daniel</ns:username>
         <ns:path>documents/test.txt</ns:path>
         <ns:data>SGVsbG8gV29ybGQ=</ns:data>
      </ns:uploadFile>
   </soap:Body>
</soap:Envelope>
```

**Nota**: `SGVsbG8gV29ybGQ=` es "Hello World" en Base64

---

### **3. DESCARGAR ARCHIVO**
**Body** (raw XML):
```xml
<?xml version="1.0" encoding="UTF-8"?>
<soap:Envelope xmlns:soap="http://schemas.xmlsoap.org/soap/envelope/" 
               xmlns:ns="http://soap.appserver.example.com/">
   <soap:Header/>
   <soap:Body>
      <ns:downloadFile>
         <ns:username>daniel</ns:username>
         <ns:path>documents/test.txt</ns:path>
      </ns:downloadFile>
   </soap:Body>
</soap:Envelope>
```

---

### **4. ELIMINAR ARCHIVO**
**Body** (raw XML):
```xml
<?xml version="1.0" encoding="UTF-8"?>
<soap:Envelope xmlns:soap="http://schemas.xmlsoap.org/soap/envelope/" 
               xmlns:ns="http://soap.appserver.example.com/">
   <soap:Header/>
   <soap:Body>
      <ns:deleteFile>
         <ns:username>daniel</ns:username>
         <ns:path>documents/test.txt</ns:path>
      </ns:deleteFile>
   </soap:Body>
</soap:Envelope>
```

---

### **5. REPORTE DE ALMACENAMIENTO**
**Body** (raw XML):
```xml
<?xml version="1.0" encoding="UTF-8"?>
<soap:Envelope xmlns:soap="http://schemas.xmlsoap.org/soap/envelope/" 
               xmlns:ns="http://soap.appserver.example.com/">
   <soap:Header/>
   <soap:Body>
      <ns:getStorageReport>
         <ns:username>daniel</ns:username>
      </ns:getStorageReport>
   </soap:Body>
</soap:Envelope>
```

---

## 🔧 **PRUEBAS PASO A PASO**

### **ORDEN RECOMENDADO:**

1. **Verificar WSDL**: `GET http://localhost:8090/fileService?wsdl`
2. **Crear directorio**: Usar XML del ejemplo 1
3. **Subir archivo**: Usar XML del ejemplo 2  
4. **Descargar archivo**: Usar XML del ejemplo 3
5. **Obtener reporte**: Usar XML del ejemplo 5
6. **Eliminar archivo**: Usar XML del ejemplo 4

---

## 🛠️ **CONFIGURACIÓN EN POSTMAN**

1. **Crear nueva request**
2. **Método**: POST
3. **URL**: `http://localhost:8090/fileService`
4. **Headers**:
   - `Content-Type: text/xml; charset=utf-8`
   - `SOAPAction: ""`
5. **Body**: 
   - Seleccionar `raw`
   - Tipo: `XML`
   - Pegar el XML del método deseado

---

## 📝 **NOTAS IMPORTANTES**

- ✅ **Parámetros nombrados**: Ahora con `@WebParam`
- ✅ **Validación mejorada**: NullPointerException solucionado  
- ✅ **URLs corregidas**: Todas apuntan a `/api/storage/`
- 🔐 **Usuario**: Usar `daniel` (ya existe en BD)
- 📁 **Paths**: `documents`, `images`, `files/subfolder`
- 🔢 **Data encoding**: Base64 para archivos binarios

---

## 🚀 **COMANDO PARA EJECUTAR**
```bash
java -jar target/storagenode-1.0-SNAPSHOT.jar test-node
```

**¡Ahora el servicio SOAP está completamente funcional con parámetros nombrados!** 🎉