package com.example.appserver.rest;

import com.example.appserver.interfaces.FileInterface;
import com.example.appserver.soap.FileServiceImpl;
import com.example.model.File;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

@RestController
@RequestMapping("/api/files")
public class FileController {
    private final FileServiceImpl soapFileService;
    private FileInterface rmiFileService;
    private static final String RMI_SERVER = "localhost";
    private static final int RMI_PORT = 1099;
    private static final String RMI_SERVICE_NAME = "FileService";

    public FileController(FileServiceImpl soapFileService) {
        this.soapFileService = soapFileService;
        try {
            Registry registry = LocateRegistry.getRegistry(RMI_SERVER, RMI_PORT);
            this.rmiFileService = (FileInterface) registry.lookup(RMI_SERVICE_NAME);
        } catch (Exception e) {
            System.err.println("Error connecting to RMI service: " + e.getMessage());
        }
    }

    @GetMapping("/test")
    public ResponseEntity<String> testEndpoint() {
        return ResponseEntity.ok("File service is running!");
    }

    @PostMapping("/upload")
    public ResponseEntity<?> uploadFile(
            @RequestParam(value = "file", required = false) MultipartFile file,
            @RequestParam("username") String username,
            @RequestParam("path") String path,
            @RequestParam(value = "useRmi", defaultValue = "false") boolean useRmi) {
        try {
            // Para pruebas sin archivo
            if (file == null) {
                return ResponseEntity.ok("Endpoint working! Ready to receive files.");
            }
            byte[] fileData = file.getBytes();
            
            if (useRmi && rmiFileService != null) {
                // Use RMI implementation
                File uploadedFile = rmiFileService.uploadFile(fileData, path, 
                    file.getContentType(), Integer.parseInt(username));
                return ResponseEntity.ok(uploadedFile);
            } else {
                // Use SOAP implementation
                String result = soapFileService.uploadFile(username, path, fileData);
                return ResponseEntity.ok(result);
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body("Error al procesar el archivo: " + e.getMessage());
        }
    }

    @GetMapping("/download")
    public ResponseEntity<byte[]> downloadFile(
            @RequestParam("username") String username,
            @RequestParam("path") String path,
            @RequestParam(value = "useRmi", defaultValue = "false") boolean useRmi) {
        try {
            byte[] fileData;
            if (useRmi && rmiFileService != null) {
                // Implement RMI download when needed
                return ResponseEntity.badRequest().build();
            } else {
                fileData = soapFileService.downloadFile(username, path);
            }
            
            if (fileData != null) {
                return ResponseEntity.ok()
                        .header("Content-Disposition", "attachment; filename=" + path)
                        .body(fileData);
            }
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/report")
    public ResponseEntity<String> getStorageReport(
            @RequestParam("username") String username,
            @RequestParam(value = "useRmi", defaultValue = "false") boolean useRmi) {
        try {
            if (useRmi && rmiFileService != null) {
                // Implement RMI report when needed
                return ResponseEntity.badRequest().body("RMI report not implemented");
            } else {
                String report = soapFileService.getStorageReport(username);
                return ResponseEntity.ok(report);
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }
}