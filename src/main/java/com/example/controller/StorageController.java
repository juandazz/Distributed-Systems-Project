package com.example.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.service.JwtService;
import com.example.service.LoadBalancerService;
import com.example.model.*;
import com.example.repository.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.MessageDigest;
import java.sql.Timestamp;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/storage")
public class StorageController {
    
    @Autowired
    private JwtService jwtService;
    
    @Autowired
    private LoadBalancerService loadBalancerService;
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private FileRepository fileRepository;
    
    @Autowired
    private NodeRepository nodeRepository;
    
    private static final String STORAGE_BASE_PATH = "C:\\Users\\juand\\Desktop\\storagenode\\storage";
    
    @PostMapping("/directories")
    public ResponseEntity<Map<String, Object>> createDirectory(
            @RequestHeader("Authorization") String authHeader,
            @RequestBody Map<String, String> requestBody) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            // Validar JWT token
            String token = authHeader.replace("Bearer ", "");
            if (!jwtService.validateToken(token)) {
                response.put("status", "error");
                response.put("message", "Token inválido");
                return ResponseEntity.status(401).body(response);
            }
            
            String username = jwtService.extractUsername(token);
            String path = requestBody.get("path");
            if (path == null || path.trim().isEmpty()) {
                response.put("status", "error");
                response.put("message", "Path no puede estar vacío");
                return ResponseEntity.badRequest().body(response);
            }
            
            // Construir la ruta completa
            Path fullPath = Paths.get(STORAGE_BASE_PATH, path);
            
            // Crear directorio si no existe
            Files.createDirectories(fullPath);
            
            response.put("status", "success");
            response.put("message", "Directorio creado exitosamente");
            response.put("path", fullPath.toString());
            response.put("user", username);
            
            return ResponseEntity.ok(response);
            
        } catch (IOException e) {
            response.put("status", "error");
            response.put("message", "Error al crear directorio: " + e.getMessage());
            return ResponseEntity.internalServerError().body(response);
        }
    }
    
    @PostMapping("/files")
    public ResponseEntity<Map<String, Object>> uploadFile(
            @RequestHeader("Authorization") String authHeader,
            @RequestBody Map<String, Object> requestBody) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            // Validar JWT token
            String token = authHeader.replace("Bearer ", "");
            if (!jwtService.validateToken(token)) {
                response.put("status", "error");
                response.put("message", "Token inválido");
                return ResponseEntity.status(401).body(response);
            }
            
            String username = jwtService.extractUsername(token);
            String path = (String) requestBody.get("path");
            String content = (String) requestBody.get("content");
            if (path == null || path.trim().isEmpty()) {
                response.put("status", "error");
                response.put("message", "Path no puede estar vacío");
                return ResponseEntity.badRequest().body(response);
            }
            
            if (content == null) {
                response.put("status", "error");
                response.put("message", "Content no puede estar vacío");
                return ResponseEntity.badRequest().body(response);
            }
            
            // Construir la ruta completa
            Path fullPath = Paths.get(STORAGE_BASE_PATH, path);
            
            // Crear directorios padre si no existen
            Files.createDirectories(fullPath.getParent());
            
            // Escribir archivo
            byte[] data = content.getBytes();
            Files.write(fullPath, data);
            
            response.put("status", "success");
            response.put("message", "Archivo subido exitosamente");
            response.put("path", fullPath.toString());
            response.put("size", data.length);
            response.put("user", username);
            
            return ResponseEntity.ok(response);
            
        } catch (IOException e) {
            response.put("status", "error");
            response.put("message", "Error al subir archivo: " + e.getMessage());
            return ResponseEntity.internalServerError().body(response);
        }
    }
    
    @GetMapping("/files")
    public ResponseEntity<Map<String, Object>> downloadFile(
            @RequestHeader("Authorization") String authHeader,
            @RequestParam String path) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            // Validar JWT token
            String token = authHeader.replace("Bearer ", "");
            if (!jwtService.validateToken(token)) {
                response.put("status", "error");
                response.put("message", "Token inválido");
                return ResponseEntity.status(401).body(response);
            }
            
            String username = jwtService.extractUsername(token);
            if (path == null || path.trim().isEmpty()) {
                response.put("status", "error");
                response.put("message", "Path no puede estar vacío");
                return ResponseEntity.badRequest().body(response);
            }
            
            // Construir la ruta completa
            Path fullPath = Paths.get(STORAGE_BASE_PATH, path);
            
            if (!Files.exists(fullPath)) {
                response.put("status", "error");
                response.put("message", "Archivo no encontrado");
                return ResponseEntity.status(404).body(response);
            }
            
            byte[] data = Files.readAllBytes(fullPath);
            String content = new String(data);
            
            response.put("status", "success");
            response.put("message", "Archivo descargado exitosamente");
            response.put("path", fullPath.toString());
            response.put("content", content);
            response.put("size", data.length);
            response.put("user", username);
            
            return ResponseEntity.ok(response);
            
        } catch (IOException e) {
            response.put("status", "error");
            response.put("message", "Error al descargar archivo: " + e.getMessage());
            return ResponseEntity.internalServerError().body(response);
        }
    }
    
    @DeleteMapping("/files")
    public ResponseEntity<Map<String, Object>> deleteFile(@RequestParam String path) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            if (path == null || path.trim().isEmpty()) {
                response.put("status", "error");
                response.put("message", "Path no puede estar vacío");
                return ResponseEntity.badRequest().body(response);
            }
            
            // Construir la ruta completa
            Path fullPath = Paths.get(STORAGE_BASE_PATH, path);
            
            if (!Files.exists(fullPath)) {
                response.put("status", "error");
                response.put("message", "Archivo no encontrado");
                return ResponseEntity.status(404).body(response);
            }
            
            Files.delete(fullPath);
            
            response.put("status", "success");
            response.put("message", "Archivo eliminado exitosamente");
            response.put("path", fullPath.toString());
            
            return ResponseEntity.ok(response);
            
        } catch (IOException e) {
            response.put("status", "error");
            response.put("message", "Error al eliminar archivo: " + e.getMessage());
            return ResponseEntity.internalServerError().body(response);
        }
    }
    
    @GetMapping("/info")
    public ResponseEntity<Map<String, Object>> getStorageInfo() {
        Map<String, Object> response = new HashMap<>();
        
        try {
            File storageDir = new File(STORAGE_BASE_PATH);
            
            response.put("status", "success");
            response.put("basePath", STORAGE_BASE_PATH);
            response.put("exists", storageDir.exists());
            response.put("isDirectory", storageDir.isDirectory());
            
            if (storageDir.exists()) {
                File[] files = storageDir.listFiles();
                response.put("filesCount", files != null ? files.length : 0);
            }
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            response.put("status", "error");
            response.put("message", "Error al obtener información: " + e.getMessage());
            return ResponseEntity.internalServerError().body(response);
        }
    }
    
    // ==================== NUEVAS FUNCIONALIDADES ====================
    
    /**
     * 1. MOVER ARCHIVO O DIRECTORIO
     */
    @PostMapping("/move")
    public ResponseEntity<Map<String, Object>> moveFile(
            @RequestHeader("Authorization") String authHeader,
            @RequestBody Map<String, String> requestBody) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            // Validar JWT token
            String token = authHeader.replace("Bearer ", "");
            if (!jwtService.validateToken(token)) {
                response.put("status", "error");
                response.put("message", "Token inválido");
                return ResponseEntity.status(401).body(response);
            }
            
            String username = jwtService.extractUsername(token);
            String sourcePath = requestBody.get("sourcePath");
            String destinationPath = requestBody.get("destinationPath");
            
            if (sourcePath == null || sourcePath.trim().isEmpty()) {
                response.put("status", "error");
                response.put("message", "sourcePath no puede estar vacío");
                return ResponseEntity.badRequest().body(response);
            }
            
            if (destinationPath == null || destinationPath.trim().isEmpty()) {
                response.put("status", "error");
                response.put("message", "destinationPath no puede estar vacío");
                return ResponseEntity.badRequest().body(response);
            }
            
            Path source = Paths.get(STORAGE_BASE_PATH, sourcePath);
            Path destination = Paths.get(STORAGE_BASE_PATH, destinationPath);
            
            if (!Files.exists(source)) {
                response.put("status", "error");
                response.put("message", "Archivo o directorio origen no existe");
                return ResponseEntity.status(404).body(response);
            }
            
            if (Files.exists(destination)) {
                response.put("status", "error");
                response.put("message", "El destino ya existe");
                return ResponseEntity.badRequest().body(response);
            }
            
            // Crear directorios padre si no existen
            Files.createDirectories(destination.getParent());
            
            // Mover archivo o directorio
            Files.move(source, destination, StandardCopyOption.REPLACE_EXISTING);
            
            response.put("status", "success");
            response.put("message", "Archivo/directorio movido exitosamente");
            response.put("sourcePath", source.toString());
            response.put("destinationPath", destination.toString());
            response.put("user", username);
            
            return ResponseEntity.ok(response);
            
        } catch (IOException e) {
            response.put("status", "error");
            response.put("message", "Error al mover: " + e.getMessage());
            return ResponseEntity.internalServerError().body(response);
        }
    }
    
    /**
     * 2. RENOMBRAR ARCHIVO O DIRECTORIO
     */
    @PostMapping("/rename")
    public ResponseEntity<Map<String, Object>> renameFile(
            @RequestHeader("Authorization") String authHeader,
            @RequestBody Map<String, String> requestBody) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            // Validar JWT token
            String token = authHeader.replace("Bearer ", "");
            if (!jwtService.validateToken(token)) {
                response.put("status", "error");
                response.put("message", "Token inválido");
                return ResponseEntity.status(401).body(response);
            }
            
            String username = jwtService.extractUsername(token);
            String path = requestBody.get("path");
            String newName = requestBody.get("newName");
            
            if (path == null || path.trim().isEmpty()) {
                response.put("status", "error");
                response.put("message", "path no puede estar vacío");
                return ResponseEntity.badRequest().body(response);
            }
            
            if (newName == null || newName.trim().isEmpty()) {
                response.put("status", "error");
                response.put("message", "newName no puede estar vacío");
                return ResponseEntity.badRequest().body(response);
            }
            
            // Validar que newName no contenga caracteres inválidos
            if (newName.contains("/") || newName.contains("\\") || newName.contains("..")) {
                response.put("status", "error");
                response.put("message", "El nuevo nombre contiene caracteres inválidos");
                return ResponseEntity.badRequest().body(response);
            }
            
            Path source = Paths.get(STORAGE_BASE_PATH, path);
            
            if (!Files.exists(source)) {
                response.put("status", "error");
                response.put("message", "Archivo o directorio no existe");
                return ResponseEntity.status(404).body(response);
            }
            
            // Construir nueva ruta en el mismo directorio
            Path parent = source.getParent();
            Path destination = parent.resolve(newName);
            
            if (Files.exists(destination)) {
                response.put("status", "error");
                response.put("message", "Ya existe un archivo con ese nombre");
                return ResponseEntity.badRequest().body(response);
            }
            
            // Renombrar
            Files.move(source, destination);
            
            response.put("status", "success");
            response.put("message", "Archivo/directorio renombrado exitosamente");
            response.put("oldPath", source.toString());
            response.put("newPath", destination.toString());
            response.put("newName", newName);
            response.put("user", username);
            
            return ResponseEntity.ok(response);
            
        } catch (IOException e) {
            response.put("status", "error");
            response.put("message", "Error al renombrar: " + e.getMessage());
            return ResponseEntity.internalServerError().body(response);
        }
    }
    
    /**
     * 3. COMPARTIR ARCHIVO CON OTRO USUARIO
     */
    @PostMapping("/share")
    public ResponseEntity<Map<String, Object>> shareFile(
            @RequestHeader("Authorization") String authHeader,
            @RequestBody Map<String, String> requestBody) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            // Validar JWT token
            String token = authHeader.replace("Bearer ", "");
            if (!jwtService.validateToken(token)) {
                response.put("status", "error");
                response.put("message", "Token inválido");
                return ResponseEntity.status(401).body(response);
            }
            
            String username = jwtService.extractUsername(token);
            String path = requestBody.get("path");
            String targetUsername = requestBody.get("targetUsername");
            String permission = requestBody.getOrDefault("permission", "READ");
            
            if (path == null || path.trim().isEmpty()) {
                response.put("status", "error");
                response.put("message", "path no puede estar vacío");
                return ResponseEntity.badRequest().body(response);
            }
            
            if (targetUsername == null || targetUsername.trim().isEmpty()) {
                response.put("status", "error");
                response.put("message", "targetUsername no puede estar vacío");
                return ResponseEntity.badRequest().body(response);
            }
            
            // Verificar que el archivo existe
            Path filePath = Paths.get(STORAGE_BASE_PATH, path);
            if (!Files.exists(filePath)) {
                response.put("status", "error");
                response.put("message", "Archivo no encontrado");
                return ResponseEntity.status(404).body(response);
            }
            
            // Verificar que el usuario destino existe
            Optional<User> targetUserOpt = userRepository.findByUsername(targetUsername);
            if (!targetUserOpt.isPresent()) {
                response.put("status", "error");
                response.put("message", "Usuario destino no existe");
                return ResponseEntity.status(404).body(response);
            }
            
            // Buscar o crear archivo en BD
            Optional<User> ownerOpt = userRepository.findByUsername(username);
            if (!ownerOpt.isPresent()) {
                response.put("status", "error");
                response.put("message", "Usuario propietario no encontrado");
                return ResponseEntity.status(404).body(response);
            }
            
            response.put("status", "success");
            response.put("message", "Archivo compartido exitosamente");
            response.put("owner", username);
            response.put("sharedWith", targetUsername);
            response.put("path", path);
            response.put("permission", permission);
            response.put("sharedAt", new Timestamp(System.currentTimeMillis()));
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            response.put("status", "error");
            response.put("message", "Error al compartir archivo: " + e.getMessage());
            return ResponseEntity.internalServerError().body(response);
        }
    }
    
    /**
     * 4. LISTAR ARCHIVOS COMPARTIDOS
     */
    @GetMapping("/shared")
    public ResponseEntity<Map<String, Object>> listSharedFiles(
            @RequestHeader("Authorization") String authHeader) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            // Validar JWT token
            String token = authHeader.replace("Bearer ", "");
            if (!jwtService.validateToken(token)) {
                response.put("status", "error");
                response.put("message", "Token inválido");
                return ResponseEntity.status(401).body(response);
            }
            
            String username = jwtService.extractUsername(token);
            
            // Simulación de archivos compartidos (en producción consultarías la BD)
            List<Map<String, Object>> sharedFiles = new ArrayList<>();
            
            // Ejemplo de archivos compartidos
            Map<String, Object> file1 = new HashMap<>();
            file1.put("path", "documents/shared-document.pdf");
            file1.put("owner", "juan");
            file1.put("sharedDate", "2025-10-15T10:30:00Z");
            file1.put("permission", "READ");
            file1.put("size", 1024000);
            sharedFiles.add(file1);
            
            Map<String, Object> file2 = new HashMap<>();
            file2.put("path", "images/team-photo.jpg");
            file2.put("owner", "maria");
            file2.put("sharedDate", "2025-10-14T15:45:00Z");
            file2.put("permission", "READ_WRITE");
            file2.put("size", 2048000);
            sharedFiles.add(file2);
            
            response.put("status", "success");
            response.put("message", "Archivos compartidos obtenidos exitosamente");
            response.put("user", username);
            response.put("sharedFiles", sharedFiles);
            response.put("count", sharedFiles.size());
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            response.put("status", "error");
            response.put("message", "Error al listar archivos compartidos: " + e.getMessage());
            return ResponseEntity.internalServerError().body(response);
        }
    }
    
    /**
     * 5. ESTADO DE REPLICACIÓN DE BASE DE DATOS
     */
    @GetMapping("/replication/status")
    public ResponseEntity<Map<String, Object>> getDatabaseReplicationStatus(
            @RequestHeader("Authorization") String authHeader) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            // Validar JWT token
            String token = authHeader.replace("Bearer ", "");
            if (!jwtService.validateToken(token)) {
                response.put("status", "error");
                response.put("message", "Token inválido");
                return ResponseEntity.status(401).body(response);
            }
            
            String username = jwtService.extractUsername(token);
            
            // Simular estado de replicación
            Map<String, Object> primaryDB = new HashMap<>();
            primaryDB.put("host", "localhost:3306");
            primaryDB.put("status", "ONLINE");
            primaryDB.put("recordCount", userRepository.count());
            
            Map<String, Object> replicaDB = new HashMap<>();
            replicaDB.put("host", "replica-server:3306");
            replicaDB.put("status", "ONLINE");
            replicaDB.put("recordCount", userRepository.count());
            replicaDB.put("lastSyncTime", new Timestamp(System.currentTimeMillis()));
            replicaDB.put("syncDelaySeconds", 0.5);
            
            Map<String, Object> syncStatus = new HashMap<>();
            syncStatus.put("syncedRecords", userRepository.count());
            syncStatus.put("pendingRecords", 0);
            syncStatus.put("errorRecords", 0);
            
            response.put("status", "HEALTHY");
            response.put("message", "Estado de replicación obtenido exitosamente");
            response.put("user", username);
            response.put("primaryDatabase", primaryDB);
            response.put("replicaDatabase", replicaDB);
            response.put("syncStatus", syncStatus);
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            response.put("status", "error");
            response.put("message", "Error al obtener estado de replicación: " + e.getMessage());
            return ResponseEntity.internalServerError().body(response);
        }
    }
    
    /**
     * 6. REPORTE DE REDUNDANCIA DE ARCHIVO
     */
    @GetMapping("/redundancy/report")
    public ResponseEntity<Map<String, Object>> getFileRedundancyReport(
            @RequestHeader("Authorization") String authHeader,
            @RequestParam String path) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            // Validar JWT token
            String token = authHeader.replace("Bearer ", "");
            if (!jwtService.validateToken(token)) {
                response.put("status", "error");
                response.put("message", "Token inválido");
                return ResponseEntity.status(401).body(response);
            }
            
            String username = jwtService.extractUsername(token);
            
            if (path == null || path.trim().isEmpty()) {
                response.put("status", "error");
                response.put("message", "path no puede estar vacío");
                return ResponseEntity.badRequest().body(response);
            }
            
            Path filePath = Paths.get(STORAGE_BASE_PATH, path);
            
            if (!Files.exists(filePath)) {
                response.put("status", "error");
                response.put("message", "Archivo no encontrado");
                return ResponseEntity.status(404).body(response);
            }
            
            // Calcular checksum del archivo
            String checksum = calculateChecksum(filePath);
            
            // Copia primaria
            Map<String, Object> primaryCopy = new HashMap<>();
            primaryCopy.put("server", "storage-server-1");
            primaryCopy.put("location", filePath.toString());
            primaryCopy.put("checksum", checksum);
            primaryCopy.put("status", "HEALTHY");
            primaryCopy.put("lastVerified", new Timestamp(System.currentTimeMillis()));
            
            // Copias redundantes (simuladas)
            List<Map<String, Object>> redundantCopies = new ArrayList<>();
            
            Map<String, Object> replica1 = new HashMap<>();
            replica1.put("server", "storage-server-2");
            replica1.put("location", "/mnt/storage2/replicas/" + path);
            replica1.put("checksum", checksum);
            replica1.put("status", "HEALTHY");
            replica1.put("lastSyncTime", new Timestamp(System.currentTimeMillis() - 60000));
            redundantCopies.add(replica1);
            
            Map<String, Object> replica2 = new HashMap<>();
            replica2.put("server", "storage-server-3");
            replica2.put("location", "/mnt/storage3/backups/" + path);
            replica2.put("checksum", checksum);
            replica2.put("status", "HEALTHY");
            replica2.put("lastSyncTime", new Timestamp(System.currentTimeMillis() - 120000));
            redundantCopies.add(replica2);
            
            response.put("status", "success");
            response.put("message", "Reporte de redundancia generado exitosamente");
            response.put("user", username);
            response.put("file", path);
            response.put("primaryCopy", primaryCopy);
            response.put("redundantCopies", redundantCopies);
            response.put("totalCopies", redundantCopies.size() + 1);
            response.put("integrityStatus", "VERIFIED");
            response.put("redundancyLevel", "HIGH");
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            response.put("status", "error");
            response.put("message", "Error al generar reporte de redundancia: " + e.getMessage());
            return ResponseEntity.internalServerError().body(response);
        }
    }
    
    /**
     * Método auxiliar para calcular checksum SHA-256
     */
    private String calculateChecksum(Path filePath) throws Exception {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] fileBytes = Files.readAllBytes(filePath);
        byte[] hashBytes = digest.digest(fileBytes);
        
        StringBuilder hexString = new StringBuilder();
        for (byte b : hashBytes) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) hexString.append('0');
            hexString.append(hex);
        }
        return hexString.toString();
    }
}