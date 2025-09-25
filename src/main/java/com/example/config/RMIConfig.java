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
    
    @Value("${rmi.node.port:1098}")
    private int nodeRmiPort;
    
    @PostConstruct
    public void initRMI() {
        try {
            // Initialize node information
            String hostname = InetAddress.getLocalHost().getHostName();
            String ipAddress = InetAddress.getLocalHost().getHostAddress();
            Node node = new Node(hostname, ipAddress, 1024L * 1024L * 1024L); // 1GB capacity
            
            // Start Node RMI Server
            Registry nodeRegistry = LocateRegistry.createRegistry(nodeRmiPort);
            StorageNodeRMI storageNode = new StorageNodeRMI(node);
            nodeRegistry.rebind("StorageNode", storageNode);
            System.out.println("Node RMI Server started on port " + nodeRmiPort);

            // Start File Services RMI Server
            Registry registry = LocateRegistry.createRegistry(rmiPort);
            RMIServer.startRMIServer();
            System.out.println("File Services RMI Server started on port " + rmiPort);
            
        } catch (Exception e) {
            System.err.println("Error initializing RMI services: " + e.getMessage());
            throw new RuntimeException("Failed to initialize RMI services", e);
        }
    }
}