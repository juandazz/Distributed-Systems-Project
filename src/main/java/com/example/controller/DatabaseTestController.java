package com.example.controller;

import com.example.model.User;
import com.example.model.Node;
import com.example.repository.UserRepository;
import com.example.repository.NodeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/database")
public class DatabaseTestController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private NodeRepository nodeRepository;

    @GetMapping("/test")
    public ResponseEntity<Map<String, Object>> testDatabaseConnection() {
        Map<String, Object> response = new HashMap<>();
        
        try {
            // Probar conexión básica contando usuarios
            long userCount = userRepository.count();
            long nodeCount = nodeRepository.count();
            
            response.put("status", "success");
            response.put("message", "Conexión a la base de datos exitosa");
            response.put("userCount", userCount);
            response.put("nodeCount", nodeCount);
            response.put("timestamp", Timestamp.from(Instant.now()));
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("status", "error");
            response.put("message", "Error conectando a la base de datos: " + e.getMessage());
            response.put("timestamp", Timestamp.from(Instant.now()));
            
            return ResponseEntity.status(500).body(response);
        }
    }

    @PostMapping("/create-test-user")
    public ResponseEntity<Map<String, Object>> createTestUser() {
        Map<String, Object> response = new HashMap<>();
        
        try {
            // Crear un usuario de prueba
            User testUser = new User();
            testUser.setUsername("test_user_" + System.currentTimeMillis());
            testUser.setEmail("test@example.com");
            testUser.setPasswordHash("test_password_hash");
            testUser.setCreatedAt(Timestamp.from(Instant.now()));
            
            User savedUser = userRepository.save(testUser);
            
            response.put("status", "success");
            response.put("message", "Usuario de prueba creado exitosamente");
            response.put("user", Map.of(
                "id", savedUser.getIdUser(),
                "username", savedUser.getUsername(),
                "email", savedUser.getEmail()
            ));
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("status", "error");
            response.put("message", "Error creando usuario de prueba: " + e.getMessage());
            
            return ResponseEntity.status(500).body(response);
        }
    }

    @PostMapping("/create-test-node")
    public ResponseEntity<Map<String, Object>> createTestNode() {
        Map<String, Object> response = new HashMap<>();
        
        try {
            // Crear un nodo de prueba
            Node testNode = new Node();
            testNode.setHostname("test-node-" + System.currentTimeMillis());
            testNode.setIpAddress("192.168.1.100");
            testNode.setStatus(Node.NodeStatus.ACTIVE);
            testNode.setCapacityBytes(1024L * 1024L * 1024L); // 1GB
            testNode.setUsedBytes(0L);
            
            Node savedNode = nodeRepository.save(testNode);
            
            response.put("status", "success");
            response.put("message", "Nodo de prueba creado exitosamente");
            response.put("node", Map.of(
                "id", savedNode.getIdNode(),
                "hostname", savedNode.getHostname(),
                "ipAddress", savedNode.getIpAddress(),
                "status", savedNode.getStatus().toString(),
                "capacityBytes", savedNode.getCapacityBytes()
            ));
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("status", "error");
            response.put("message", "Error creando nodo de prueba: " + e.getMessage());
            
            return ResponseEntity.status(500).body(response);
        }
    }

    @GetMapping("/users")
    public ResponseEntity<?> getAllUsers() {
        try {
            return ResponseEntity.ok(userRepository.findAll());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error obteniendo usuarios: " + e.getMessage());
        }
    }

    @GetMapping("/nodes")
    public ResponseEntity<?> getAllNodes() {
        try {
            return ResponseEntity.ok(nodeRepository.findAll());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error obteniendo nodos: " + e.getMessage());
        }
    }
}