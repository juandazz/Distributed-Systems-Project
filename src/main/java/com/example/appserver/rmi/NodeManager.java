package com.example.appserver.rmi;

import com.example.IStorageNode;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class NodeManager {

    // Lista de nodos disponibles (en producción, esto vendría de una BD o config)
    private static final List<NodeInfo> NODES = Arrays.asList(
        new NodeInfo("node1", "localhost", 1098)
       
    );

    // Cache de conexiones RMI (por rendimiento)
    private final ConcurrentHashMap<String, IStorageNode> nodeCache = new ConcurrentHashMap<>();

    public IStorageNode getAvailableNode() throws RemoteException, NotBoundException {
        if (NODES.isEmpty()) {
            throw new RemoteException("No hay nodos de almacenamiento configurados");
        }

        NodeInfo node = NODES.get(0); 
        return getNodeConnection(node);
    }

  
    private IStorageNode getNodeConnection(NodeInfo node) throws RemoteException, NotBoundException {
        return nodeCache.computeIfAbsent(node.id, id -> {
            try {
                Registry registry = LocateRegistry.getRegistry(node.host, node.port);
                return (IStorageNode) registry.lookup("StorageNode");
            } catch (RemoteException | NotBoundException e) {
                throw new RuntimeException("Error conectando al nodo " + node.id, e);
            }
        });
    }

    
    private static class NodeInfo {
        final String id;
        final String host;
        final int port;

        NodeInfo(String id, String host, int port) {
            this.id = id;
            this.host = host;
            this.port = port;
        }
    }
}