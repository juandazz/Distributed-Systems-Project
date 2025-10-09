package com.example;

import com.example.model.User;
import com.example.service.ConsoleAuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("!test")
public class InteractiveAuthRunner implements CommandLineRunner {
    
    @Autowired
    private ConsoleAuthService consoleAuthService;
    
    @Override
    public void run(String... args) throws Exception {
        // Solo ejecutar autenticación interactiva si no es un test
        if (args.length > 0 && "interactive".equals(args[0])) {
            System.out.println("\n" + "=".repeat(50));
            System.out.println("    STORAGE NODE - SISTEMA DISTRIBUIDO");
            System.out.println("=".repeat(50));
            
            // Iniciar proceso de autenticación
            User authenticatedUser = consoleAuthService.startConsoleAuth();
            
            if (authenticatedUser != null) {
                System.out.println("\n" + "=".repeat(50));
                System.out.println("Usuario: " + authenticatedUser.getUsername());
                System.out.println("Email: " + authenticatedUser.getEmail());
                System.out.println("ID: " + authenticatedUser.getIdUser());
                System.out.println("Sistema iniciado correctamente");
                System.out.println("=".repeat(50));
                
                // Aquí puedes agregar lógica adicional después del login
                showMainMenu();
            } else {
                System.out.println("Cerrando sistema...");
                System.exit(0);
            }
        }
    }
    
    private void showMainMenu() {
        System.out.println("\n--- MENÚ PRINCIPAL ---");
        System.out.println("El nodo de almacenamiento está ejecutándose...");
        System.out.println("Servicios disponibles:");
        System.out.println("- Servidor RMI en puerto 1099");
        System.out.println("- Servicio SOAP en puerto 8090");
        System.out.println("- API REST en puerto 8081");
        System.out.println("- Base de datos MySQL conectada");
        System.out.println("\nPresiona Ctrl+C para detener el sistema.");
        
        // Mantener la aplicación ejecutándose
        try {
            while (consoleAuthService.isLoggedIn()) {
                Thread.sleep(1000);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.out.println("\nSistema detenido por el usuario.");
        }
    }
}