package com.example.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.HashMap;

@RestController
@RequestMapping("/api/storage")
public class StorageController {
    
    private static final String STORAGE_BASE_PATH = "C:\\Users\\juand\\Desktop\\storagenode\\storage";
    
    @PostMapping("/directories")
    public ResponseEntity<Map<String, Object>> createDirectory(@RequestParam String path) {
        Map<String, Object> response = new HashMap<>();
        
        try {
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
            
            return ResponseEntity.ok(response);
            
        } catch (IOException e) {
            response.put("status", "error");
            response.put("message", "Error al crear directorio: " + e.getMessage());
            return ResponseEntity.internalServerError().body(response);
        }
    }
    
    @PostMapping("/files")
    public ResponseEntity<Map<String, Object>> uploadFile(@RequestParam String path, @RequestBody byte[] data) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            if (path == null || path.trim().isEmpty()) {
                response.put("status", "error");
                response.put("message", "Path no puede estar vacío");
                return ResponseEntity.badRequest().body(response);
            }
            
            // Construir la ruta completa
            Path fullPath = Paths.get(STORAGE_BASE_PATH, path);
            
            // Crear directorios padre si no existen
            Files.createDirectories(fullPath.getParent());
            
            // Escribir archivo
            Files.write(fullPath, data);
            
            response.put("status", "success");
            response.put("message", "Archivo subido exitosamente");
            response.put("path", fullPath.toString());
            response.put("size", data.length);
            
            return ResponseEntity.ok(response);
            
        } catch (IOException e) {
            response.put("status", "error");
            response.put("message", "Error al subir archivo: " + e.getMessage());
            return ResponseEntity.internalServerError().body(response);
        }
    }
    
    @GetMapping("/files")
    public ResponseEntity<byte[]> downloadFile(@RequestParam String path) {
        try {
            if (path == null || path.trim().isEmpty()) {
                return ResponseEntity.badRequest().build();
            }
            
            // Construir la ruta completa
            Path fullPath = Paths.get(STORAGE_BASE_PATH, path);
            
            if (!Files.exists(fullPath)) {
                return ResponseEntity.notFound().build();
            }
            
            byte[] data = Files.readAllBytes(fullPath);
            return ResponseEntity.ok(data);
            
        } catch (IOException e) {
            return ResponseEntity.internalServerError().build();
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
}