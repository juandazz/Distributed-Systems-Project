package com.example.appserver.soap;

import com.example.appserver.rest.DatabaseClient;
import com.example.model.User;
import com.example.service.AuthService;
import com.example.service.JwtService;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.Base64;
import java.util.Optional;
import javax.jws.WebService;
import javax.jws.WebParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@WebService(endpointInterface = "com.example.appserver.soap.FileService")
public class FileServiceImpl implements FileService {
    private final DatabaseClient dbClient = new DatabaseClient();
    private final HttpClient httpClient = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(5))
            .build();

    private static final String STORAGE_BASE_URL = "http://localhost:8081";
    
    @Autowired
    private JwtService jwtService;
    
    @Autowired
    private AuthService authService;
    
    /**
     * Valida el token JWT y retorna el username
     */
    private String validateTokenAndGetUsername(String token) {
        try {
            if (token == null || token.trim().isEmpty()) {
                return null;
            }
            
            if (!jwtService.validateToken(token)) {
                return null;
            }
            
            return jwtService.extractUsername(token);
        } catch (Exception e) {
            System.err.println("Error validating token: " + e.getMessage());
            return null;
        }
    }

    @Override
    public String createDirectory(@WebParam(name = "token") String token, 
                                @WebParam(name = "path") String path) {
        try {
            // Validar token y obtener username
            String username = validateTokenAndGetUsername(token);
            if (username == null) {
                return "Error: Token inválido o expirado";
            }
            
            // Validar parámetros
            if (path == null || path.trim().isEmpty()) {
                return "Error: Path no puede estar vacío";
            }

            String cleanPath = path.trim().replace("\\", "/");
            
            if (cleanPath.startsWith("/")) {
                cleanPath = cleanPath.substring(1);
            }
            
            // Validar que la ruta no contenga elementos peligrosos
            if (cleanPath.contains("..") || cleanPath.contains("//")) {
                return "Error: Ruta inválida - contiene elementos de navegación no permitidos";
            }
       
            File userBaseDir = new File("storage", username);
            if (!userBaseDir.exists()) {
                userBaseDir.mkdirs();
            }
            
            File targetDirectory;
            if (cleanPath.isEmpty()) {
                targetDirectory = userBaseDir;
            } else {
                targetDirectory = new File(userBaseDir, cleanPath);
            }
            
            boolean success = targetDirectory.mkdirs() || targetDirectory.exists();
            
            if (success) {
                
                try {
                    String fullPath = username + "/" + cleanPath;
                    String url = STORAGE_BASE_URL + "/api/storage/directories?path=" + fullPath;

                    HttpRequest request = HttpRequest.newBuilder()
                            .uri(URI.create(url))
                            .POST(HttpRequest.BodyPublishers.noBody())
                            .timeout(Duration.ofSeconds(10))
                            .build();

                    httpClient.send(request, HttpResponse.BodyHandlers.ofString());
                    
                    
                    dbClient.createDirectory(username, cleanPath);
                    
                    System.out.println("Directory created at: " + targetDirectory.getAbsolutePath());
                } catch (Exception remoteException) {
                    System.err.println("Remote storage error: " + remoteException.getMessage());
                }
                
                return "Directory created successfully: " + targetDirectory.getPath();
            } else {
                return "Failed to create directory: " + cleanPath;
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            return "Error creating directory: " + e.getMessage();
        }
    }

    @Override
    public String uploadFile(@WebParam(name = "token") String token, 
                           @WebParam(name = "path") String path, 
                           @WebParam(name = "data") String data) {
        try {
            // Validar token y obtener username
            String username = validateTokenAndGetUsername(token);
            if (username == null) {
                return "Error: Token inválido o expirado";
            }
            if (path == null || path.trim().isEmpty()) {
                return "Error: Path no puede estar vacío";
            }
            if (data == null || data.trim().isEmpty()) {
                return "Error: Data no puede estar vacío";
            }
            
            String cleanPath = path.trim().replace("\\", "/");
            
            // Remover slash inicial si existe
            if (cleanPath.startsWith("/")) {
                cleanPath = cleanPath.substring(1);
            }
            
            // Crear directorio base del usuario
            File userBaseDir = new File("storage", username);
            if (!userBaseDir.exists()) {
                userBaseDir.mkdirs();
            }
            
            // Crear archivo target
            File targetFile = new File(userBaseDir, cleanPath);
            
            // Crear directorios padre si no existen
            File parentDir = targetFile.getParentFile();
            if (parentDir != null && !parentDir.exists()) {
                parentDir.mkdirs();
            }
            
            // Decodificar datos Base64
            byte[] fileBytes = Base64.getDecoder().decode(data);
            
            // Escribir archivo
            try (FileOutputStream fos = new FileOutputStream(targetFile)) {
                fos.write(fileBytes);
                fos.flush();
            }
            
            // También intentar guardar en storage remoto y base de datos
            try {
                String fullPath = username + "/" + cleanPath;
                String url = STORAGE_BASE_URL + "/api/storage/files?path=" + fullPath;

                HttpRequest request = HttpRequest.newBuilder()
                        .uri(URI.create(url))
                        .POST(HttpRequest.BodyPublishers.ofByteArray(fileBytes))
                        .timeout(Duration.ofSeconds(10))
                        .build();

                httpClient.send(request, HttpResponse.BodyHandlers.ofString());
                dbClient.saveFileMetadata(username, cleanPath, fileBytes.length);
            } catch (Exception remoteException) {
                System.err.println("Remote storage error: " + remoteException.getMessage());
            }
            
            return "File uploaded successfully: " + cleanPath;
            
        } catch (IllegalArgumentException e) {
            return "Error: Invalid Base64 data - " + e.getMessage();
        } catch (IOException e) {
            return "Error: Failed to write file - " + e.getMessage();
        } catch (Exception e) {
            e.printStackTrace();
            return "Error uploading file: " + e.getMessage();
        }
    }

    @Override
    public byte[] downloadFile(@WebParam(name = "token") String token, 
                             @WebParam(name = "path") String path) {
        try {
            // Validar token y obtener username
            String username = validateTokenAndGetUsername(token);
            if (username == null) {
                System.err.println("Error: Token inválido o expirado");
                return null;
            }
            if (path == null || path.trim().isEmpty()) {
                System.err.println("Error: Path no puede estar vacío");
                return null;
            }

            String cleanPath = path.trim().replace("\\", "/");
            
            // Remover slash inicial si existe
            if (cleanPath.startsWith("/")) {
                cleanPath = cleanPath.substring(1);
            }

            // Validar que la ruta no contenga elementos peligrosos
            if (cleanPath.contains("..") || cleanPath.contains("//")) {
                System.err.println("Error: Ruta inválida - contiene elementos de navegación no permitidos");
                return null;
            }

            // Intentar leer desde almacenamiento local primero
            File userBaseDir = new File("storage", username);
            File targetFile = new File(userBaseDir, cleanPath);
            
            if (targetFile.exists() && targetFile.isFile()) {
                try {
                    byte[] fileData = java.nio.file.Files.readAllBytes(targetFile.toPath());
                    System.out.println("File downloaded from local storage: " + targetFile.getAbsolutePath());
                    return fileData;
                } catch (IOException e) {
                    System.err.println("Error reading local file: " + e.getMessage());
                }
            }

            // Fallback: intentar descargar desde almacenamiento remoto
            try {
                String fullPath = username + "/" + cleanPath;
                String url = STORAGE_BASE_URL + "/api/storage/files?path=" + fullPath;

                HttpRequest request = HttpRequest.newBuilder()
                        .uri(URI.create(url))
                        .GET()
                        .timeout(Duration.ofSeconds(10))
                        .build();

                HttpResponse<byte[]> response = httpClient.send(request, HttpResponse.BodyHandlers.ofByteArray());

                if (response.statusCode() == 200) {
                    System.out.println("File downloaded from remote storage: " + cleanPath);
                    return response.body();
                } else {
                    System.err.println("Error al descargar desde storage remoto: " + response.statusCode());
                }
            } catch (Exception remoteException) {
                System.err.println("Remote storage error: " + remoteException.getMessage());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        
        System.err.println("File not found: " + path);
        return null;
    }

    @Override
    public String deleteFile(@WebParam(name = "token") String token, 
                           @WebParam(name = "path") String path) {
        try {
            // Validar token y obtener username
            String username = validateTokenAndGetUsername(token);
            if (username == null) {
                return "Error: Token inválido o expirado";
            }
            
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
    public String getStorageReport(@WebParam(name = "token") String token) {
        // Validar token y obtener username
        String username = validateTokenAndGetUsername(token);
        if (username == null) {
            return "Error: Token inválido o expirado";
        }
        
        return dbClient.getStorageReport(username);
    }
    
    @Override
    public String authenticateUser(@WebParam(name = "username") String username,
                                 @WebParam(name = "password") String password) {
        try {
            Optional<User> userOpt = authService.authenticateUser(username, password);
            
            if (userOpt.isPresent()) {
                User user = userOpt.get();
                String token = jwtService.generateToken(user.getUsername(), user.getIdUser().longValue());
                return token;
            } else {
                return "ERROR: Usuario o contraseña incorrectos";
            }
        } catch (Exception e) {
            return "ERROR: " + e.getMessage();
        }
    }
}