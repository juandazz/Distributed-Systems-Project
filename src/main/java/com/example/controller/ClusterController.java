package com.example.controller;

import com.example.model.StorageNode;
import com.example.service.LoadBalancerService;
import com.example.service.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/cluster")
public class ClusterController {
    
    @Autowired
    private LoadBalancerService loadBalancerService;
    
    @Autowired
    private JwtService jwtService;
    
    /**
     * Obtener estadísticas del cluster
     */
    @GetMapping("/stats")
    public ResponseEntity<Map<String, Object>> getClusterStats(
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
            Map<String, Object> stats = loadBalancerService.getClusterStats();
            
            response.put("status", "success");
            response.put("message", "Estadísticas del cluster obtenidas exitosamente");
            response.put("user", username);
            response.putAll(stats);
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            response.put("status", "error");
            response.put("message", "Error al obtener estadísticas: " + e.getMessage());
            return ResponseEntity.internalServerError().body(response);
        }
    }
    
    /**
     * Obtener información de todos los nodos
     */
    @GetMapping("/nodes")
    public ResponseEntity<Map<String, Object>> getAllNodes(
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
            
            response.put("status", "success");
            response.put("message", "Nodos obtenidos exitosamente");
            response.put("user", username);
            response.put("nodes", loadBalancerService.getAllNodes().values());
            response.put("totalNodes", loadBalancerService.getAllNodes().size());
            response.put("availableNodes", loadBalancerService.getAvailableNodes().size());
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            response.put("status", "error");
            response.put("message", "Error al obtener nodos: " + e.getMessage());
            return ResponseEntity.internalServerError().body(response);
        }
    }
    
    /**
     * Obtener información de un nodo específico
     */
    @GetMapping("/nodes/{nodeName}")
    public ResponseEntity<Map<String, Object>> getNode(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable String nodeName) {
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
            StorageNode node = loadBalancerService.getNode(nodeName);
            
            if (node == null) {
                response.put("status", "error");
                response.put("message", "Nodo no encontrado");
                return ResponseEntity.status(404).body(response);
            }
            
            response.put("status", "success");
            response.put("message", "Nodo obtenido exitosamente");
            response.put("user", username);
            response.put("node", node);
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            response.put("status", "error");
            response.put("message", "Error al obtener nodo: " + e.getMessage());
            return ResponseEntity.internalServerError().body(response);
        }
    }
    
    /**
     * Recargar configuración de nodos
     */
    @PostMapping("/reload")
    public ResponseEntity<Map<String, Object>> reloadConfiguration(
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
            loadBalancerService.reloadConfiguration();
            
            response.put("status", "success");
            response.put("message", "Configuración recargada exitosamente");
            response.put("user", username);
            response.put("totalNodes", loadBalancerService.getAllNodes().size());
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            response.put("status", "error");
            response.put("message", "Error al recargar configuración: " + e.getMessage());
            return ResponseEntity.internalServerError().body(response);
        }
    }
    
    /**
     * Obtener nodo recomendado para operación (según balanceador)
     */
    @GetMapping("/next-node")
    public ResponseEntity<Map<String, Object>> getNextNode(
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
            StorageNode node = loadBalancerService.getNextNode();
            
            response.put("status", "success");
            response.put("message", "Nodo asignado exitosamente");
            response.put("user", username);
            response.put("node", node);
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            response.put("status", "error");
            response.put("message", "Error al asignar nodo: " + e.getMessage());
            return ResponseEntity.internalServerError().body(response);
        }
    }
}
