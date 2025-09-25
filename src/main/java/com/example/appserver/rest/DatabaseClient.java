package com.example.appserver.rest;

import org.springframework.stereotype.Component;
import java.util.HashMap;
import java.util.Map;

@Component
public class DatabaseClient {
    private static final Map<String, Map<String, Long>> userFiles = new HashMap<>();

    public void saveFileMetadata(String user, String path, long size) {
        userFiles.computeIfAbsent(user, k -> new HashMap<>()).put(path, size);
    }

    public void createDirectory(String user, String path) {
        
    }

    public void deleteFile(String user, String path) {
        var files = userFiles.get(user);
        if (files != null) files.remove(path);
    }

    public String getStorageReport(String user) {
        var files = userFiles.getOrDefault(user, new HashMap<>());
        long total = files.values().stream().mapToLong(Long::longValue).sum();
        return "Usuario: " + user + ", Espacio usado: " + total + " bytes";
    }
}