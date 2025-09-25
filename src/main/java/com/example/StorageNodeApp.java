package com.example;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class StorageNodeApp {
    public static void main(String[] args) {
        try {
            String nodeId = args.length > 0 ? args[0] : "node1";
            String storagePath = args.length > 1 ? args[1] : "./storage/" + nodeId;

            IStorageNode node = new StorageNodeRMI(nodeId, storagePath);
            Registry registry = LocateRegistry.createRegistry(1098);
            registry.rebind("StorageNode", node);
            System.out.println("Nodo de almacenamiento '" + nodeId + "' listo en RMI (puerto 1098)");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}