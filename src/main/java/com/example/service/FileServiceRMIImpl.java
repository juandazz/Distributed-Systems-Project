package com.example.service;

import com.example.appserver.interfaces.FileInterface;
import com.example.model.File;
import com.example.appserver.rest.DatabaseClient;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.Timestamp;
import java.time.Instant;

public class FileServiceRMIImpl extends UnicastRemoteObject implements FileInterface {
    private final DatabaseClient dbClient;

    public FileServiceRMIImpl() throws RemoteException {
        super();
        this.dbClient = new DatabaseClient();
    }

    @Override
    public File uploadFile(byte[] fileContent, String fileName, String mimeType, Integer userId) throws RemoteException {
        try {
            File file = new File();
            file.setName(fileName);
            file.setContent(fileContent);
            file.setMimeType(mimeType);
            file.setUserId(userId);
            file.setSizeBytes((long) fileContent.length);
            file.setCreatedAt(Timestamp.from(Instant.now()));

            // Save file metadata in database
            dbClient.saveFileMetadata(userId.toString(), fileName, fileContent.length);

            // Store the actual file content
            String path = userId + "/" + fileName;
            String result = storeFileContent(path, fileContent);
            
            if (result.equals("Archivo subido")) {
                return file;
            } else {
                throw new RemoteException("Error storing file: " + result);
            }
        } catch (Exception e) {
            throw new RemoteException("Error in uploadFile: " + e.getMessage(), e);
        }
    }

    @Override
    public File getFile(Integer fileId) throws RemoteException {
        // Implementation for file retrieval
        // This would need to be implemented based on your storage system
        throw new RemoteException("Method not implemented yet");
    }

    @Override
    public boolean deleteFile(Integer fileId) throws RemoteException {
        // Implementation for file deletion
        // This would need to be implemented based on your storage system
        throw new RemoteException("Method not implemented yet");
    }

    private String storeFileContent(String path, byte[] content) {
        try {
            java.net.http.HttpClient httpClient = java.net.http.HttpClient.newBuilder()
                    .connectTimeout(java.time.Duration.ofSeconds(5))
                    .build();

            String url = "http://localhost:3307/storage/files?path=" + path;
            java.net.http.HttpRequest request = java.net.http.HttpRequest.newBuilder()
                    .uri(new java.net.URI(url))
                    .POST(java.net.http.HttpRequest.BodyPublishers.ofByteArray(content))
                    .timeout(java.time.Duration.ofSeconds(10))
                    .build();

            java.net.http.HttpResponse<String> response = httpClient.send(request, 
                java.net.http.HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200 || response.statusCode() == 201) {
                return "Archivo subido";
            } else {
                return "Error: " + response.statusCode() + " - " + response.body();
            }
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }
}