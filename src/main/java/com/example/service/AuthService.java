package com.example.service;

import com.example.model.User;
import com.example.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class AuthService {
    
    @Autowired
    private UserRepository userRepository;
    
    /**
     * Registra un nuevo usuario
     */
    public User registerUser(String username, String email, String password) throws Exception {
        // Verificar si el usuario ya existe
        if (userRepository.existsByUsername(username)) {
            throw new Exception("El nombre de usuario ya existe");
        }
        
        if (userRepository.existsByEmail(email)) {
            throw new Exception("El email ya está registrado");
        }
        
        // Crear hash de la contraseña
        String passwordHash = hashPassword(password);
        
        // Crear nuevo usuario
        User newUser = new User();
        newUser.setUsername(username);
        newUser.setEmail(email);
        newUser.setPasswordHash(passwordHash);
        newUser.setCreatedAt(Timestamp.valueOf(LocalDateTime.now()));
        
        return userRepository.save(newUser);
    }
    
    /**
     * Autentica un usuario
     */
    public Optional<User> authenticateUser(String username, String password) {
        Optional<User> userOpt = userRepository.findByUsername(username);
        
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            String hashedPassword = hashPassword(password);
            
            if (user.getPasswordHash().equals(hashedPassword)) {
                return Optional.of(user);
            }
        }
        
        return Optional.empty();
    }
    
    /**
     * Verifica si un usuario existe por username
     */
    public boolean userExists(String username) {
        return userRepository.existsByUsername(username);
    }
    
    /**
     * Obtiene un usuario por username
     */
    public Optional<User> getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }
    
    /**
     * Crea un hash SHA-256 de la contraseña
     */
    private String hashPassword(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(password.getBytes());
            StringBuilder hexString = new StringBuilder();
            
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }
            
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error al crear hash de contraseña", e);
        }
    }
}