package com.example.service;

import com.example.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Optional;
import java.util.Scanner;

@Service
public class ConsoleAuthService {
    
    @Autowired
    private AuthService authService;
    
    private Scanner scanner = new Scanner(System.in);
    private User currentUser = null;
    
    /**
     * Inicia el proceso de autenticación por consola
     */
    public User startConsoleAuth() {
        System.out.println("\n=== SISTEMA DE AUTENTICACIÓN ===");
        System.out.println("1. Iniciar Sesión");
        System.out.println("2. Registrarse");
        System.out.println("3. Salir");
        System.out.print("Seleccione una opción: ");
        
        try {
            int option = Integer.parseInt(scanner.nextLine());
            
            switch (option) {
                case 1:
                    return handleLogin();
                case 2:
                    return handleRegister();
                case 3:
                    System.out.println("Saliendo del sistema...");
                    return null;
                default:
                    System.out.println("Opción inválida. Intente de nuevo.");
                    return startConsoleAuth();
            }
        } catch (NumberFormatException e) {
            System.out.println("Por favor ingrese un número válido.");
            return startConsoleAuth();
        }
    }
    
    /**
     * Maneja el proceso de login
     */
    private User handleLogin() {
        System.out.println("\n--- INICIAR SESIÓN ---");
        System.out.print("Nombre de usuario: ");
        String username = scanner.nextLine().trim();
        
        if (username.isEmpty()) {
            System.out.println("El nombre de usuario no puede estar vacío.");
            return handleLogin();
        }
        
        System.out.print("Contraseña: ");
        String password = scanner.nextLine();
        
        if (password.isEmpty()) {
            System.out.println("La contraseña no puede estar vacía.");
            return handleLogin();
        }
        
        Optional<User> userOpt = authService.authenticateUser(username, password);
        
        if (userOpt.isPresent()) {
            currentUser = userOpt.get();
            System.out.println("¡Bienvenido, " + currentUser.getUsername() + "!");
            System.out.println("Sesión iniciada exitosamente.");
            return currentUser;
        } else {
            System.out.println("Usuario o contraseña incorrectos. Intente de nuevo.");
            System.out.println("1. Reintentar");
            System.out.println("2. Volver al menú principal");
            System.out.print("Seleccione una opción: ");
            
            try {
                int option = Integer.parseInt(scanner.nextLine());
                if (option == 1) {
                    return handleLogin();
                } else {
                    return startConsoleAuth();
                }
            } catch (NumberFormatException e) {
                return startConsoleAuth();
            }
        }
    }
    
    /**
     * Maneja el proceso de registro
     */
    private User handleRegister() {
        System.out.println("\n--- REGISTRAR USUARIO ---");
        
        System.out.print("Nombre de usuario: ");
        String username = scanner.nextLine().trim();
        
        if (username.isEmpty()) {
            System.out.println("El nombre de usuario no puede estar vacío.");
            return handleRegister();
        }
        
        if (authService.userExists(username)) {
            System.out.println("El nombre de usuario ya existe. Intente con otro.");
            return handleRegister();
        }
        
        System.out.print("Email: ");
        String email = scanner.nextLine().trim();
        
        if (email.isEmpty() || !isValidEmail(email)) {
            System.out.println("Por favor ingrese un email válido.");
            return handleRegister();
        }
        
        System.out.print("Contraseña: ");
        String password = scanner.nextLine();
        
        if (password.length() < 4) {
            System.out.println("La contraseña debe tener al menos 4 caracteres.");
            return handleRegister();
        }
        
        System.out.print("Confirmar contraseña: ");
        String confirmPassword = scanner.nextLine();
        
        if (!password.equals(confirmPassword)) {
            System.out.println("Las contraseñas no coinciden. Intente de nuevo.");
            return handleRegister();
        }
        
        try {
            User newUser = authService.registerUser(username, email, password);
            currentUser = newUser;
            System.out.println("¡Usuario registrado exitosamente!");
            System.out.println("¡Bienvenido, " + newUser.getUsername() + "!");
            return newUser;
        } catch (Exception e) {
            System.out.println("Error al registrar usuario: " + e.getMessage());
            System.out.println("1. Reintentar");
            System.out.println("2. Volver al menú principal");
            System.out.print("Seleccione una opción: ");
            
            try {
                int option = Integer.parseInt(scanner.nextLine());
                if (option == 1) {
                    return handleRegister();
                } else {
                    return startConsoleAuth();
                }
            } catch (NumberFormatException ex) {
                return startConsoleAuth();
            }
        }
    }
    
    /**
     * Valida el formato de email básico
     */
    private boolean isValidEmail(String email) {
        return email.contains("@") && email.contains(".") && email.length() > 5;
    }
    
    /**
     * Obtiene el usuario actual logueado
     */
    public User getCurrentUser() {
        return currentUser;
    }
    
    /**
     * Cierra la sesión actual
     */
    public void logout() {
        if (currentUser != null) {
            System.out.println("Sesión de " + currentUser.getUsername() + " cerrada.");
            currentUser = null;
        }
    }
    
    /**
     * Verifica si hay un usuario logueado
     */
    public boolean isLoggedIn() {
        return currentUser != null;
    }
}