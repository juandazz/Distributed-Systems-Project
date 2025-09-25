package com.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.beans.factory.annotation.Value;

@SpringBootApplication
@ComponentScan(basePackages = "com.example")
public class StorageNodeApp {
    
    @Value("${server.port:8080}")
    private int serverPort;

    public static void main(String[] args) {
        if (args.length < 1) {
            System.err.println("Usage: java StorageNodeApp <nodeId>");
            System.exit(1);
        }

        String nodeId = args[0];
        System.setProperty("node.id", nodeId);
        
        try {
            // Start Spring Boot application
            SpringApplication.run(StorageNodeApp.class, args);
        } catch (Exception e) {
            System.err.println("Error starting the system: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
    }
}