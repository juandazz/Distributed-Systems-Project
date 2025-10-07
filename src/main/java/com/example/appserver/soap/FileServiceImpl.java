package com.example.appserver.soap;

import com.example.appserver.rest.DatabaseClient;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import javax.jws.WebService;
import javax.jws.WebParam;
import org.springframework.stereotype.Service;

@Service
@WebService(endpointInterface = "com.example.appserver.soap.FileService")
public class FileServiceImpl implements FileService {
    private final DatabaseClient dbClient = new DatabaseClient();
    private final HttpClient httpClient = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(5))
            .build();

    private static final String STORAGE_BASE_URL = "http://localhost:8080";

    @Override
    public String createDirectory(@WebParam(name = "username") String username, 
                                @WebParam(name = "path") String path) {
        try {
            String fullPath = username + "/" + path;
            String url = STORAGE_BASE_URL + "/api/storage/directories?path=" + fullPath;

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .POST(HttpRequest.BodyPublishers.noBody())
                    .timeout(Duration.ofSeconds(10))
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200 || response.statusCode() == 201) {
                dbClient.createDirectory(username, path);
                return "Directorio creado";
            } else {
                System.err.println("Error del storage: " + response.statusCode() + " - " + response.body());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "Error al crear directorio";
    }

    @Override
    public String uploadFile(@WebParam(name = "username") String username, 
                           @WebParam(name = "path") String path, 
                           @WebParam(name = "data") byte[] data) {
        try {
            // Validar que los parámetros no sean null
            if (username == null || path == null || data == null) {
                return "Error: Parámetros inválidos (username, path o data son null)";
            }
            
            String fullPath = username + "/" + path;
            String url = STORAGE_BASE_URL + "/api/storage/files?path=" + fullPath;

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .POST(HttpRequest.BodyPublishers.ofByteArray(data))
                    .timeout(Duration.ofSeconds(10))
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200 || response.statusCode() == 201) {
                dbClient.saveFileMetadata(username, path, data.length);
                return "Archivo subido";
            } else {
                System.err.println("Error del storage: " + response.statusCode() + " - " + response.body());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "Error al subir archivo";
    }

    @Override
    public byte[] downloadFile(@WebParam(name = "username") String username, 
                             @WebParam(name = "path") String path) {
        try {
            String fullPath = username + "/" + path;
            String url = STORAGE_BASE_URL + "/api/storage/files?path=" + fullPath;

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .GET()
                    .timeout(Duration.ofSeconds(10))
                    .build();

            HttpResponse<byte[]> response = httpClient.send(request, HttpResponse.BodyHandlers.ofByteArray());

            if (response.statusCode() == 200) {
                return response.body();
            } else {
                System.err.println("Error al descargar: " + response.statusCode());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public String deleteFile(@WebParam(name = "username") String username, 
                           @WebParam(name = "path") String path) {
        try {
            String fullPath = username + "/" + path;
            String url = STORAGE_BASE_URL + "/api/storage/files?path=" + fullPath;

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .DELETE()
                    .timeout(Duration.ofSeconds(10))
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200 || response.statusCode() == 204) {
                dbClient.deleteFile(username, path);
                return "Archivo eliminado";
            } else {
                System.err.println("Error al eliminar: " + response.statusCode() + " - " + response.body());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "Error al eliminar";
    }

    @Override
    public String getStorageReport(@WebParam(name = "username") String username) {
        return dbClient.getStorageReport(username);
    }
}