package com.example.appserver.soap;

import com.example.appserver.rest.DatabaseClient;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import javax.jws.WebService;
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
    public String createDirectory(String username, String path) {
        try {
            String fullPath = username + "/" + path;
            String url = STORAGE_BASE_URL + "/storage/dirs?path=" + fullPath;

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
    public String uploadFile(String username, String path, byte[] data) {
        try {
            String fullPath = username + "/" + path;
            String url = STORAGE_BASE_URL + "/storage/files?path=" + fullPath;

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
    public byte[] downloadFile(String username, String path) {
        try {
            String fullPath = username + "/" + path;
            String url = STORAGE_BASE_URL + "/storage/files?path=" + fullPath;

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
    public String deleteFile(String username, String path) {
        try {
            String fullPath = username + "/" + path;
            String url = STORAGE_BASE_URL + "/storage/files?path=" + fullPath;

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
    public String getStorageReport(String username) {
        return dbClient.getStorageReport(username);
    }
}