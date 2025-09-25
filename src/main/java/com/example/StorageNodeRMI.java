package com.example;

import java.io.*;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.concurrent.ConcurrentHashMap;

public class StorageNodeRMI extends UnicastRemoteObject implements IStorageNode {
    private final String nodeId;
    private final File storageRoot;
    private long totalCapacity = 10_000_000_000L; // 10 GB
    private long usedSpace = 0;
    private final ConcurrentHashMap<String, Long> fileSizes = new ConcurrentHashMap<>();

    protected StorageNodeRMI(String nodeId, String storagePath) throws RemoteException {
        this.nodeId = nodeId;
        this.storageRoot = new File(storagePath);
        if (!storageRoot.exists()) storageRoot.mkdirs();
    }

    @Override
    public boolean storeFile(String path, byte[] data) throws RemoteException {
        try {
            File file = new File(storageRoot, path);
            file.getParentFile().mkdirs();
            try (FileOutputStream fos = new FileOutputStream(file)) {
                fos.write(data);
            }
            long size = data.length;
            usedSpace += size;
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
                usedSpace -= size;              
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
        return String.format("Node=%s, Used=%d, Total=%d", nodeId, usedSpace, totalCapacity);
    }
}