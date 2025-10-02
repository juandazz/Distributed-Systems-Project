package com.example.config;

import com.example.IStorageNode;
import com.example.StorageNodeRMI;
import com.example.RMIServer;
import com.example.model.Node;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.rmi.registry.Registry;
import java.rmi.registry.LocateRegistry;
import java.net.InetAddress;
import org.springframework.beans.factory.annotation.Value;
import javax.annotation.PostConstruct;

@Configuration
public class RMIConfig {
    
    @Value("${rmi.port:1099}")
    private int rmiPort;
    
    @PostConstruct
    public void initRMI() {
        try {
            // Inicializar información del nodo
            String hostname = InetAddress.getLocalHost().getHostName();
            String ipAddress = InetAddress.getLocalHost().getHostAddress();
            Node node = new Node(hostname, ipAddress, 1024L * 1024L * 1024L); // 1GB

            // Crear o obtener el registro RMI en el puerto 1099
            Registry registry;
            try {
                registry = LocateRegistry.createRegistry(rmiPort);
                System.out.println("RMI Registry creado en el puerto " + rmiPort);
            } catch (java.rmi.server.ExportException e) {
                registry = LocateRegistry.getRegistry(rmiPort);
                System.out.println("Usando RMI Registry existente en el puerto " + rmiPort);
            }

            // Publicar ambos servicios en el mismo registro
            StorageNodeRMI storageNode = new StorageNodeRMI(node);
            registry.rebind("StorageNode", storageNode);
            System.out.println("Servicio StorageNode publicado en RMI");

            com.example.service.FileServiceRMIImpl fileService = new com.example.service.FileServiceRMIImpl();
            registry.rebind("FileService", fileService);
            System.out.println("Servicio FileService publicado en RMI");

        } catch (Exception e) {
            System.err.println("Error inicializando servicios RMI: " + e.getMessage());
            throw new RuntimeException("Fallo al inicializar servicios RMI", e);
        }
    }
}