package com.example.controller;

import com.example.model.User;
import com.example.service.AuthService;
import com.example.service.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    
    @Autowired
    private AuthService authService;
    
    @Autowired
    private JwtService jwtService;
    
    @PostMapping("/register")
    public ResponseEntity<Map<String, Object>> register(@RequestBody Map<String, String> request) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            String username = request.get("username");
            String email = request.get("email");
            String password = request.get("password");
            
            if (username == null || email == null || password == null) {
                response.put("status", "error");
                response.put("message", "Faltan campos requeridos");
                return ResponseEntity.badRequest().body(response);
            }
            
            User newUser = authService.registerUser(username, email, password);
            
            response.put("status", "success");
            response.put("message", "Usuario registrado exitosamente");
            response.put("user", Map.of(
                "id", newUser.getIdUser(),
                "username", newUser.getUsername(),
                "email", newUser.getEmail()
            ));
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            response.put("status", "error");
            response.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
    
    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@RequestBody Map<String, String> request) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            String username = request.get("username");
            String password = request.get("password");
            
            if (username == null || password == null) {
                response.put("status", "error");
                response.put("message", "Username y password son requeridos");
                return ResponseEntity.badRequest().body(response);
            }
            
            Optional<User> userOpt = authService.authenticateUser(username, password);
            
            if (userOpt.isPresent()) {
                User user = userOpt.get();
                
                // Generar token JWT
                String token = jwtService.generateToken(user.getUsername(), user.getIdUser().longValue());
                
                response.put("status", "success");
                response.put("message", "Login exitoso");
                response.put("token", token);
                response.put("user", Map.of(
                    "id", user.getIdUser(),
                    "username", user.getUsername(),
                    "email", user.getEmail()
                ));
                return ResponseEntity.ok(response);
            } else {
                response.put("status", "error");
                response.put("message", "Usuario o contraseña incorrectos");
                return ResponseEntity.status(401).body(response);
            }
            
        } catch (Exception e) {
            response.put("status", "error");
            response.put("message", "Error interno del servidor");
            return ResponseEntity.internalServerError().body(response);
        }
    }
    
    @GetMapping("/check-user/{username}")
    public ResponseEntity<Map<String, Object>> checkUser(@PathVariable String username) {
        Map<String, Object> response = new HashMap<>();
        
        boolean exists = authService.userExists(username);
        response.put("exists", exists);
        response.put("username", username);
        
        return ResponseEntity.ok(response);
    }
    
    @PostMapping("/validate-token")
    public ResponseEntity<Map<String, Object>> validateToken(@RequestHeader("Authorization") String authHeader) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            String token = jwtService.extractTokenFromHeader(authHeader);
            
            if (token == null) {
                response.put("status", "error");
                response.put("message", "Token no encontrado en el header Authorization");
                return ResponseEntity.badRequest().body(response);
            }
            
            if (jwtService.validateToken(token)) {
                String username = jwtService.extractUsername(token);
                Long userId = jwtService.extractUserId(token);
                
                response.put("status", "success");
                response.put("message", "Token válido");
                response.put("user", Map.of(
                    "id", userId,
                    "username", username
                ));
                return ResponseEntity.ok(response);
            } else {
                response.put("status", "error");
                response.put("message", "Token inválido o expirado");
                return ResponseEntity.status(401).body(response);
            }
            
        } catch (Exception e) {
            response.put("status", "error");
            response.put("message", "Error al validar token: " + e.getMessage());
            return ResponseEntity.status(401).body(response);
        }
    }
}