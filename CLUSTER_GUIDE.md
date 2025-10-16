# 🌐 **GUÍA DEL SISTEMA DE CLUSTER CON BALANCEADOR DE CARGA**

## 📋 **DESCRIPCIÓN**

Este sistema distribuye automáticamente archivos entre múltiples nodos de almacenamiento usando un balanceador de carga inteligente.

---

## ⚙️ **CONFIGURACIÓN DE NODOS**

### **Archivo: `nodes.properties`**

```properties
# ==================== NODOS DE ALMACENAMIENTO ====================

# Nodos Locales (para pruebas)
node1=http://localhost:8081
node2=http://localhost:8082
node3=http://localhost:8083

# Nodos Remotos (producción)
node4=http://192.168.1.100:8081
node5=http://192.168.1.101:8081
node6=http://10.0.0.50:8081

# ==================== CONFIGURACIÓN DEL BALANCEADOR ====================

# Estrategias disponibles:
# - ROUND_ROBIN: Distribuye secuencialmente entre nodos
# - LEAST_LOADED: Selecciona el nodo con menor carga
# - RANDOM: Selecciona nodo aleatorio
loadbalancer.strategy=ROUND_ROBIN

# ==================== CONFIGURACIÓN DE REDUNDANCIA ====================

# Número de copias de cada archivo (en diferentes nodos)
replication.factor=3

# ==================== HEALTH CHECK ====================

# Intervalo en milisegundos para verificar salud de nodos
healthcheck.interval=30000
```

---

## 🚀 **CÓMO AGREGAR NODOS**

### **1. Para Nodos Locales (Mismo PC):**
```properties
node1=http://localhost:8081
node2=http://localhost:8082
node3=http://localhost:8083
```

### **2. Para Nodos en Red Local:**
```properties
node_oficina1=http://192.168.1.100:8081
node_oficina2=http://192.168.1.101:8081
node_servidor=http://192.168.1.200:8081
```

### **3. Para Nodos en Internet (IPs Públicas):**
```properties
node_datacenter1=http://203.45.67.89:8081
node_datacenter2=http://203.45.67.90:8081
node_cloud=http://45.123.45.67:8081
```

### **4. Con Nombres Descriptivos:**
```properties
node_ventas_bogota=http://10.1.1.50:8081
node_contabilidad_medellin=http://10.2.1.50:8081
node_backup_cali=http://10.3.1.50:8081
```

---

## 📊 **ENDPOINTS DEL CLUSTER**

### **1. Ver Estadísticas del Cluster**
**GET** `http://localhost:8081/api/cluster/stats`

**Headers:**
```
Authorization: Bearer TU_TOKEN
```

**Respuesta:**
```json
{
    "status": "success",
    "message": "Estadísticas del cluster obtenidas exitosamente",
    "user": "testuser",
    "totalNodes": 6,
    "onlineNodes": 5,
    "offlineNodes": 1,
    "totalLoad": 12,
    "strategy": "ROUND_ROBIN",
    "replicationFactor": 3,
    "nodes": [
        {
            "name": "node1",
            "url": "http://localhost:8081",
            "status": "ONLINE",
            "currentLoad": 3,
            "freeSpace": 1000000000
        },
        {
            "name": "node2",
            "url": "http://localhost:8082",
            "status": "ONLINE",
            "currentLoad": 2,
            "freeSpace": 1500000000
        }
    ]
}
```

---

### **2. Listar Todos los Nodos**
**GET** `http://localhost:8081/api/cluster/nodes`

**Headers:**
```
Authorization: Bearer TU_TOKEN
```

**Respuesta:**
```json
{
    "status": "success",
    "message": "Nodos obtenidos exitosamente",
    "user": "testuser",
    "totalNodes": 6,
    "availableNodes": 5,
    "nodes": [...]
}
```

---

### **3. Ver Información de Un Nodo Específico**
**GET** `http://localhost:8081/api/cluster/nodes/node1`

**Headers:**
```
Authorization: Bearer TU_TOKEN
```

---

### **4. Obtener Próximo Nodo (según balanceador)**
**GET** `http://localhost:8081/api/cluster/next-node`

**Headers:**
```
Authorization: Bearer TU_TOKEN
```

**Respuesta:**
```json
{
    "status": "success",
    "message": "Nodo asignado exitosamente",
    "user": "testuser",
    "node": {
        "name": "node2",
        "url": "http://localhost:8082",
        "status": "ONLINE",
        "currentLoad": 1
    }
}
```

---

### **5. Recargar Configuración de Nodos**
**POST** `http://localhost:8081/api/cluster/reload`

**Headers:**
```
Authorization: Bearer TU_TOKEN
```

**Descripción:** Recarga `nodes.properties` sin reiniciar la aplicación.

---

## 🔄 **ESTRATEGIAS DE BALANCEO**

### **1. ROUND_ROBIN (Recomendado)**
- Distribuye archivos secuencialmente
- Balanceo equitativo entre nodos
- Ideal para cargas uniformes

```properties
loadbalancer.strategy=ROUND_ROBIN
```

### **2. LEAST_LOADED (Para Cargas Variables)**
- Selecciona el nodo con menor carga activa
- Optimiza para respuesta rápida
- Ideal cuando los nodos tienen diferentes capacidades

```properties
loadbalancer.strategy=LEAST_LOADED
```

### **3. RANDOM**
- Selección aleatoria
- Distribución estadísticamente uniforme
- Más simple, menos control

```properties
loadbalancer.strategy=RANDOM
```

---

## 📁 **CÓMO FUNCIONA LA DISTRIBUCIÓN**

### **Escenario: Usuario sube 6 archivos con 3 nodos**

#### **Con ROUND_ROBIN:**
```
Archivo 1 → Node 1
Archivo 2 → Node 2
Archivo 3 → Node 3
Archivo 4 → Node 1
Archivo 5 → Node 2
Archivo 6 → Node 3
```

#### **Con LEAST_LOADED:**
```
Archivo 1 → Node 1 (carga: 0)
Archivo 2 → Node 2 (carga: 0)
Archivo 3 → Node 3 (carga: 0)
Archivo 4 → Node 1 (carga: 1) ← El menos cargado
Archivo 5 → Node 2 (carga: 1)
Archivo 6 → Node 3 (carga: 1)
```

---

## 🔒 **REPLICACIÓN Y REDUNDANCIA**

### **Factor de Replicación = 3**
Cada archivo se guarda en 3 nodos diferentes:

```
documento.pdf:
  ✅ Copia 1 → Node 1 (Principal)
  ✅ Copia 2 → Node 3 (Réplica)
  ✅ Copia 3 → Node 5 (Backup)
```

**Ventajas:**
- ✅ Alta disponibilidad
- ✅ Tolerancia a fallos
- ✅ Si un nodo falla, el archivo sigue accesible

---

## 💓 **HEALTH CHECK AUTOMÁTICO**

El sistema verifica automáticamente cada 30 segundos:

```
✅ Node 1: ONLINE  (última verificación: 10 seg)
✅ Node 2: ONLINE  (última verificación: 10 seg)
❌ Node 3: OFFLINE (última verificación: 45 seg)
✅ Node 4: ONLINE  (última verificación: 15 seg)
```

**Acción automática:**
- Nodos OFFLINE son excluidos del balanceo
- Cuando vuelven ONLINE, se reintegran automáticamente

---

## 🛠️ **CONFIGURACIÓN PARA DIFERENTES ESCENARIOS**

### **Escenario 1: Servidor Local + 2 PCs en Red**
```properties
node_principal=http://localhost:8081
node_pc_oficina1=http://192.168.1.100:8081
node_pc_oficina2=http://192.168.1.101:8081

loadbalancer.strategy=ROUND_ROBIN
replication.factor=3
```

### **Escenario 2: 1 Servidor Principal + 3 Backups Remotos**
```properties
node_principal=http://localhost:8081
node_backup_ciudad1=http://45.123.45.67:8081
node_backup_ciudad2=http://45.123.45.68:8081
node_backup_ciudad3=http://45.123.45.69:8081

loadbalancer.strategy=ROUND_ROBIN
replication.factor=4
```

### **Escenario 3: Cluster de Alta Disponibilidad**
```properties
node1=http://10.0.1.10:8081
node2=http://10.0.1.11:8081
node3=http://10.0.1.12:8081
node4=http://10.0.1.13:8081
node5=http://10.0.1.14:8081
node6=http://10.0.1.15:8081

loadbalancer.strategy=LEAST_LOADED
replication.factor=3
healthcheck.interval=15000
```

---

## 🔧 **PASOS PARA IMPLEMENTAR**

### **1. Editar `nodes.properties`**
```bash
# Ubicación: C:\Users\juand\Desktop\storagenode\nodes.properties
notepad nodes.properties
```

### **2. Agregar tus nodos**
```properties
node1=http://localhost:8081
node2=http://192.168.1.100:8081
node3=http://192.168.1.101:8081
```

### **3. Reiniciar aplicación**
```bash
java -jar target/storagenode-1.0-SNAPSHOT.jar test-node
```

### **4. Verificar cluster**
```
GET http://localhost:8081/api/cluster/stats
```

---

## 📊 **MONITOREO EN TIEMPO REAL**

### **Dashboard de Cluster:**
```json
{
    "totalNodes": 6,
    "onlineNodes": 5,
    "offlineNodes": 1,
    "totalLoad": 23,
    "strategy": "ROUND_ROBIN",
    "nodes": [
        {
            "name": "node1",
            "status": "ONLINE",
            "currentLoad": 5,
            "url": "http://localhost:8081"
        }
    ]
}
```

---

## 🎯 **FLUJO COMPLETO DE USO**

1. **Configurar nodos** en `nodes.properties`
2. **Iniciar aplicación**
3. **Login** para obtener token
4. **Ver cluster:** `GET /api/cluster/stats`
5. **Subir archivo:** Se distribuye automáticamente
6. **Sistema replica** en múltiples nodos
7. **Monitorear** salud de nodos

---

## ✅ **VENTAJAS DEL SISTEMA**

- ✅ **Escalabilidad**: Agrega nodos sin recompilar
- ✅ **Alta Disponibilidad**: Replicación automática
- ✅ **Balanceo Inteligente**: 3 estrategias disponibles
- ✅ **Monitoreo Automático**: Health checks periódicos
- ✅ **Fácil Configuración**: Solo edita `nodes.properties`
- ✅ **Recarga en Caliente**: Sin reiniciar aplicación

---

**¡Tu sistema distribuido está listo para producción!** 🚀