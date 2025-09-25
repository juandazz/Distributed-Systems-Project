package com.example;

import com.example.service.FileServiceRMIImpl;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class RMIServer {
    private static final int RMI_PORT = 1099;
    private static final String SERVICE_NAME = "FileService";

    public static void startRMIServer() {
        try {
            // Create and export the registry on the specified port
            Registry registry = LocateRegistry.createRegistry(RMI_PORT);

            // Create the remote service
            FileServiceRMIImpl fileService = new FileServiceRMIImpl();

            // Bind the remote service in the registry
            registry.rebind(SERVICE_NAME, fileService);

            System.out.println("RMI Server is running on port " + RMI_PORT);
        } catch (Exception e) {
            System.err.println("RMI Server error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}