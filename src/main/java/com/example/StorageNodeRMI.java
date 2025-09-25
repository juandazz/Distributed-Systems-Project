package com.example;

import java.io.*;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.concurrent.ConcurrentHashMap;
import com.example.model.Node;

public class StorageNodeRMI extends UnicastRemoteObject implements IStorageNode {
    private final Node node;
    private final File storageRoot;
    private final ConcurrentHashMap<String, Long> fileSizes = new ConcurrentHashMap<>();

    public StorageNodeRMI(Node node) throws RemoteException {
        this.node = node;
        this.storageRoot = new File("storage", node.getHostname());
        if (!storageRoot.exists()) storageRoot.mkdirs();
    }

    @Override
    public boolean storeFile(String path, byte[] data) throws RemoteException {
        try {
            if (data.length + node.getUsedBytes() > node.getCapacityBytes()) {
                throw new RemoteException("Insufficient storage space");
            }

            File file = new File(storageRoot, path);
            file.getParentFile().mkdirs();
            try (FileOutputStream fos = new FileOutputStream(file)) {
                fos.write(data);
            }
            long size = data.length;
            node.setUsedBytes(node.getUsedBytes() + size);
            fileSizes.put(path, size);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public byte[] readFile(String path) throws RemoteException {
        try {
            File file = new File(storageRoot, path);
            if (!file.exists()) return null;
            return java.nio.file.Files.readAllBytes(file.toPath());
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public boolean deleteFile(String path) throws RemoteException {
        File file = new File(storageRoot, path);
        if (file.exists()) {
            Long size = fileSizes.remove(path); 
            if (size != null) {                
                node.setUsedBytes(node.getUsedBytes() - size);              
            }
            return file.delete();
        }
        return false;
    }

    @Override
    public boolean createDirectory(String path) throws RemoteException {
        return new File(storageRoot, path).mkdirs();
    }

    @Override
    public String getStatus() throws RemoteException {
        return String.format("Node=%s, Used=%d, Total=%d, Status=%s", 
            node.getHostname(), 
            node.getUsedBytes(), 
            node.getCapacityBytes(),
            node.getStatus());
    }
}